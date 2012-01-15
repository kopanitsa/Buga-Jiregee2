package com.jirge.server;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;
import com.jirge.shared.message.Message;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wraps the ChannelService up in our application-specific
 * push-messaging infrastructure.
 *
 * @author Toby Reyelts
 */
public class PushServer {

  private static final Logger logger = Logger.getLogger(PushServer.class.getName());
  private static final Method dummyMethod = getDummyMethod();

  private static SerializationPolicy serializationPolicy = createPushSerializationPolicy();

  public static void sendMessage(List<Player> players, Message msg) {
    List<String> playerKeys = new ArrayList<String>();

    for (Player player : players) {
      playerKeys.add(player.getKey());
    }

    sendMessageByKey(playerKeys, msg);
  }

  /**
   * Sends a message to all specified players.
   * @param playerKeys The players to send the message to.
   * @param msg The message to be sent.
   */
  private static void sendMessageByKey(List<String> playerKeys, Message msg) {
    String encodedMessage = encodeMessage(msg);
    for (String player : playerKeys) {
      String key = getAppKeyForPlayer(player);

      try {
        getChannelService().sendMessage(new ChannelMessage(key, encodedMessage));
      } catch (Exception e) {
        // TODO A bug in the dev_appserver causes an exception to be thrown when
        // no users are connected yet.
        // Rethink this when that bug is taken care of.
        logger.log(Level.SEVERE, "Failed to push the message " + msg + " to client " + key, e);
      }
    }
  }

  /**
   * Sends a message to one specific player.
   * @param player The player to send the message to.
   * @param msg The message to be sent.
   */
  public static void sendMessage(Player player, Message msg) {
    sendMessageByKey(Arrays.asList(player.getKey()), msg);
  }

  /**
   * Create a channel for a player. Returns the channel id that
   * the client must use to connect for receiving push messages.
   *
   * @param player
   * @return the client channel id
   */
  public static String createChannel(Player player) {
    String channelId = getChannelService().createChannel(player.getKey());
    logger.info("Returning new channel " + channelId + " for player " + player);
    return channelId;
  }

  /**
   * Creates a new SerializationPolicy for push RPC.
   */
  private static SerializationPolicy createPushSerializationPolicy() {
    // We're reading all of the SerializationPolicy files in the app
    // and merging them together. This approach seems a bit crappy,
    // but less crappy than the other alternatives.

    File[] files = new File("BugaJiregee2").listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith(".gwt.rpc");
      }
    });

    List<SerializationPolicy> policies = new ArrayList<SerializationPolicy>();

    for (File f : files) {
      try {
        BufferedInputStream input = new BufferedInputStream(
            new FileInputStream(f));
        policies.add(SerializationPolicyLoader.loadFromStream(input, null));
      } catch (Exception e) {
        throw new RuntimeException(
            "Unable to load a policy file: " + f.getAbsolutePath());
      }
    }

    return new MergedSerializationPolicy(policies);
  }

  private static String getAppKeyForPlayer(String player) {
    return player;
  }

  private static String encodeMessage(Message msg) {
    try {
      return RPC.encodeResponseForSuccess(dummyMethod, msg, serializationPolicy);
    } catch (SerializationException e) {
      throw new RuntimeException("Unable to encode a message for push.\n" + msg, e);
    }
  }

  /**
   * This method exists to make GWT RPC happy.
   * <p>
   * {@link RPC#encodeResponseForSuccess(java.lang.reflect.Method, Object)}
   * insists that we pass it a Method that has a return type equal to the
   * object we're encoding. What we really want to use is
   * {@link RPC#encodeResponse(Class, Object, boolean, int, com.google.gwt.user.server.rpc.SerializationPolicy)},
   * but it is unfortunately private.
   */
  private Message dummyMethod() {
    throw new UnsupportedOperationException("This should never be called.");
  }

  private static Method getDummyMethod() {
    try {
      return PushServer.class.getDeclaredMethod("dummyMethod");
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("Unable to find the dummy RPC method.");
    }
  }

  private static ChannelService getChannelService() {
    // NB: This is really cheap, but if it became expensive, we could stuff
    // it in a thread local.
    return ChannelServiceFactory.getChannelService();
  }

  private static class MergedSerializationPolicy extends SerializationPolicy {
    List<SerializationPolicy> policies;

    MergedSerializationPolicy(List<SerializationPolicy> policies) {
      this.policies = policies;
    }

    @Override
    public boolean shouldDeserializeFields(Class<?> clazz) {
      for (SerializationPolicy p : policies) {
        if (p.shouldDeserializeFields(clazz)) {
          return true;
        }
      }
      return false;
    }

    @Override
    public boolean shouldSerializeFields(Class<?> clazz) {
      for (SerializationPolicy p : policies) {
        if (p.shouldSerializeFields(clazz)) {
          return true;
        }
      }
      return false;
    }

    @Override
    public void validateDeserialize(Class<?> clazz)
        throws SerializationException {
      SerializationException se = null;
      for (SerializationPolicy p : policies) {
        try {
          p.validateDeserialize(clazz);
          return;
        } catch (SerializationException e) {
          se = e;
        }
      }
      throw se;
    }

    @Override
    public void validateSerialize(Class<?> clazz) throws SerializationException {
      SerializationException se = null;
      for (SerializationPolicy p : policies) {
        try {
          p.validateSerialize(clazz);
          return;
        } catch (SerializationException e) {
          se = e;
        }
      }
      throw se;
    }
  }

}
