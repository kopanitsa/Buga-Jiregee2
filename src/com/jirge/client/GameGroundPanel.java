package com.jirge.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.jirge.shared.PieceType;
import com.jirge.shared.UpdateBoardInfo;
import com.jirge.shared.message.Message;
import com.jirge.shared.message.TurnChangedMessage;
import com.jirge.shared.message.UpdateBoardMessage;

public class GameGroundPanel extends HorizontalPanel {

	private static final String PIXEL = "px";
	private static final int GAMEBOARD_MARGIN = 35;
	private static final int GAMEBOARD_BLOCKWIDTH = 75;
	private static final int GAMEBOARD_BLOCKHEIGHT = 75;
	private static final int GAMEBOARD_WIDTH = GAMEBOARD_BLOCKWIDTH*4;
	private static final int GAMEBOARD_HEIGHT = GAMEBOARD_BLOCKHEIGHT*8;
	private static final int GAMEPANEL_WIDTH = GAMEBOARD_WIDTH + GAMEBOARD_MARGIN*2;
	private static final int GAMEPANEL_HEIGHT = GAMEBOARD_HEIGHT + GAMEBOARD_MARGIN*2;

	private static final int SIDEBOARD_WIDTH = 100;
	private static final int SIDEBOARD_HEIGHT = 180;

	public static final int GAMEGROUND_SPACE = 10;
	public static final int GAMEGROUND_WIDTH = GAMEGROUND_SPACE*4 + GAMEPANEL_WIDTH + SIDEBOARD_WIDTH;
	public static final int GAMEGROUND_HEIGHT = GAMEGROUND_SPACE*2 + GAMEPANEL_HEIGHT;

    private GameServiceAsync asyncServiceHandler;
    private AsyncCallback <Boolean> movePlayerCallback;
    private AsyncCallback <int[]> getPositionsCallback;
 
    private GameBoard gameBoard;
	private final ArrayList<Integer> accessiblePositionArrayList = new ArrayList<Integer>(0);
	private final ArrayList<Integer> validPositinsArrayList = new ArrayList<Integer>(0);
	private volatile boolean isPlayTurn = false;

	private Canvas canvas;
    private Context2d context;
    private ImageElement elementDog, elementDeer;
    private int mouseX, mouseY;

	public GameGroundPanel() {
	    asyncServiceHandler = GameService.App.getInstance();

		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		setSize(Integer.toString(GAMEGROUND_WIDTH), Integer.toString(GAMEGROUND_HEIGHT));
		
	    VerticalPanel gameBoardPanel = new VerticalPanel();
	    gameBoardPanel.setSize(Integer.toString(GAMEPANEL_WIDTH), Integer.toString(GAMEPANEL_HEIGHT));
	    add(gameBoardPanel);

		canvas = Canvas.createIfSupported();
		canvas.setWidth(Integer.toString(GAMEPANEL_WIDTH) + PIXEL);
		canvas.setHeight(Integer.toString(GAMEPANEL_HEIGHT) + PIXEL);
		canvas.setCoordinateSpaceWidth(GAMEPANEL_WIDTH);
		canvas.setCoordinateSpaceHeight(GAMEPANEL_HEIGHT);
		context = canvas.getContext2d();
		gameBoardPanel.add(canvas);

		gameBoard = new GameBoard(new Point(GAMEBOARD_MARGIN, GAMEBOARD_MARGIN), GAMEBOARD_BLOCKWIDTH, GAMEBOARD_BLOCKHEIGHT);
		gameBoard.refreshAnimate(context);

	    VerticalPanel dogCountPanel = new VerticalPanel();
	    dogCountPanel.setSpacing(GAMEGROUND_SPACE);
	    dogCountPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);	    
	    add(dogCountPanel);
	    dogCountPanel.setSize(Integer.toString(SIDEBOARD_WIDTH), Integer.toString(SIDEBOARD_HEIGHT));

