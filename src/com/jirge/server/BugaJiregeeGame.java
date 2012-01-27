package com.jirge.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.jirge.shared.UpdateBoardInfo;

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

	private static final int[] INIT_DEER_POINTS = {9, 29};
	private static final int[] INIT_DOG_POINTS  = {13, 14, 15, 18, 20, 23, 24, 25};		// 8 pieces on board

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

	@Persistent
	private State state;

	@Persistent
	private List<Player> players;

	@Persistent
	private Integer currentPlayerIndex;

	//@Persistent private List<Move> moves;
	
	@Persistent
	private BugaJiregeeBoard board;

	@Persistent
	private List<BugaJiregeePiece> stockedDogPieces;

	@Persistent
	private Date timeCreated;

	@NotPersistent
	private List<UpdateBoardInfo> lastUpdatedBoardInfo;

	public BugaJiregeeGame(Long id) {
		this.id = id;
		this.state = State.NEW;
		this.timeCreated = new Date(System.currentTimeMillis());
		this.players = new ArrayList<Player>();
		this.currentPlayerIndex = 0;
		this.board = new BugaJiregeeBoard();
		lastUpdatedBoardInfo = null;
	}


	//
	// APIs
	//

	public void start() {
		resetPiecesPosition();
		this.currentPlayerIndex = 0;
	}

	public List<BugaJiregeePiece> getMovablePieces() {
		Player currentPlayer = this.players.get(this.currentPlayerIndex);
		List<BugaJiregeePiece> movablePieces = new ArrayList<BugaJiregeePiece>();
		List<BugaJiregeePiece> pieces = currentPlayer.getPieces();
		for (BugaJiregeePiece piece : pieces) {
			List<BugaJiregeePoint> accessiblePoints = getAccessiblePoints(piece);
			if (!accessiblePoints.isEmpty()) {
				movablePieces.add(piece);
			}
		}
		if (currentPlayer.getType() == BugaJiregeePiece.TYPE_DOG) {
			for (BugaJiregeePiece stockedPiece : this.stockedDogPieces) {
				movablePieces.add(stockedPiece);
			}
		}
		return movablePieces;
	}

	public BugaJiregeePiece getPieceByPointIndex(int pointIndex) {
		if (pointIndex >= 0 && pointIndex < 36) {	// TODO : not to use the magic number.
//			BugaJiregeePoint point = this.board.getPoint(pointIndex);
//			if (point != null) {
//				return point.getPiece();
//			}
			for (Player player : this.players) {
				for (BugaJiregeePiece piece : player.getPieces()) {
					if (piece.getPointIndex() == new Integer(pointIndex)) {
						return piece;
					}
				}
			}
		} else if (pointIndex == 36) {	// TODO : not to use the magic number.
			if (!this.stockedDogPieces.isEmpty()) {
				this.stockedDogPieces.get(0);
			}
		}
		return null;
	}

	public List<BugaJiregeePoint> getAccessiblePoints(BugaJiregeePiece piece) {
		if (piece != null) {
			if (this.stockedDogPieces.contains(piece)) {
//				return this.board.getEmptyPoints();
				return getEmptyPoints();
			} else {
//				return piece.getAccessiblePoints();
				List<BugaJiregeePoint> accessiblePoints = new ArrayList<BugaJiregeePoint>();
//				BugaJiregeePoint piecePos = piece.getPoint();
				BugaJiregeePoint piecePos = this.board.getPoint(piece.getPointIndex().intValue());
				if (piecePos != null) {
					Set<Integer> keySet = piecePos.getPaths().keySet();
					Iterator<Integer> itr = keySet.iterator();
					while (itr.hasNext()) {
						Integer directionInt = itr.next();
						int direction = directionInt.intValue();
						BugaJiregeePoint nextPoint = piecePos.getPaths().get(directionInt);
//						if (nextPoint.getPiece() == null) {
						if (getPieceByPointIndex(nextPoint.getIndex()) == null) {
							accessiblePoints.add(nextPoint);
						} else if (piece.getType() == BugaJiregeePiece.TYPE_DEER) {
							BugaJiregeePoint nextNextPoint = nextPoint.getPath(direction);
//							if (nextNextPoint != null && nextNextPoint.getPiece() == null) {
							if (nextNextPoint != null && getPieceByPointIndex(nextNextPoint.getIndex()) == null) {
								accessiblePoints.add(nextNextPoint);
							}
						}
					}
				}
				return accessiblePoints;
			}
		}
		return new ArrayList<BugaJiregeePoint>();	// return empty list
	}

	public boolean movePiece(BugaJiregeePiece piece, BugaJiregeePoint toPoint) {
//		BugaJiregeePoint fromPoint = piece.getPoint();
		BugaJiregeePoint fromPoint = this.board.getPoint(piece.getPointIndex().intValue());

		if (this.players.get(this.currentPlayerIndex).getPieces().contains(piece)) {
			if (this.stockedDogPieces.contains(piece)) {
//				if (this.board.getEmptyPoints().contains(toPoint)) {
				if (getEmptyPoints().contains(toPoint)) {
//					piece.setPoint(toPoint);
					piece.setPointIndex(new Integer(toPoint.getIndex()));
					this.stockedDogPieces.remove(piece);
					currentPlayerIndex = (currentPlayerIndex + 1) % 2;	// 0, 1, 0, 1, ...
					lastUpdatedBoardInfo = new ArrayList<UpdateBoardInfo>();
					lastUpdatedBoardInfo.add(new UpdateBoardInfo(piece.getType(), fromPoint.getIndex(), toPoint.getIndex()));
					return true;
				}
			} else {
//				BugaJiregeePiece removedPiece = piece.moveTo(toPoint);
				BugaJiregeePiece removedPiece = null;
//				BugaJiregeePoint piecePos = piece.getPoint();
				BugaJiregeePoint piecePos = this.board.getPoint(piece.getPointIndex().intValue());
				if (piecePos != null) {
//					if (piecePos.isNextTo(toPoint) && toPoint.getPiece() == null) {
					if (piecePos.isNextTo(toPoint) && getPieceByPointIndex(toPoint.getIndex()) == null) {
//						piece.setPoint(toPoint);
						piece.setPointIndex(new Integer(toPoint.getIndex()));
					} else if (piece.getType() == BugaJiregeePiece.TYPE_DEER) {
						Integer directionInt = piecePos.getNextNextDirection(toPoint);
						if (directionInt != null) {
//							BugaJiregeePiece nextDogPiece = piecePos.getPath(directionInt.intValue()).getPiece();
							BugaJiregeePiece nextDogPiece = getPieceByPointIndex(piecePos.getPath(directionInt.intValue()).getIndex());
							if (nextDogPiece != null) {
								// Deer jumps over dog.
//								piece.setPoint(toPoint);
								piece.setPointIndex(new Integer(toPoint.getIndex()));
//								return nextDogPiece;
								removedPiece = nextDogPiece;
							}
						}
					}
				}
//				return null;
				lastUpdatedBoardInfo = new ArrayList<UpdateBoardInfo>();
				lastUpdatedBoardInfo.add(new UpdateBoardInfo(piece.getType(), fromPoint.getIndex(), toPoint.getIndex()));

				if (removedPiece != null) {
//					lastUpdatedBoardInfo.add(new UpdateBoardInfo(piece.getType(), removedPiece.getPoint().getIndex(), 0));
//					removedPiece.setPoint(this.board.getPoint(0));	// FIXME : change the managing method.
					lastUpdatedBoardInfo.add(new UpdateBoardInfo(piece.getType(), removedPiece.getPointIndex().intValue(), 0));
					removedPiece.setPointIndex(new Integer(0));	// FIXME : change the managing method.
				}
				currentPlayerIndex = (currentPlayerIndex + 1) % 2;	// 0, 1, 0, 1, ...
				return true;
			}
		}
		// TODO implement to throw an error exception.
		return false;
	}

	public List<UpdateBoardInfo> getLastUpdateBoardInfo() {
		return lastUpdatedBoardInfo;
	}

	//

	private void resetPiecesPosition() {
		stockedDogPieces = new ArrayList<BugaJiregeePiece>();
		lastUpdatedBoardInfo = new ArrayList<UpdateBoardInfo>();

		for (Player player : this.players) {
			for (int i = 0; i < player.getPieces().size(); i++) {
				BugaJiregeePiece piece = player.getPieces().get(i);
				switch (piece.getType()) {
				case BugaJiregeePiece.TYPE_DEER:
//					piece.setPoint(this.board.getPoint(INIT_DEER_POINTS[i]));
//					lastUpdatedBoardInfo.add(new UpdateBoardInfo(BugaJiregeePiece.TYPE_DEER, 0, piece.getPoint().getIndex()));
					piece.setPointIndex(new Integer(INIT_DEER_POINTS[i]));
					lastUpdatedBoardInfo.add(new UpdateBoardInfo(BugaJiregeePiece.TYPE_DEER, 0, piece.getPointIndex().intValue()));
					break;
				case BugaJiregeePiece.TYPE_DOG:
					if (i < INIT_DOG_POINTS.length) {
//						piece.setPoint(this.board.getPoint(INIT_DOG_POINTS[i]));
//						lastUpdatedBoardInfo.add(new UpdateBoardInfo(BugaJiregeePiece.TYPE_DOG, 0, piece.getPoint().getIndex()));
						piece.setPointIndex(new Integer(INIT_DOG_POINTS[i]));
						lastUpdatedBoardInfo.add(new UpdateBoardInfo(BugaJiregeePiece.TYPE_DOG, 0, piece.getPointIndex()));
					} else {
						piece.setPointIndex(null);
						this.stockedDogPieces.add(piece);
					}
					break;
				}
			}
		}
	}

	public List<BugaJiregeePoint> getEmptyPoints() {
		List<BugaJiregeePoint> emptyPoints = new ArrayList<BugaJiregeePoint>();
		for (int i = 0; i < this.board.getPoints().length; i++) {
			if (getPieceByPointIndex(this.board.getPoint(i).getIndex()) == null) {
				emptyPoints.add(this.board.getPoint(i));
			}
		}
		return emptyPoints;
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

	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}
}
