package com.jirge.server.tasks;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.servlet.ServletException;

import com.jirge.server.BugaJiregeeGame;
import com.jirge.server.PushServer;
import com.jirge.shared.message.GameBeginMessage;
import com.jirge.util.JdoUtil;
import com.newatlanta.appengine.taskqueue.Deferred;

@SuppressWarnings("serial")
public class StartGame implements Deferred.Deferrable {
	private final long gameId;

	public StartGame(long gameId) {
		this.gameId = gameId;
	}

	public void doTask() throws ServletException, IOException {
		PersistenceManager pm = JdoUtil.getPm();
		Transaction tx = pm.currentTransaction();

		BugaJiregeeGame game;
		try {
			tx.begin();
			Query q = pm.newQuery(BugaJiregeeGame.class, "id == " + gameId);
			game = JdoUtil.queryFirst(q, BugaJiregeeGame.class);
			if (game.getState() == BugaJiregeeGame.State.IN_PROGRESS) {
				// Already started, nothing to do.
				return;
			}
			game.setState(BugaJiregeeGame.State.IN_PROGRESS);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}

		PushServer.sendMessage(game.getPlayers(), new GameBeginMessage());
	}
}
