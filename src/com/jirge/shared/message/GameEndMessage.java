package com.jirge.shared.message;

@SuppressWarnings("serial")
public class GameEndMessage extends Message {
  public GameEndMessage() {
    super(Type.GAME_END);
  }
}
