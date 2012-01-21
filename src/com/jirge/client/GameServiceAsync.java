package com.jirge.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.jirge.shared.LoginResults;
import com.jirge.shared.message.Message;

public interface GameServiceAsync {

	void login(String name, Long gameId, AsyncCallback<LoginResults> async);

	void confirmLogin(AsyncCallback<List<Message>> async);

	void getAccessiblePoints(int index, AsyncCallback<int[]> callback);
}
