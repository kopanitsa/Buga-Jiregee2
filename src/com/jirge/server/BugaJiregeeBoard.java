package com.jirge.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class BugaJiregeeBoard implements Serializable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String key;

	@Persistent private List<BugaJiregeePoint> points;

	public BugaJiregeeBoard() {
		this.points = new ArrayList<BugaJiregeePoint>(5 * 9);
		//this.points[0][0] = new BugaJiregeePoint(0, 0, 0, 0, 0, 2, 0, 0, 1);
		//this.points[0][1] = null;
		//this.points[0][2] = new BugaJiregeePoint(0, 0, 0, 2, 0, 2, 0, 1, 0);
	}

	public BugaJiregeePoint getPoint(int x, int y) {
		if (x >= 0 && x < 5 && y >= 0 && y < 9) {
			return this.points.get(x + y * 5);
		} else {
			return null;
		}
	}

	public BugaJiregeePiece getPiece(int x, int y) {
		BugaJiregeePoint point = getPoint(x, y);
		if (point != null) {
			return point.getPiece();
		} else {
			return null;
		}
	}

	public List<BugaJiregeePiece> getMovablePieces() {
		List<BugaJiregeePiece> movablePieces = new ArrayList<BugaJiregeePiece>();
		// TODO implement
		return movablePieces;
	}

}
