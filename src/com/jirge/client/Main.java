package com.jirge.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.ui.RootPanel;
import com.jirge.client.channel.Channel;
import com.jirge.client.channel.ChannelFactory;
import com.jirge.client.channel.SocketListener;
import com.jirge.shared.LoginResults;
import com.jirge.shared.message.Message;

import java.util.List;
import java.util.logging.Logger;

public class Main implements EntryPoint {

  FindGamePanel mFindGamePanel;
  RootPanel mRoot;
  SerializationStreamFactory mPushServiceStreamFactory;
  GameServiceAsync mGameService;
  private static final Logger mLogger = Logger.getLogger(Main.class.getName());

  public void onModuleLoad() {
    mLogger.info("Main#onModuleLoad");
    mPushServiceStreamFactory =(SerializationStreamFactory) PushService.App.getInstance();
    mGameService = GameService.App.getInstance();

    mRoot = RootPanel.get();
    mRoot.setStyleName("root");
    mFindGamePanel = new FindGamePanel(this);
    mRoot.add(mFindGamePanel);
  }

  /**
   * Continues the game workflow after login is complete.
   * @param playerName The name of the player.
   * @param results The results of the login.
   */
  public void loginComplete(final String playerName, LoginResults results) {
    mLogger.info("Main#loginComplete");
    final JirgeBoard board = new JirgeBoard(playerName, results);

    Channel channel = ChannelFactory.createChannel(results.getChannelId());
    channel.open(new SocketListener() {
      public void onOpen() {
        mGameService.confirmLogin(new AsyncCallback<List<Message>>() {
          public void onFailure(Throwable caught) {
            Window.alert(caught.getMessage());
          }

          public void onSuccess(List<Message> messages) {
            for (Message msg : messages) {
                board.receiveMsg(msg);
            }
          }
        });
      }

      public void onMessage(String encodedData) {
        try {
          SerializationStreamReader reader = mPushServiceStreamFactory.createStreamReader(encodedData);
          Message message = (Message) reader.readObject();
          board.receiveMsg(message);
        } catch (SerializationException e) {
          throw new RuntimeException("Unable to deserialize " + encodedData, e);
        }
      }
    });

    mRoot.remove(mFindGamePanel);
    mRoot.add(board);
  }

}
