package com.jirge.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.jirge.shared.LoginResults;

public interface GameServiceAsync {

	void login(String name, Long gameId, AsyncCallback<LoginResults> callback);

	void getAccessiblePoints(int index, AsyncCallback<int[]> callback);

	void movePiece(int fromIndex, int toIndex, AsyncCallback<Boolean> callback);
}
