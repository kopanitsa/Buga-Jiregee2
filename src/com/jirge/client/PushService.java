package com.jirge.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;
import com.jirge.shared.message.Message;

/**
 * A GWT RPC service interface for RPC calls which are pushed to
 * this game client.
 * <p>
 * This interface is odd in that the client doesn't actually make calls
 * through this interface to the server. Instead the server uses server-side
 * push to send GWT RPC encoded data to the client via an alternate
 * transport. The definition of this interface helps to ensure that all
 * the correct de-serialization code is generated for the client. A call to
 * GWT.create on this service must be made to ensure the de-serialization
 * code is actually generated.
 *
 * @author Toby Reyelts
 */
@RemoteServiceRelativePath("push_service")
public interface PushService extends RemoteService {

  /**
   * Utility/Convenience class. Use PushService.App.getInstance() to access
   * static instance of PushServiceAsync
   */
  public static class App {
    private static final PushServiceAsync ourInstance = (PushServiceAsync) GWT
        .create(PushService.class);

    public static PushServiceAsync getInstance() {
      return ourInstance;
    }
  }
  /**
   * A dummy method ensuring that Message and all its subclasses
   * are client serializable.
   */
  Message receiveMessage();
}
