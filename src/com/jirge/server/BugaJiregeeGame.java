package com.jirge.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Extension;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class BugaJiregeeGame implements Serializable {

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
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

	@Persistent
	private State state;

	@Persistent
	private List<Player> players;

	//@Persistent private List<Move> moves;
	
	@Persistent
	private BugaJiregeeBoard board;

	@Persistent
	private Date timeCreated;

	public BugaJiregeeGame(Long id) {
		this.id = id;
	    this.state = State.NEW;
	    this.timeCreated = new Date(System.currentTimeMillis());
		this.players = new ArrayList<Player>(2);
		this.board = new BugaJiregeeBoard();
	}

	// Setters & Getters

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

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public BugaJiregeeBoard getBoard() {
		return board;
	}

	public void setBoard(BugaJiregeeBoard board) {
		this.board = board;
	}

	public Date getTimeCreated() {
	    return timeCreated;
	}
	
}
