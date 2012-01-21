package com.jirge.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UpdateBoardInfo implements Serializable {
	public int playerType;
	public int beforePos;
	public int afterPos;

	// For GWT RPC
	@SuppressWarnings("unused")
	private UpdateBoardInfo() {
	}

	public UpdateBoardInfo(int playerType, int beforePos, int afterPos) {
		this.playerType = playerType;
		this.beforePos = beforePos;
		this.afterPos = afterPos;
	}
}
