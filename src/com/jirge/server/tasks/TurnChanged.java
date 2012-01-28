package com.jirge.server.tasks;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.jirge.server.BugaJiregeeGame;
import com.jirge.server.BugaJiregeePiece;
import com.jirge.server.Player;
import com.jirge.server.PushServer;
import com.jirge.shared.message.TurnChangedMessage;
import com.newatlanta.appengine.taskqueue.Deferred;

@SuppressWarnings("serial")
public class TurnChanged implements Deferred.Deferrable {
	private final BugaJiregeeGame game;

	public TurnChanged(BugaJiregeeGame game) {
		this.game = game;
	}

	public void doTask() throws ServletException, IOException {
		List<BugaJiregeePiece> movablePieces = game.getMovablePieces();
		int[] movableIndexes = new int[movablePieces.size()];
		for (int i = 0; i < movableIndexes.length; i++) {
//			movableIndexes[i] = movablePieces.get(i).getPoint().getIndex();
			movableIndexes[i] = movablePieces.get(i).getPointIndex();
		}
		int playerIndex = game.getCurrentPlayerIndex();
		Player player = game.getPlayers().get(playerIndex);
		
		PushServer.sendMessage(player, new TurnChangedMessage(movableIndexes));
	}
}
