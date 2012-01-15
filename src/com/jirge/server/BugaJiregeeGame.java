package com.jirge.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class BugaJiregeeGame implements Serializable {

	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey private Long id;

	@Persistent private List<Player> players;
	//@Persistent private List<Move> moves;
	@Persistent private BugaJiregeeBoard board;

	public BugaJiregeeGame(Long id) {
		this.id = id;
		this.players = new ArrayList<Player>(2);
		this.board = new BugaJiregeeBoard();
	}

	// Setters & Getters

	public Long getId() {
		return id;
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

}
