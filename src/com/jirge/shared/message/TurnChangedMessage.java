package com.jirge.shared.message;

@SuppressWarnings("serial")
public class TurnChangedMessage extends Message {

	private int[] movablePieces;

	// For GWT RPC
	private TurnChangedMessage() {
		super(Type.TURN_CHANGED);
	}

	public TurnChangedMessage(int[] movablePieces) {
		super(Type.TURN_CHANGED);

		this.movablePieces = movablePieces;
	}

	public int[] getMovablePieces() {
		return movablePieces;
	}
}
