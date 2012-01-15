package com.jirge.shared;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class LoginResults implements Serializable {
  private String channelId;
  private Date estimatedStartTime;
  private Long gameId;

  // For GWT RPC
  @SuppressWarnings("unused")
  private LoginResults() {
  }

  public LoginResults(Long gameId, String channelId, Date estimatedStartTime) {
    this.gameId = gameId;
    this.channelId = channelId;
    this.estimatedStartTime = estimatedStartTime;
  }

  public Long getGameId() {
    return gameId;
  }

  public String getChannelId() {
    return channelId;
  }

  public Date getEstimatedStartTime() {
    return estimatedStartTime;
  }
}
