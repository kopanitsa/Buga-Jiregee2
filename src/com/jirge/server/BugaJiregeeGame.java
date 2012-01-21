package com.jirge.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("serial")
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

	private static final int[] INIT_DOG_POINTS = {13, 14, 15, 18, 20, 23, 24, 25,		// 8 pieces on board
												  36, 36, 36, 36, 36, 36, 36, 36,
												  36, 36, 36, 36, 36, 36, 36, 36, 36};	// 17 pieces stocked

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

	@Persistent
	private State state;

	@Persistent
	private List<Player> players;

	@Persistent
	private int currentPlayerIndex;

	//@Persistent private List<Move> moves;
	
	@Persistent
	private BugaJiregeeBoard board;

	@Persistent
	private Date timeCreated;

	public BugaJiregeeGame(Long id) {
		this.id = id;
		this.state = State.NEW;
		this.timeCreated = new Date(System.currentTimeMillis());
		this.players = new ArrayList<Player>();
		this.currentPlayerIndex = 0;
		this.board = new BugaJiregeeBoard();
	}


	//
	// APIs
	//

	public void start() {
		resetPiecesPosition();
		this.currentPlayerIndex = 0;
	}

	public List<BugaJiregeePiece> getMovablePieces() {
		List<BugaJiregeePiece> movablePieces = new ArrayList<BugaJiregeePiece>();
		List<BugaJiregeePiece> pieces = this.players.get(this.currentPlayerIndex).getPieces();
		for (BugaJiregeePiece piece : pieces) {
			List<BugaJiregeePoint> accessiblePoints = getAccessiblePoints(piece);
			if (!accessiblePoints.isEmpty()) {
				movablePieces.add(piece);
			}
		}
		return movablePieces;
	}

	public List<BugaJiregeePoint> getAccessiblePoints(BugaJiregeePiece piece) {
		return piece.getAccessiblePoints();
	}

	public BugaJiregeePiece movePiece(BugaJiregeePiece piece, BugaJiregeePoint toPoint) {
		if (this.players.get(this.currentPlayerIndex).getPieces().contains(piece)) {
			BugaJiregeePiece removedPiece = piece.moveTo(toPoint);
			this.currentPlayerIndex = (this.currentPlayerIndex + 1) % 2;	// 0, 1, 0, 1, ...
			return removedPiece;
		}
		// TODO implement to throw an error exception.
		return null;
	}

	//

	private void resetPiecesPosition() {
		for (Player player : this.players) {
			for (int i = 0; i < player.getPieces().size(); i++) {
				switch (player.getPieces().get(i).getType()) {
				case BugaJiregeePiece.TYPE_DEER:
					player.getPieces().get(i).setPoint(this.board.getPoint(INIT_DEER_POINTS[i]));
					break;
				case BugaJiregeePiece.TYPE_DOG:
					player.getPieces().get(i).setPoint(this.board.getPoint(INIT_DOG_POINTS[i]));
					break;
				}
			}
		}
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
