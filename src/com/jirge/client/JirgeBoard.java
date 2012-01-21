package com.jirge.client;

import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.jirge.shared.LoginResults;
import com.jirge.shared.UpdateBoardInfo;
import com.jirge.shared.message.Message;
import com.jirge.shared.message.TurnChangedMessage;
import com.jirge.shared.message.UpdateBoardMessage;

public class JirgeBoard extends VerticalPanel {

	public JirgeBoard(String playerName, LoginResults results) {
		// TODO Auto-generated constructor stub

		// render
		HorizontalPanel firstRow = new HorizontalPanel();
		add(firstRow);
		Label title = new Label("Log-in process has been completed!");
		title.setStyleName("title");
		firstRow.add(title);

		HorizontalPanel secondRow = new HorizontalPanel();
		add(secondRow);
		Label gameId = new Label("Game ID:" + results.getGameId());
		gameId.addStyleDependentName("nameLabel");
		secondRow.add(gameId);

		HorizontalPanel thirdRow = new HorizontalPanel();
		add(thirdRow);
		Label channel = new Label("chennel ID:" + results.getChannelId());
		channel.addStyleDependentName("nameLabel");
		thirdRow.add(channel);

		HorizontalPanel fourthRow = new HorizontalPanel();
		add(fourthRow);
		Label name = new Label("Player name:" + playerName);
		name.addStyleDependentName("nameLabel");
		fourthRow.add(name);
	}

	/**
	 * Receives messages pushed from the server.
	 */
	public void receiveMsg(Message msg) {
		switch (msg.getType()) {
		case GAME_BEGIN:
			break;

		case UPDATE_BOARD:
			updateBoard((UpdateBoardMessage) msg);
			break;

		case TURN_CHANGED:
			turnChanged((TurnChangedMessage) msg);
			break;
			
		case NEW_PLAYER:
			break;

		case STEP_OCCURRED:
			break;

		case GAME_END:
			break;

		default:
			Window.alert("Unknown game type: " + msg.getType());
		}
	}

	private void updateBoard(UpdateBoardMessage msg) {
		List<UpdateBoardInfo> updateInfo = msg.getUpdateInfo();
		for (UpdateBoardInfo info : updateInfo) {
			// Show message info for debug
			Window.alert("player type : " + info.playerType + ", before : "
					+ info.beforePos + ", after : " + info.afterPos);
		}
	}

	private void turnChanged(TurnChangedMessage msg) {
		int[] movablePieces = msg.getMovablePieces();
		Window.alert(movablePieces.toString());
	}
}
