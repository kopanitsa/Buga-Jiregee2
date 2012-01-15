package com.jirge.shared;

import java.io.Serializable;


@SuppressWarnings("serial")
public class PlayerValue implements Serializable {
  private String key;
  private String name;

  // For GWT RPC
  @SuppressWarnings("unused")
  private PlayerValue() {
  }

  public PlayerValue(String key, String name) {
    this.key = key;
    this.name = name;
  }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }
}
