package com.jirge.shared.message;

import com.jirge.shared.PlayerValue;


/**
 * TODO(tobyr): Doc me
 *
 * @author Toby Reyelts
 */
@SuppressWarnings("serial")
public class NewPlayerMessage extends Message {

  private PlayerValue player;

  // For GWT RPC
  private NewPlayerMessage() {
    super(Type.NEW_PLAYER);
  }

  public NewPlayerMessage(PlayerValue value) {
      super(Type.NEW_PLAYER);
    // TODO Auto-generated constructor stub
  }

public PlayerValue getPlayer() {
    return player;
  }
}
