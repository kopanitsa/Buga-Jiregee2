package com.jirge.shared.message;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Message implements Serializable {

	public enum Type {
		NEW_PLAYER,
		GAME_BEGIN,
		UPDATE_BOARD,
		TURN_CHANGED,
		GAME_END,
	}

	private Type type;

	// For GWT RPC
	@SuppressWarnings("unused")
	private Message() {
	}

	protected Message(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}
}
