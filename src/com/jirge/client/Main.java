package com.jirge.client;

import java.util.logging.Logger;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.appengine.channel.client.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.ui.RootPanel;
import com.jirge.shared.LoginResults;
import com.jirge.shared.message.Message;

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

    ChannelFactory.createChannel(results.getChannelId(), new ChannelCreatedCallback() {
        @Override
        public void onChannelCreated(Channel channel) {
          channel.open(new SocketListener() {
            @Override
            public void onOpen() {
              Window.alert("Channel opened!");
            }
            @Override
            public void onMessage(String encodedData) {
              Window.alert("Received: " + encodedData);
              try {
                SerializationStreamReader reader = mPushServiceStreamFactory.createStreamReader(encodedData);
                Message message = (Message) reader.readObject();
                board.receiveMsg(message);
              } catch (SerializationException e) {
                throw new RuntimeException("Unable to deserialize " + encodedData, e);
              }
            }
            @Override
            public void onError(SocketError error) {
              Window.alert("Error: " + error.getDescription());
            }
            @Override
            public void onClose() {
              Window.alert("Channel closed!");
            }
          });
        }
      });

    mRoot.remove(mFindGamePanel);
    mRoot.add(board);
  }

}
