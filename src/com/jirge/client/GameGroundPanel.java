package com.jirge.client;

import java.util.ArrayList;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GameGroundPanel extends HorizontalPanel {

	static final String PIXEL = "px";
	static final int GAMEBOARD_BLOCKWIDTH = 70;
	static final int GAMEBOARD_BLOCKHEIGHT = 70;
	static final int GAMEBOARD_WIDTH = GAMEBOARD_BLOCKWIDTH*4;
	static final int GAMEBOARD_HEIGHT = GAMEBOARD_BLOCKHEIGHT*8;
	static final int SIDEBOARD_WIDTH = 100;
	static final int SIDEBOARD_HEIGHT = 180;

	static final int GAMEGROUND_SPACE = 10;
	static final int GAMEGROUND_WIDTH = GAMEGROUND_SPACE*4 + GAMEBOARD_WIDTH + SIDEBOARD_WIDTH;
    static final int GAMEGROUND_HEIGHT = GAMEGROUND_SPACE*2 + GAMEBOARD_HEIGHT;

    GameServiceAsync gameService;
    
    final CssColor originColor = CssColor.make("rgba(255,255,100,0.6)");
    final CssColor redrawColor = CssColor.make("rgba(255,255,255,1.0)");

	ArrayList<Point> groundPositinsArrayList = new ArrayList<Point>(0);
	Canvas canvas, playerCanvas;
    Context2d context, playerContext;
    ImageElement elementDog, elementDeer;
    int mouseX, mouseY;

	public GameGroundPanel() {
	    gameService = GameService.App.getInstance();
	
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

		GameBoard gameBoard = new GameBoard(new Point(0, 0), GAMEBOARD_BLOCKWIDTH, GAMEBOARD_BLOCKHEIGHT);
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
		    	  elementDog = (ImageElement) imageDeer.getElement().cast();
	    	  }
	    	});
	    imageDeer.setVisible(false);
	}

	void createGroundPositionsArrayList() {		
	}

	void initHandlers() {
		canvas.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				mouseX = event.getRelativeX(canvas.getElement());
				mouseY = event.getRelativeY(canvas.getElement());
				GWT.log("initHandlers() : mouseX, mouseX is " + Integer.toString(mouseX)+ ", " +  Integer.toString(mouseY));
			}
		});
	}
	
}