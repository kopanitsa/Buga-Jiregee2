package com.jirge.shared.message;

import com.jirge.shared.UpdateBoardInfo;
import java.util.List;

@SuppressWarnings("serial")
public class UpdateBoardMessage extends Message {

	private List<UpdateBoardInfo> updateInfo;

	// For GWT RPC
	private UpdateBoardMessage() {
		super(Type.UPDATE_BOARD);
	}

	public UpdateBoardMessage(List<UpdateBoardInfo> updateInfo) {
		super(Type.UPDATE_BOARD);

		this.updateInfo = updateInfo;
	}

	public List<UpdateBoardInfo> getUpdateInfo() {
		return updateInfo;
	}
}
