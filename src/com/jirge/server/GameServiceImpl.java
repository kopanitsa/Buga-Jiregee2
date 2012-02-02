package com.jirge.server;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.*;
import static com.newatlanta.appengine.taskqueue.Deferred.*;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.jirge.client.GameService;
import com.jirge.server.tasks.SendNewPlayer;
import com.jirge.server.tasks.StartGame;
import com.jirge.server.tasks.TurnChanged;
import com.jirge.server.tasks.UpdateBoard;
import com.jirge.shared.LoginResults;
import com.jirge.shared.UpdateBoardInfo;
import com.jirge.util.JdoUtil;

@SuppressWarnings("serial")
public class GameServiceImpl extends RemoteServiceServlet implements
		GameService {

	private static final int MAX_PLAYERS = 2;
	private static final int MAX_WAIT_TIME_MILLIS = 60000;

	private static final String PLAYER = "player";
	private static final String GAME_ID = "game_id";

	public static final String CURRENT_GAME_ID = "current_game_id";
	private static final String CURRENT_GAME_NUM_PLAYERS = "num_players";
	private final Logger mLogger = Logger.getLogger(this.getClass().getName());

	public LoginResults login(final String name, Long gameId) {
		mLogger.info("GameServiceImpl#login name:" + name + " id:" + gameId);
		if (gameId == null) {
			gameId = reserveGame(5); // temporary value
		}
		HttpSession session = getThreadLocalRequest().getSession();
		session.setAttribute(GAME_ID, gameId);

		BugaJiregeeGame game = getGameById(gameId);

		Player player;
		if (game.getPlayers().size() == 0) {
			// 1st player is deer.
			player = new Player(BugaJiregeePiece.TYPE_DEER);
		} else {
			// 2nd player is dog.
			player = new Player(BugaJiregeePiece.TYPE_DOG);
		}
		player.setName(name);
		session.setAttribute(PLAYER, player);

		PersistenceManager pm = JdoUtil.getPm();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			game.getPlayers().add(player);
			pm.makePersistent(game);
			tx.commit();
		} catch (ConcurrentModificationException ex) {
			// Someone else tried to modify the game at the same time.
			// Just let the client retry.
			return new LoginResults(gameId, null, null);
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}

		defer(new SendNewPlayer(player, game), getTaskOptions());

		if (game.getPlayers().size() >= MAX_PLAYERS) {
			// Start game
			defer(new StartGame(gameId), getTaskOptions().countdownMillis(1000));

			// Initialize game
			initGame(game);

			List<UpdateBoardInfo> updateBoardInfo = game
					.getLastUpdateBoardInfo();
			defer(new UpdateBoard(updateBoardInfo, game), getTaskOptions()
					.countdownMillis(2000));

			defer(new TurnChanged(game), getTaskOptions().countdownMillis(3000));
		}

		Date estimatedStartTime = new Date(game.getTimeCreated().getTime() + 60);

		String channelId = PushServer.createChannel(player);
		return new LoginResults(null, channelId, estimatedStartTime);
	}

	public int[] getAccessiblePoints(int index) {
		mLogger.warning("getAccessiblePoints start, index=" + index);
		BugaJiregeeGame game = getGameForSession();
		BugaJiregeePiece piece = game.getPieceByPointIndex(index);

		if (piece == null) {
			mLogger.severe("getAccessiblePoints error : piece is null");
			return new int[0];
		}

		List<BugaJiregeePoint> pointList = game.getAccessiblePoints(piece);
		int[] accessiblePoints = new int[pointList.size()];

		mLogger.warning("getAccessiblePoints, size=" + accessiblePoints.length);
		for (int i = 0; i < accessiblePoints.length; i++) {
			accessiblePoints[i] = pointList.get(i).getIndex();
		}
		mLogger.warning("getAccessiblePoints end");
		return accessiblePoints;
	}

	public Boolean movePiece(int fromIndex, int toIndex) {
		mLogger.warning("movePiece start, fromIndex=" + fromIndex
				+ ", toIndex=" + toIndex);
		BugaJiregeeGame game = getGameForSession();
		BugaJiregeePiece piece = game.getPieceByPointIndex(fromIndex);
		BugaJiregeePoint toPoint = game.getBoard().getPoint(toIndex);

		if (piece == null) {
			mLogger.severe("movePiece error : piece is null");
			return new Boolean(false);
		}

		Boolean moveSuccess = false;
		PersistenceManager pm = JdoUtil.getPm();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			moveSuccess = game.movePiece(piece, toPoint);
			pm.makePersistent(game);
			tx.commit();
		} catch (ConcurrentModificationException ex) {
			// Someone else tried to modify the game at the same time.
			// Just let the client retry.
			return new Boolean(false);
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}

		if (moveSuccess) {
			List<UpdateBoardInfo> updateBoardInfo = game
					.getLastUpdateBoardInfo();
			defer(new UpdateBoard(updateBoardInfo, game), getTaskOptions()
					.countdownMillis(1000));

			defer(new TurnChanged(game), getTaskOptions().countdownMillis(2000));
		}

		return moveSuccess;
	}

	private boolean tryCreateGame(long gameId) {
		PersistenceManager pm = JdoUtil.getPm();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			BugaJiregeeGame game = new BugaJiregeeGame(gameId);
			pm.makePersistent(game);
			mLogger.info("GameServerImpl:tryCreateGame" + gameId);
			tx.commit();
		} catch (JDOException e) {
			e.printStackTrace();
			// NB(tobyr) Need to figure out really what exception to catch here.
			return false;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}

		// ---------DEBUG-----------
		Query qAll = pm.newQuery(BugaJiregeeGame.class);
		Collection cAll = (Collection) qAll.execute();
		mLogger.warning("[Game number]All:" + cAll.size());
		Query q = pm.newQuery(BugaJiregeeGame.class, "id == " + gameId);
		Collection c = (Collection) q.execute();
		mLogger.warning("[Game number]filter:" + c.size());
		if (gameId > 1) {
			long prevGameId = gameId - 1;
			Query qp = pm
					.newQuery(BugaJiregeeGame.class, "id == " + prevGameId);
			Collection cp = (Collection) qp.execute();
			mLogger.warning("[Game number]filter:" + cp.size());
		}
		// ---------DEBUG-----------

		return true;
	}

	private BugaJiregeeGame getGameForSession() {
		Long gameId = (Long) getThreadLocalRequest().getSession().getAttribute(
				GAME_ID);
		return getGameById(gameId);
	}

	private Player getPlayerForSession() {
		return (Player) getThreadLocalRequest().getSession().getAttribute(
				PLAYER);
	}

	public static TaskOptions getTaskOptions() {
		return url("/tasks/deferred");
	}

	private static BugaJiregeeGame getGameById(Long gameId) {
		PersistenceManager pm = JdoUtil.getPm();
		Query q = pm.newQuery(BugaJiregeeGame.class, "id == " + gameId);
		return JdoUtil.queryFirst(q, BugaJiregeeGame.class);
	}

	/**
	 * Returns the largest existing game id. Returns 0 if no game has ever been
	 * created.
	 */
	private long queryForLargestGameId() {
		PersistenceManager pm = JdoUtil.getPm();
		Query q = pm.newQuery(BugaJiregeeGame.class);
		q.setOrdering("id descending");
		Collection results = (Collection) q.execute();
		if (results.size() == 0) {
			return 0;
		}
		return ((BugaJiregeeGame) results.iterator().next()).getId();
	}

	/**
	 * Reserve a spot in a game for a player. May create a new game in the
	 * process.
	 * 
	 * @return the id of the game which has a reserved spot
	 */
	private long reserveGame(int waitTime) {
		mLogger.info("GameServiceImpl#reserveGame1");
		MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
		Long gameId = getCachedGameId(cache);

		if (gameId != null) {
			mLogger.info("GameServiceImpl#reserveGame exist");
			if (cache.increment(gameId + "-" + CURRENT_GAME_NUM_PLAYERS, 1, 0L) <= MAX_PLAYERS) {
				// There's room for another player, but the game may have
				// already
				// started.
				PersistenceManager pm = JdoUtil.getPm();
				Transaction tx = pm.currentTransaction();
				try {
					tx.begin();
					BugaJiregeeGame game = getGameById(gameId);
					if (game.getState() == BugaJiregeeGame.State.NEW) {
						return gameId;
					}
					tx.commit();
				} finally {
					if (tx.isActive()) {
						tx.rollback();
					}
				}
			}
		}

		gameId = queryForLargestGameId() + 1;
		mLogger.info("GameServiceImpl#reserveGame: gameid is " + gameId);

		if (tryCreateGame(gameId)) {
			cache.put(CURRENT_GAME_ID, gameId);
			cache.put(gameId + "-" + CURRENT_GAME_NUM_PLAYERS, 1);
			StartGame startGame = new StartGame(gameId);
			TaskOptions to = getTaskOptions().countdownMillis(
					MAX_WAIT_TIME_MILLIS);
			defer(startGame, to);
			return gameId;
		}

		// We couldn't create it, so someone else must have.
		// Search for a newer game id in cache than the
		// one we just tried to create.
		gameId = findNewerGameInCache(gameId);

		// If our cache disappeared, start all over again.
		return gameId == null ? reserveGame(waitTime) : gameId;
	}

	private Long getCachedGameId(MemcacheService memcache) {
		return (Long) memcache.get(CURRENT_GAME_ID);
	}

	private Long findNewerGameInCache(Long gameId) {
		MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
		Long newGameId = getCachedGameId(cache);
		while (newGameId <= gameId) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				//
			}
			newGameId = getCachedGameId(cache);
			if (newGameId == null) {
				return null;
			}
		}
		return newGameId;
	}

	private boolean initGame(BugaJiregeeGame game) {
		PersistenceManager pm = JdoUtil.getPm();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			game.start();
			pm.makePersistent(game);
			tx.commit();
		} catch (ConcurrentModificationException ex) {
			// Someone else tried to modify the game at the same time.
			// Just let the client retry.
			return false;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
		return true;
	}
}
