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
	private static final int GAMEBOARD_BLOCKWIDTH = 70;
	private static final int GAMEBOARD_BLOCKHEIGHT = 70;
	private static final int GAMEBOARD_WIDTH = GAMEBOARD_BLOCKWIDTH*4;
	private static final int GAMEBOARD_HEIGHT = GAMEBOARD_BLOCKHEIGHT*8;
	private static final int SIDEBOARD_WIDTH = 100;
	private static final int SIDEBOARD_HEIGHT = 180;

	public static final int GAMEGROUND_SPACE = 10;
	public static final int GAMEGROUND_WIDTH = GAMEGROUND_SPACE*4 + GAMEBOARD_WIDTH + SIDEBOARD_WIDTH;
	public static final int GAMEGROUND_HEIGHT = GAMEGROUND_SPACE*2 + GAMEBOARD_HEIGHT;

    private GameServiceAsync asyncServiceHandler;
    private AsyncCallback <Boolean> movePlayerCallback;
    private AsyncCallback <int[]> getPositionsCallback;
 
    GameBoard gameBoard;
	final ArrayList<Integer> accessiblePositionArrayList = new ArrayList<Integer>(0);
	final ArrayList<Integer> validPositinsArrayList = new ArrayList<Integer>(2);
	volatile boolean isPlayTurn = false;

	Canvas canvas, playerCanvas;
    Context2d context, playerContext;
    ImageElement elementDog, elementDeer;
    int mouseX, mouseY;

	public GameGroundPanel() {
	    asyncServiceHandler = GameService.App.getInstance();
	
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		setSize(Integer.toString(GAMEGROUND_WIDTH), Integer.toString(GAMEGROUND_HEIGHT));
	    
	    VerticalPanel gameBoardPanel = new VerticalPanel();
	    gameBoardPanel.setSize(Integer.toString(GAMEBOARD_WIDTH), Integer.toString(GAMEBOARD_HEIGHT));
	    add(gameBoardPanel);
	    
		canvas = Canvas.createIfSupported();
		canvas.setWidth(Integer.toString(GAMEBOARD_WIDTH) + PIXEL);
		canvas.setHeight(Integer.toString(GAMEBOARD_HEIGHT) + PIXEL);
		canvas.setCoordinateSpaceWidth(GAMEBOARD_WIDTH);
		canvas.setCoordinateSpaceHeight(GAMEBOARD_HEIGHT);
		context = canvas.getContext2d();
		gameBoardPanel.add(canvas);

		gameBoard = new GameBoard(new Point(0, 0), GAMEBOARD_BLOCKWIDTH, GAMEBOARD_BLOCKHEIGHT);
		gameBoard.drawGameBoard(context);

	    VerticalPanel dogCountPanel = new VerticalPanel();
	    dogCountPanel.setSpacing(GAMEGROUND_SPACE);
	    dogCountPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);	    
	    add(dogCountPanel);
	    dogCountPanel.setSize(Integer.toString(SIDEBOARD_WIDTH), Integer.toString(SIDEBOARD_HEIGHT));

		Button dogButton = new Button("Dog Comes");
		dogButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				event.getClientX();
				//drawRect();
			}
		});
		dogCountPanel.add(dogButton);		

		initHandlers();
		initAsyncCallbackHanlders();

	    final Image imageDog = new Image("images/JiregeeDog.png");
	    add(imageDog);
	    imageDog.setSize("24", "24");
	    imageDog.addLoadHandler(new LoadHandler() {
	    	  public void onLoad(LoadEvent event) {
		    	  elementDog = (ImageElement) imageDog.getElement().cast();
	    	  }
	    	});
	    imageDog.setVisible(false);
	    
	    final Image imageDeer = new Image("images/JiregeeDeer.png");
	    add(imageDeer);
	    imageDeer.setSize("36", "36");
	    imageDeer.addLoadHandler(new LoadHandler() {
	    	  public void onLoad(LoadEvent event) {
		    	  elementDeer = (ImageElement) imageDeer.getElement().cast();
	    	  }
	    	});
	    imageDeer.setVisible(false);
	}

	void createGroundPositionsArrayList() {
	}

	private void initHandlers() {
		canvas.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (isPlayTurn) {
					mouseX = event.getRelativeX(canvas.getElement());
					mouseY = event.getRelativeY(canvas.getElement());
					GWT.log("initHandlers() : mouseX, mouseX is " + Integer.toString(mouseX)+ ", " +  Integer.toString(mouseY));
					validateClickedPosition(new Point(mouseX, mouseY));
				}
				
				//int dummyFrom = Random.nextInt(36);
				//int dummyTo = Random.nextInt(36%4);
				//palyerMove(dummyFrom, dummyTo);
			}
		});
	}

	private void initAsyncCallbackHanlders() {
		movePlayerCallback =  new AsyncCallback <Boolean> (){
		    public void onFailure(Throwable caught) {
		      GWT.log("Failed to get response from server" + caught.getMessage());
		    }

		    public void onSuccess(Boolean result) {
		      GWT.log(Boolean.toString(result));
		    }
		};

		getPositionsCallback =  new AsyncCallback <int[]> (){
		    public void onFailure(Throwable caught) {
		      GWT.log("Failed to get response from server" + caught.getMessage());
		    }

		    public void onSuccess(int[] results) {
		    	int num = results.length;
		    	getAccessiblePositins().clear();
		    	for (int i=0; i<num; i++) {
		    		getAccessiblePositins().add(new Integer(results[i]));		    		
		    	}
		    	GWT.log(Integer.toString(results.length));
		    }
		};
	}

	private void palyerMove(int from, int to) {
		asyncServiceHandler.movePiece(from, to, movePlayerCallback);
	}
	
	private void getNextPositions(int currentPosition) {
		asyncServiceHandler.getAccessiblePoints(currentPosition, getPositionsCallback);
	}

	private void playStart() {
		getNextPositions(36);
	}
	
	/**
	 * Receives messages pushed from the server.
	 */
	public void receiveMsg(Message msg) {
		switch (msg.getType()) {
		case GAME_BEGIN:
			getNextPositions(0);
			break;

		case UPDATE_BOARD:
			updateBoard((UpdateBoardMessage) msg);
			break;

		case TURN_CHANGED:
			turnChanged((TurnChangedMessage) msg);
			break;

		case NEW_PLAYER:
			break;

		case STEP_OCCURRED:
			break;

		case GAME_END:
			break;

		default:
			GWT.log("Unknown game type: " + msg.getType());
		}
	}

	private void updateBoard(UpdateBoardMessage msg) {
		List<UpdateBoardInfo> updateInfo = msg.getUpdateInfo();
		for (UpdateBoardInfo info : updateInfo) {
			GWT.log("player type : " + info.playerType + ", before : "
					+ info.beforePos + ", after : " + info.afterPos);
			updateGroundPositions(info);
		}
	}

	private void turnChanged(TurnChangedMessage msg) {
		int[] movablePieces = msg.getMovablePieces();
		refreshAccessiblePositions(movablePieces);
		isPlayTurn = true;

		GameServiceAsync gameService = GameService.App.getInstance();

		gameService.getAccessiblePoints(movablePieces[0], new AsyncCallback<int[]>() {
			public void onFailure(Throwable caught) {
				GWT.log("Failure: " + caught.getMessage());
			}

			public void onSuccess(int[] result) {
				for(int i=0; i<result.length; i++) {
					GWT.log((Integer.toString(result[i]) + " = " + Integer.toString(result[i])));
				}
			}
		});
	}

	private void validateClickedPosition(final Point clickPoint) {
		int index = gameBoard.pickPlayPosition(clickPoint);
		if (getAccessiblePositins().contains(new Integer(index))) {

			if (!getValidPostions().contains(new Integer(index))) {
				getValidPostions().add(new Integer(index));
			}

			if (getValidPostions().size() == 1) {
				getNextPositions(getValidPostions().get(0).intValue());
			}
			
			if (getValidPostions().size() == 2) {
				palyerMove(getValidPostions().get(0).intValue(), getValidPostions().get(1).intValue());
				getValidPostions().clear();
			}
		}
	}

	synchronized private void refreshAccessiblePositions(int[] accessibles) {
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
}