		Button dogButton = new Button("Dog Comes");
		dogButton.setPixelSize(100, 35);		
		dogButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (checkIfPlayTurn()) {
					int buttonX = event.getRelativeX(canvas.getElement());
					int buttonY = event.getRelativeY(canvas.getElement());
					GWT.log("Dog clicked (X, Y) " + Integer.toString(buttonX)+ ", " +  Integer.toString(buttonY));

					// Stack into the valid position array.
					validateClickedPosition(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE));
				}				
			}
		});
		dogCountPanel.add(dogButton);

		initHandlers();
		initAsyncCallbackHanlders();

	    final Image imageDog = new Image("images/JirgeDog.png");
	    add(imageDog);
	    imageDog.setSize("48px", "48px");
	    imageDog.addLoadHandler(new LoadHandler() {
	    	  public void onLoad(LoadEvent event) {
		    	  elementDog = (ImageElement) imageDog.getElement().cast();
	    	  }
	    	});
	    imageDog.setVisible(false);

	    final Image imageDeer = new Image("images/JirgeDeer.png");
	    add(imageDeer);
	    imageDeer.setSize("72px", "72px");
	    imageDeer.addLoadHandler(new LoadHandler() {
	    	  public void onLoad(LoadEvent event) {
		    	  elementDeer = (ImageElement) imageDeer.getElement().cast();
	    	  }
	    	});
	    imageDeer.setVisible(false);
	}

	private void initHandlers() {
		canvas.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (checkIfPlayTurn()) {
					mouseX = event.getRelativeX(canvas.getElement());
					mouseY = event.getRelativeY(canvas.getElement());
					GWT.log("Game board clicked (X, Y) " + Integer.toString(mouseX)+ ", " +  Integer.toString(mouseY));

					// Pick up a valid position to the stack.
					validateClickedPosition(new Point(mouseX, mouseY));
				}
			}
		});
	}

	private void initAsyncCallbackHanlders() {
		movePlayerCallback =  new AsyncCallback <Boolean> (){
			public void onFailure(Throwable caught) {
		    	GWT.log("movePlayerCallback() failed : " + caught.getMessage());
	    	}

			public void onSuccess(Boolean result) {
				GWT.log("movePlayerCallback() successed");
		    	setPlayTurnOn();
		    }
		};

		getPositionsCallback =  new AsyncCallback <int[]> (){
			public void onFailure(Throwable caught) {
				GWT.log("getPositionsCallback() failed : " + caught.getMessage());
		    }

		    public void onSuccess(int[] results) {
		    	GWT.log("getPositionsCallback() successed");

		    	refreshAccessiblePositions(results);
		    	setPlayTurnOn();
		    }
		};
	}

	private void playerMove(int from, int to) {
		GWT.log("playerMove() called from, to " + String.valueOf(from) + ", " + String.valueOf(from));
		asyncServiceHandler.movePiece(from, to, movePlayerCallback);
	}
	
	private void getNextPositions(int currentPosition) {
		GWT.log("getNextPositionsmessage() called on position: " + String.valueOf(currentPosition));
		asyncServiceHandler.getAccessiblePoints(currentPosition, getPositionsCallback);
	}
	
	/**
	 * Receives messages pushed from the server.
	 */
	public void receiveMsg(Message msg) {
		GWT.log("receiveMsg() : " + String.valueOf(msg.getType()));

		switch (msg.getType()) {
		case GAME_BEGIN:
			break;

		case UPDATE_BOARD:
			updateBoard((UpdateBoardMessage) msg);
			break;

		case TURN_CHANGED:
			turnChanged((TurnChangedMessage) msg);
			break;

		case NEW_PLAYER:
			break;

		case GAME_END:
			break;

		default:
			break;
		}
	}

	private void updateBoard(UpdateBoardMessage msg) {
		List<UpdateBoardInfo> updateInfo = msg.getUpdateInfo();
		for (UpdateBoardInfo info : updateInfo) {
			GWT.log("player type : " + info.playerType + ", before : " + info.beforePos + ", after : " + info.afterPos);

			updateGroundPositions(info);
			refreshGameBoard(context);
		}
	}

	private void turnChanged(TurnChangedMessage msg) {
		int[] movablePieces = msg.getMovablePieces();
		refreshAccessiblePositions(movablePieces);
		setPlayTurnOn();
	}

	private void validateClickedPosition(final Point clickPoint) {
		int index = gameBoard.pickPlayPosition(clickPoint);
       	GWT.log("validateClickedPosition() : clicked position " + String.valueOf(index) );
    	GWT.log("validateClickedPosition() : size " + String.valueOf(getValidPostions().size()));       
    	
		if (getAccessiblePositins().contains(new Integer(index))) {
			if (!getValidPostions().contains(new Integer(index))) {
				getValidPostions().add(new Integer(index));
			}

			if (getValidPostions().size() == 1) {
				GWT.log("getValidPostions().size() " + String.valueOf(getValidPostions().size()));
				setPlayTurnOff();
				getNextPositions(getValidPostions().get(0).intValue());
				return;
			}

			if (getValidPostions().size() == 2) {
				GWT.log("getValidPostions().size() " + String.valueOf(getValidPostions().size()));
				setPlayTurnOff();
				playerMove(getValidPostions().get(0).intValue(), getValidPostions().get(1).intValue());
				getValidPostions().clear();
			}
		}
	}

	synchronized private void refreshAccessiblePositions(int[] accessibles) {
		GWT.log("refreshAccessiblePositions : accessibles size " + String.valueOf(accessibles.length));

		getAccessiblePositins().clear();
		for (int i=0; i<accessibles.length; i++) {
			getAccessiblePositins().add(new Integer(accessibles[i]));
		}
	}
	
	synchronized private ArrayList<Integer> getValidPostions() {
		return validPositinsArrayList;
	}

	synchronized private ArrayList<Integer> getAccessiblePositins() {
		return accessiblePositionArrayList;
	}

    private void updateGroundPositions(UpdateBoardInfo info) {
    	ImageElement element = (info.playerType == PieceType.DOG) ? (elementDog) : (elementDeer);
    	if (info.beforePos == 36) {
        	gameBoard.animateIn(info.afterPos, context, element);
    	} else {
			gameBoard.animateOut(info.beforePos, context);
			if (info.afterPos != 0) {
		    	gameBoard.animateIn(info.afterPos, context, element);    		
			}
    	}
    }

    synchronized private void setPlayTurnOff() {
    	isPlayTurn = false;
    }
 
    synchronized private void setPlayTurnOn() {
    	isPlayTurn = true;
    }
    
    private boolean checkIfPlayTurn() {
    	return isPlayTurn;
    }

    // Redraw entire canvas and positions.
    private void refreshGameBoard(Context2d context) {
    	context.clearRect(0, 0, GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT);
		gameBoard.refreshAnimate(context);
    }
}