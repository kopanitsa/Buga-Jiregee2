package com.jirge.server;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;


@PersistenceCapable(identityType= IdentityType.APPLICATION)
public class BugaJiregeeBoard implements Serializable {

	/**
	 * 0    : Out of board,
	 * 1-35 : On the board,
	 * 36   : Stocks (for Dog pieces)
	 */
	private static final int NUM_OF_POINTS = (6 + 5 * 5 + 4) + 2;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@NotPersistent
	private BugaJiregeePoint points[];

	public BugaJiregeeBoard() {
		initPoints();
		initPaths();
	}

	public BugaJiregeePoint getPoint(int index) {
		if (index >= 0 && index < NUM_OF_POINTS) {
			return this.points[index];
		} else {
			return null;
		}
	}

	//

	private void initPoints() {
		this.points = new BugaJiregeePoint[NUM_OF_POINTS];
		for (int i = 0; i < NUM_OF_POINTS; i++) {
			this.points[i] = new BugaJiregeePoint(i);
		}
	}

	private void initPaths() {
		BugaJiregeePoint p[] = this.points;

		/* p[1]-p[6] */
		/*              UP,    DOWN,  LEFT, RIGHT,   U_L,   U_R,   D_L,   D_R */
		p[ 1].setPaths( null,  null,  null, p[ 2],  null,  null,  null, p[ 4]);
		p[ 2].setPaths( null, p[ 5], p[ 1], p[ 3],  null,  null,  null,  null);
		p[ 3].setPaths( null,  null, p[ 2],  null,  null,  null, p[ 6],  null);
		p[ 4].setPaths( null,  null,  null, p[ 5], p[ 1],  null,  null, p[ 9]);
		p[ 5].setPaths(p[ 2], p[ 9], p[ 4], p[ 6],  null,  null,  null,  null);
		p[ 6].setPaths( null,  null, p[ 5],  null,  null, p[ 3], p[ 9],  null);

		/* p[7]-p[31] */
		for (int i = 7; i < 32; i++) {
			if (i > 11) p[i].setPath(BugaJiregeePoint.D_UP, p[i - 5]);
			if (i < 27) p[i].setPath(BugaJiregeePoint.D_DOWN, p[i + 5]);
			if (i % 5 != 2) p[i].setPath(BugaJiregeePoint.D_LEFT, p[i - 1]);
			if (i % 5 != 1) p[i].setPath(BugaJiregeePoint.D_RIGHT, p[i + 1]);
			if (i % 2 == 1) {
				if (i > 11) {
					if (i % 5 != 2) p[i].setPath(BugaJiregeePoint.D_UP_LEFT, p[i - 6]);
					if (i % 5 != 1) p[i].setPath(BugaJiregeePoint.D_UP_RIGHT, p[i - 4]);
				}
				if (i < 27) {
					if (i % 5 != 2) p[i].setPath(BugaJiregeePoint.D_DOWN_LEFT, p[i + 4]);
					if (i % 5 != 1) p[i].setPath(BugaJiregeePoint.D_DOWN_RIGHT, p[i + 6]);
				}
			}
		}
		p[ 9].setPath(BugaJiregeePoint.D_UP_LEFT,    p[ 4]);
		p[ 9].setPath(BugaJiregeePoint.D_UP,         p[ 5]);
		p[ 9].setPath(BugaJiregeePoint.D_UP_RIGHT,   p[ 6]);
		p[29].setPath(BugaJiregeePoint.D_DOWN_LEFT,  p[32]);
		p[29].setPath(BugaJiregeePoint.D_DOWN,       p[33]);
		p[29].setPath(BugaJiregeePoint.D_DOWN_RIGHT, p[34]);

		/* p[32]-p[35] */
		/*              UP,    DOWN,  LEFT, RIGHT,   U_L,   U_R,   D_L,   D_R */
		p[32].setPaths( null,  null,  null, p[33],  null, p[29],  null, p[35]);
		p[33].setPaths(p[29], p[35], p[32], p[34],  null,  null,  null,  null);
		p[34].setPaths( null,  null, p[33],  null, p[29],  null, p[35],  null);
		p[35].setPaths(p[33],  null,  null,  null, p[32], p[34],  null,  null);
	}
}
