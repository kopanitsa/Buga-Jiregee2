package com.jirge.shared.message;

@SuppressWarnings("serial")
public class TurnChangedMessage extends Message {
	private int playerType;
	private int[] movablePieces;

	// For GWT RPC
	private TurnChangedMessage() {
		super(Type.TURN_CHANGED);
	}

	public TurnChangedMessage(int playerType, int[] movablePieces) {
		super(Type.TURN_CHANGED);

		this.playerType = playerType;
		this.movablePieces = movablePieces;
	}

	public int getPlayerType() {
		return playerType;
	}

	public int[] getMovablePieces() {
		return movablePieces;
	}
}
