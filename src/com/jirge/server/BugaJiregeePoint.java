package com.jirge.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BugaJiregeePoint implements Serializable {

	/**
	 * Constants for the path directions
	 */
	protected static final int D_UP = 0;
	protected static final int D_DOWN = 1;
	protected static final int D_LEFT = 2;
	protected static final int D_RIGHT = 3;
	protected static final int D_UP_LEFT = 4;
	protected static final int D_UP_RIGHT = 5;
	protected static final int D_DOWN_LEFT = 6;
	protected static final int D_DOWN_RIGHT = 7;
	private static final int NUM_OF_DIRECTIONS = 8;

	private int index;
	private Map<Integer, BugaJiregeePoint> paths;
	private BugaJiregeePiece piece;

	public BugaJiregeePoint(int index) {
		this.index = index;
		this.piece = null;
		this.paths = new HashMap<Integer, BugaJiregeePoint>();
	}

	public BugaJiregeePiece getPiece() {
		return piece;
	}

	public void setPiece(BugaJiregeePiece piece) {
		this.piece = piece;
	}

	public void setPaths(BugaJiregeePoint upPoint, BugaJiregeePoint downPoint, BugaJiregeePoint leftPoint,
			BugaJiregeePoint rightPoint, BugaJiregeePoint upleftPoint, BugaJiregeePoint uprightPoint,
			BugaJiregeePoint downleftPoint, BugaJiregeePoint downrightPoint) {
		setPath(D_UP,         upPoint       );
		setPath(D_DOWN,       downPoint     );
		setPath(D_LEFT,       leftPoint     );
		setPath(D_RIGHT,      rightPoint    );
		setPath(D_UP_LEFT,    upleftPoint   );
		setPath(D_UP_RIGHT,   uprightPoint  );
		setPath(D_DOWN_LEFT,  downleftPoint );
		setPath(D_DOWN_RIGHT, downrightPoint);
	}

	public void setPath(int direction, BugaJiregeePoint point) {
		if (direction >= 0 && direction < NUM_OF_DIRECTIONS) {
			this.paths.put(new Integer(direction), point);
		}
	}

	public BugaJiregeePoint getPath(int direction) {
		return this.paths.get(new Integer(direction));
	}

	public Integer getPathDirection(BugaJiregeePoint point) {
		// FIXME: use cross-referecnce (if any)
		Set<Integer> keySet = this.paths.keySet();
		Iterator<Integer> itr = keySet.iterator();
		while (itr.hasNext()) {
			Integer directionInt = itr.next();
			if (this.paths.get(directionInt) == point) {
				return directionInt;
			}
		}
		return null;
	}

	public Integer getNextNextDirection(BugaJiregeePoint toPoint) {
		Set<Integer> keySet = this.paths.keySet();
		Iterator<Integer> itr = keySet.iterator();
		while (itr.hasNext()) {
			Integer directionInt = itr.next();
			int direction = directionInt.intValue();
			BugaJiregeePoint nextNextPoint = this.getPath(direction).getPath(direction);
			if (nextNextPoint != null && nextNextPoint == toPoint) {
				return directionInt;
			}
		}
		return null;
	}

	public boolean isNextTo(BugaJiregeePoint point) {
		return this.paths.containsValue(point);
	}

	public Map<Integer, BugaJiregeePoint> getPaths() {
		return this.paths;
	}

	public int getIndex() {
		return this.index;
	}
}
