package com.jirge.server;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.*;
import static com.newatlanta.appengine.taskqueue.Deferred.*;

import java.util.ArrayList;
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
import com.jirge.client.Main;
import com.jirge.server.tasks.SendNewPlayer;
import com.jirge.server.tasks.StartGame;
import com.jirge.shared.LoginResults;
import com.jirge.shared.message.Message;
import com.jirge.shared.message.TmpMessage;
import com.jirge.util.JdoUtil;

public class GameServiceImpl extends RemoteServiceServlet
    implements GameService {

  private static final int MAX_PLAYERS = 2;
  private static final int MAX_WAIT_TIME_MILLIS = 60000;

  private static final String PLAYER = "player";
  private static final String GAME_ID = "game_id";

  public static final String CURRENT_GAME_ID = "current_game_id";
  private static final String CURRENT_GAME_NUM_PLAYERS = "num_players";
  private final Logger mLogger = Logger.getLogger(this.getClass().getName());

  public LoginResults login(final String name, Long gameId) {
      mLogger.info("GameServiceImpl#login name:"+name+" id:"+gameId);
    if (gameId == null) {
        gameId = reserveGame(5); // temporary value
    }
    HttpSession session = getThreadLocalRequest().getSession();
    session.setAttribute(GAME_ID, gameId);

    BugaJiregeeGame game = getGameById(gameId);
    // FIXME need to select deer or dog
    Player player = new Player(BugaJiregeePiece.TYPE_DEER);
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
    }
    finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }

    defer(new SendNewPlayer(player, game), getTaskOptions());

    if (game.getPlayers().size() >= MAX_PLAYERS) {
      defer(new StartGame(gameId), getTaskOptions().countdownMillis(3000));
    }

    Date estimatedStartTime = new Date(game.getTimeCreated().getTime() + 60);

    String channelId = PushServer.createChannel(player);
    return new LoginResults(null, channelId, estimatedStartTime);
  }

  public List<Message> confirmLogin() {
      BugaJiregeeGame game = getGameForSession();
      List<Message> messages = new ArrayList<Message>();
      // TODO
      messages.add(new TmpMessage());
      return messages;
    }
  
  public int[] getAccessiblePoints(int index) {
	  BugaJiregeeGame game = getGameForSession();
	  int[] accessiblePoints = {1, 2};
	  return accessiblePoints; 
  }

  private boolean tryCreateGame(long gameId) {
    PersistenceManager pm = JdoUtil.getPm();
    Transaction tx = pm.currentTransaction();

    try {
      tx.begin();
      BugaJiregeeGame game = new BugaJiregeeGame(gameId);
      pm.makePersistent(game);
      mLogger.info("GameServerImpl:tryCreateGame"+gameId);
      tx.commit();
    } catch (JDOException e) {
      e.printStackTrace();
      // NB(tobyr) Need to figure out really what exception to catch here.
      return false;
    }
    finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }
    
    // ---------DEBUG-----------
    Query qAll = pm.newQuery(BugaJiregeeGame.class);
    Collection cAll = (Collection)qAll.execute();
    mLogger.warning("[Game number]All:"+cAll.size());
    Query q = pm.newQuery(BugaJiregeeGame.class, "id == " + gameId);
    Collection c = (Collection)q.execute();
    mLogger.warning("[Game number]filter:"+c.size());
    long prevGameId = gameId - 1;
    Query qp = pm.newQuery(BugaJiregeeGame.class, "id == " + prevGameId);
    Collection cp = (Collection)qp.execute();
    mLogger.warning("[Game number]filter:"+cp.size());
    // ---------DEBUG-----------
    
    return true;
  }

  private BugaJiregeeGame getGameForSession() {
    Long gameId = (Long)getThreadLocalRequest().getSession().getAttribute(GAME_ID);
    return getGameById(gameId);
  }

  private Player getPlayerForSession() {
    return (Player)getThreadLocalRequest().getSession().getAttribute(PLAYER);
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
   * Returns the largest existing game id.
   * Returns 0 if no game has ever been created.
   */
  private long queryForLargestGameId() {
    PersistenceManager pm = JdoUtil.getPm();
    Query q = pm.newQuery(BugaJiregeeGame.class);
    q.setOrdering("id descending");
    Collection results = (Collection)q.execute();
    if (results.size() == 0) {
      return 0;
    }
    return ((BugaJiregeeGame)results.iterator().next()).getId();
  }

  /**
   * Reserve a spot in a game for a player. May create a new game
   * in the process.
   * @return the id of the game which has a reserved spot
   */
  private long reserveGame(int waitTime) {
    mLogger.info("GameServiceImpl#reserveGame1");
    MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
    Long gameId = getCachedGameId(cache);

    if (gameId != null) {
        mLogger.info("GameServiceImpl#reserveGame exist");
      if (cache.increment(gameId + "-" + CURRENT_GAME_NUM_PLAYERS, 1, 0L) <= MAX_PLAYERS) {
        // There's room for another player, but the game may have already
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
    mLogger.info("GameServiceImpl#reserveGame: gameid is "+gameId);

    if (tryCreateGame(gameId)) {
      cache.put(CURRENT_GAME_ID, gameId);
      cache.put(gameId + "-" + CURRENT_GAME_NUM_PLAYERS, 1);
      StartGame startGame = new StartGame(gameId);
      TaskOptions to = getTaskOptions().countdownMillis(waitTime * 1000);
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
    return (Long)memcache.get(CURRENT_GAME_ID);
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
}
