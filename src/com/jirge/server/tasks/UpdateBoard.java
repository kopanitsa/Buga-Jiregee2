package com.jirge.server.tasks;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.jirge.server.BugaJiregeeGame;
import com.jirge.server.PushServer;
import com.jirge.shared.UpdateBoardInfo;
import com.jirge.shared.message.UpdateBoardMessage;
import com.newatlanta.appengine.taskqueue.Deferred;

@SuppressWarnings("serial")
public class UpdateBoard implements Deferred.Deferrable {
	private final List<UpdateBoardInfo> info;
	private final BugaJiregeeGame game;

	public UpdateBoard(List<UpdateBoardInfo> info, BugaJiregeeGame game) {
		this.info = info;
		this.game = game;
	}

	public void doTask() throws ServletException, IOException {
		PushServer.sendMessage(game.getPlayers(), new UpdateBoardMessage(info));
	}
}
