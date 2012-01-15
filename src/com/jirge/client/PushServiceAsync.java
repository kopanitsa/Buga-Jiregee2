package com.jirge.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.jirge.shared.message.Message;

/**
 * Async interface for {@link PushService}. This class is not used directly
 * by client code.
 *
 * @author Toby Reyelts
 */
public interface PushServiceAsync {
  void receiveMessage(AsyncCallback<Message> async);
}
