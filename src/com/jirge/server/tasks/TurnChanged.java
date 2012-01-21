package com.jirge.server.tasks;

import java.io.IOException;

import javax.servlet.ServletException;

import com.jirge.server.Player;
import com.jirge.server.PushServer;
import com.jirge.shared.message.TurnChangedMessage;
import com.newatlanta.appengine.taskqueue.Deferred;

@SuppressWarnings("serial")
public class TurnChanged implements Deferred.Deferrable {
	private final int[] movablePieces;
	private final Player player;

	public TurnChanged(int[] movablePieces, Player player) {
		this.movablePieces = movablePieces;
		this.player = player;
	}

	public void doTask() throws ServletException, IOException {
		PushServer.sendMessage(player, new TurnChangedMessage(movablePieces));
	}
}
