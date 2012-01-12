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
public class Player implements Serializable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String key;

	@Persistent private List<BugaJiregeePiece> pieces;

	public Player(int type) {
		switch (type) {
		case BugaJiregeePiece.TYPE_DEER:
			this.pieces = new ArrayList<BugaJiregeePiece>(2);
			break;
		case BugaJiregeePiece.TYPE_DOG:
			this.pieces = new ArrayList<BugaJiregeePiece>(24);
			break;
		}
	}

}
