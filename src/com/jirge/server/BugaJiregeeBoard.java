package com.jirge.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Persistent private BugaJiregeePoint points[];

	public BugaJiregeeBoard() {
		initPoints();
		initPaths();
	}

	public BugaJiregeePoint getPoint(int x, int y) {
		if (x >= 0 && x < 5 && y >= 0 && y < 9) {
			return this.points[x + y * 5];
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

	private void initPoints() {
		this.points = new BugaJiregeePoint[5 * 9];
		this.points[ 0] = new BugaJiregeePoint();
		this.points[ 1] = null;
		this.points[ 2] = new BugaJiregeePoint();
		this.points[ 3] = null;
		this.points[ 4] = new BugaJiregeePoint();
		this.points[ 5] = null;
		this.points[ 6] = new BugaJiregeePoint();
		this.points[ 7] = new BugaJiregeePoint();
		this.points[ 8] = new BugaJiregeePoint();
		this.points[ 9] = null;
		for (int i = 10; i < 10 + 25; i++) {
			this.points[i] = new BugaJiregeePoint();
		}
		this.points[35] = null;
		this.points[36] = new BugaJiregeePoint();
		this.points[37] = new BugaJiregeePoint();
		this.points[38] = new BugaJiregeePoint();
		this.points[39] = null;
		this.points[40] = null;
		this.points[41] = null;
		this.points[42] = new BugaJiregeePoint();
		this.points[43] = null;
		this.points[44] = null;
	}

	private void initPaths() {
		BugaJiregeePoint p[] = this.points;
		p[ 0].setPath( null,  null,  null, p[ 2],  null,  null,  null, p[ 6]);
		p[ 2].setPath( null, p[ 7], p[ 0], p[ 4],  null,  null,  null,  null);
		p[ 4].setPath( null,  null, p[ 2],  null,  null,  null, p[ 8],  null);
		p[ 6].setPath( null,  null,  null, p[ 7], p[ 0],  null,  null, p[12]);
		p[ 7].setPath(p[ 2], p[12], p[ 6], p[ 8],  null,  null,  null,  null);
		p[ 8].setPath( null,  null, p[ 7],  null,  null, p[ 4], p[12],  null);
		p[10].setPath( null, p[15],  null, p[11],  null,  null,  null, p[16]);
		p[11].setPath( null, p[16], p[10], p[12],  null,  null,  null,  null);
		p[12].setPath( p[7], p[17], p[11], p[13], p[ 6], p[ 8], p[16], p[18]);
		p[13].setPath( null, p[18], p[12], p[14],  null,  null,  null,  null);
		p[14].setPath( null, p[19], p[13],  null,  null,  null, p[18],  null);
		// TODO implement
	}
}
