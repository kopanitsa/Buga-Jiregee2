package com.jirge.server;

import java.io.Serializable;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.jirge.shared.PieceType;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class BugaJiregeePiece implements Serializable {

	public static final int TYPE_DEER = PieceType.DEER;
	public static final int TYPE_DOG = PieceType.DOG;

	public static final int NUM_OF_DEERS = 2;
	public static final int NUM_OF_DOGS = 25;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private int type;

//	@Persistent
//	private BugaJiregeePoint point;

	@Persistent
	private Integer pointIndex;

	@Persistent
	private boolean isStocked;


	public BugaJiregeePiece(int type) {
		this.type = type;
		this.pointIndex = null;
	}

	//

//	public List<BugaJiregeePoint> getAccessiblePoints() {
//		List<BugaJiregeePoint> accessiblePoints = new ArrayList<BugaJiregeePoint>();
//		if (this.point != null) {
//			Set<Integer> keySet = this.point.getPaths().keySet();
//			Iterator<Integer> itr = keySet.iterator();
//			while (itr.hasNext()) {
//				Integer directionInt = itr.next();
//				int direction = directionInt.intValue();
//				BugaJiregeePoint nextPoint = this.point.getPaths().get(directionInt);
//				if (nextPoint.getPiece() == null) {
//					accessiblePoints.add(nextPoint);
//				} else if (this.type == TYPE_DEER) {
//					BugaJiregeePoint nextNextPoint = nextPoint.getPath(direction);
//					if (nextNextPoint != null && nextNextPoint.getPiece() == null) {
//						accessiblePoints.add(nextNextPoint);
//					}
//				}
//			}
//		}
//		return accessiblePoints;
//	}

//	public BugaJiregeePiece moveTo(BugaJiregeePoint toPoint) {
//		if (this.point != null) {
//			if (this.point.isNextTo(toPoint) && toPoint.getPiece() == null) {
//				setPoint(toPoint);
//			} else if (this.type == TYPE_DEER) {
//				Integer directionInt = this.point.getNextNextDirection(toPoint);
//				if (directionInt != null) {
//					BugaJiregeePiece nextDogPiece = this.point.getPath(directionInt.intValue()).getPiece();
//					if (nextDogPiece != null) {
//						// Deer jumps over dog.
//						setPoint(toPoint);
//						return nextDogPiece;
//					}
//				}
//			}
//		}
//		return null;
//	}

	//

//	public BugaJiregeePoint getPoint() {
//		return point;
//	}
//
//	public void setPoint(BugaJiregeePoint point) {
//		this.point = point;
//		if (this.point != null) {
//			this.point.setPiece(this);
//		}
//	}

	public Integer getPointIndex() {
		return this.pointIndex;
	}

	public void setPointIndex(Integer pointIndex) {
		this.pointIndex = pointIndex;
	}

	public int getType() {
		return this.type;
	}

	public boolean isStocked() {
		return isStocked;
	}

	public void setStocked(boolean isStocked) {
		this.isStocked = isStocked;
	}

}
