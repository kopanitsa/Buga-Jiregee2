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
public class BugaJiregeePiece implements Serializable {

	public static final int TYPE_DEER = 1;
	public static final int TYPE_DOG = 2;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String key;

	@Persistent private int type;

	private BugaJiregeePoint point;


	public BugaJiregeePiece(int type) {
		this.type = type;
	}


	public BugaJiregeePoint getPoint() {
		return point;
	}

	public void setPoint(BugaJiregeePoint point) {
		this.point = point;
	}

	public List<BugaJiregeePoint> getMovablePoints() {
		List<BugaJiregeePoint> movablePoints = new ArrayList<BugaJiregeePoint>();
		// TODO implement
		return movablePoints;
	}

}
