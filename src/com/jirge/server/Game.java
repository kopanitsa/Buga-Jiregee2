package com.jirge.server;

import javax.jdo.annotations.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PersistenceCapable(identityType= IdentityType.APPLICATION)
public class Game implements Serializable {

  public enum State {
    /**
     * Only one Game in the system can be in the NEW state
     * at any given time.
     */
    NEW,
    IN_PROGRESS,
    COMPLETE
  }

  @PrimaryKey
  @Persistent
  private Long id;

  @Persistent
  private State state;

  @Persistent
  private List<Player> players;

  @Persistent
  private Date timeCreated;

  /**
   * Creates a new Game in the NEW state.
   */
  public Game(Long id) {
    this.id = id;
    state = State.NEW;
    timeCreated = new Date(System.currentTimeMillis());
    players = new ArrayList<Player>();
  }

  public Long getId() {
    return id;
  }
  
  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }
  
  public List<Player> getPlayers() {
    return players;
  }

  public Date getTimeCreated() {
    return timeCreated;
  }
}
