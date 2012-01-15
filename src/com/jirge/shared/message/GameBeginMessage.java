package com.jirge.shared.message;

@SuppressWarnings("serial")
public class GameBeginMessage extends Message {
  // NB: Don't forget to add a private no-arg constructor
  // for GWT RPC if this changes.
  public GameBeginMessage() {
    super(Type.GAME_BEGIN);
  }
}
