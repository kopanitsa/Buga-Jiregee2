package com.jirge.server;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class BugaJiregeePoint implements Serializable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String key;

	private BugaJiregeePiece piece;

	public BugaJiregeePoint() {
		this.piece = null;
	}

	public BugaJiregeePiece getPiece() {
		return piece;
	}

	public void setPiece(BugaJiregeePiece piece) {
		this.piece = piece;
	}

	public void setPath(BugaJiregeePoint upPoint, BugaJiregeePoint downPoint, BugaJiregeePoint leftPoint,
			BugaJiregeePoint rightPoint, BugaJiregeePoint upleftPoint, BugaJiregeePoint uprightPoint,
			BugaJiregeePoint downleftPoint, BugaJiregeePoint downrightPoint) {
		// TODO Auto-generated method stub		
	}

}
