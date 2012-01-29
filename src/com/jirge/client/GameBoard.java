package com.jirge.client;

import java.util.ArrayList;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;

public class GameBoard {
	ArrayList<FieldBlock> fieldBlocksContainer = new ArrayList<FieldBlock>(0);
	ArrayList<Position> groundPositionsArrayList = new ArrayList<Position>(0);

	private int blkw, blkh;
	private double ltx, lty; 

    GameBoard(final Point topLeftPoint, int blockWidth, int blockHeight) {
    	this.ltx = topLeftPoint.getX();
    	this.lty = topLeftPoint.getY();
    	this.blkw = blockWidth;
    	this.blkh = blockHeight;

    	fieldBlocksContainer.clear();
    	
    	fieldBlocksContainer.add(new TrapezoidBlock(new Point(ltx+blkw, lty), new Point(ltx+blkw*2, lty), new Point(ltx+blkw*2, lty+blkh), new Point(ltx+blkw*1.5, lty+blkh)));
    	fieldBlocksContainer.add(new TrapezoidBlock(new Point(ltx+blkw*2, lty), new Point(ltx+blkw*3, lty), new Point(ltx+blkw*2.5, lty+blkh), new Point(ltx+blkw*2, lty+blkh)));
    	fieldBlocksContainer.add(new TriangleBlock(new Point(ltx+blkw*1.5, lty+blkh), new Point(ltx+blkw*2, lty+blkh), new Point(ltx+blkw*2, lty+blkh*2)));
    	fieldBlocksContainer.add(new TriangleBlock(new Point(ltx+blkw*2, lty+blkh), new Point(ltx+blkw*2.5, lty+blkh), new Point(ltx+blkw*2, lty+blkh*2)));

    	fieldBlocksContainer.add(new RectangleBlock(new Point(ltx, lty+blkh*2), new Point(ltx+blkw, lty+blkh*2), new Point(ltx+blkw, lty+blkh*3), new Point(ltx, lty+blkh*3)));
    	fieldBlocksContainer.add(new RectangleBlockDash(new Point(ltx+blkw, lty+blkh*2), new Point(ltx+blkw*2, lty+blkh*2), new Point(ltx+blkw*2, lty+blkh*3), new Point(ltx+blkw, lty+blkh*3)));
    	fieldBlocksContainer.add(new RectangleBlock(new Point(ltx+blkw*2, lty+blkh*2), new Point(ltx+blkw*3, lty+blkh*2), new Point(ltx+blkw*3, lty+blkh*3), new Point(ltx+blkw*2, lty+blkh*3)));
    	fieldBlocksContainer.add(new RectangleBlockDash(new Point(ltx+blkw*3, lty+blkh*2), new Point(ltx+blkw*4, lty+blkh*2), new Point(ltx+blkw*4, lty+blkh*3), new Point(ltx+blkw*3, lty+blkh*3)));

    	fieldBlocksContainer.add(new RectangleBlockDash(new Point(ltx, lty+blkh*3), new Point(ltx+blkw, lty+blkh*3), new Point(ltx+blkw, lty+blkh*4), new Point(ltx, lty+blkh*4)));
    	fieldBlocksContainer.add(new RectangleBlock(new Point(ltx+blkw, lty+blkh*3), new Point(ltx+blkw*2, lty+blkh*3), new Point(ltx+blkw*2, lty+blkh*4), new Point(ltx+blkw, lty+blkh*4)));
    	fieldBlocksContainer.add(new RectangleBlockDash(new Point(ltx+blkw*2, lty+blkh*3), new Point(ltx+blkw*3, lty+blkh*3), new Point(ltx+blkw*3, lty+blkh*4), new Point(ltx+blkw*2, lty+blkh*4)));
    	fieldBlocksContainer.add(new RectangleBlock(new Point(ltx+blkw*3, lty+blkh*3), new Point(ltx+blkw*4, lty+blkh*3), new Point(ltx+blkw*4, lty+blkh*4), new Point(ltx+blkw*3, lty+blkh*4)));

    	fieldBlocksContainer.add(new RectangleBlock(new Point(ltx, lty+blkh*4), new Point(ltx+blkw, lty+blkh*4), new Point(ltx+blkw, lty+blkh*5), new Point(ltx, lty+blkh*5)));
    	fieldBlocksContainer.add(new RectangleBlockDash(new Point(ltx+blkw, lty+blkh*4), new Point(ltx+blkw*2, lty+blkh*4), new Point(ltx+blkw*2, lty+blkh*5), new Point(ltx+blkw, lty+blkh*5)));
    	fieldBlocksContainer.add(new RectangleBlock(new Point(ltx+blkw*2, lty+blkh*4), new Point(ltx+blkw*3, lty+blkh*4), new Point(ltx+blkw*3, lty+blkh*5), new Point(ltx+blkw*2, lty+blkh*5)));
    	fieldBlocksContainer.add(new RectangleBlockDash(new Point(ltx+blkw*3, lty+blkh*4), new Point(ltx+blkw*4, lty+blkh*4), new Point(ltx+blkw*4, lty+blkh*5), new Point(ltx+blkw*3, lty+blkh*5)));    	

    	fieldBlocksContainer.add(new RectangleBlockDash(new Point(ltx, lty+blkh*5), new Point(ltx+blkw, lty+blkh*5), new Point(ltx+blkw, lty+blkh*6), new Point(ltx, lty+blkh*6)));
    	fieldBlocksContainer.add(new RectangleBlock(new Point(ltx+blkw, lty+blkh*5), new Point(ltx+blkw*2, lty+blkh*5), new Point(ltx+blkw*2, lty+blkh*6), new Point(ltx+blkw, lty+blkh*6)));
    	fieldBlocksContainer.add(new RectangleBlockDash(new Point(ltx+blkw*2, lty+blkh*5), new Point(ltx+blkw*3, lty+blkh*5), new Point(ltx+blkw*3, lty+blkh*6), new Point(ltx+blkw*2, lty+blkh*6)));
    	fieldBlocksContainer.add(new RectangleBlock(new Point(ltx+blkw*3, lty+blkh*5), new Point(ltx+blkw*4, lty+blkh*5), new Point(ltx+blkw*4, lty+blkh*6), new Point(ltx+blkw*3, lty+blkh*6)));    	

    	fieldBlocksContainer.add(new TriangleBlock(new Point(ltx+blkw, lty+blkh*7), new Point(ltx+blkw*2, lty+blkh*6), new Point(ltx+blkw*2, lty+blkh*7)));
    	fieldBlocksContainer.add(new TriangleBlock(new Point(ltx+blkw*2, lty+blkh*6), new Point(ltx+blkw*3, lty+blkh*7), new Point(ltx+blkw*2, lty+blkh*7)));    	
    	fieldBlocksContainer.add(new TriangleBlock(new Point(ltx+blkw, lty+blkh*7), new Point(ltx+blkw*2, lty+blkh*7), new Point(ltx+blkw*2, lty+blkh*8)));
    	fieldBlocksContainer.add(new TriangleBlock(new Point(ltx+blkw*2, lty+blkh*7), new Point(ltx+blkw*3, lty+blkh*7), new Point(ltx+blkw*2, lty+blkh*8)));

    	// Generate the positions to draw players.
    	groundPositionsArrayList.clear();
    	groundPositionsArrayList.add(new Position(new Point(0, 0)));

    	// Flat valley.
    	groundPositionsArrayList.add(new Position(new Point(ltx+blkw, lty)));
    	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*2, lty)));
    	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*3, lty)));
    	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*1.5, lty+blkh)));
    	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*2, lty+blkh)));
       	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*2.5, lty+blkh)));

    	// Row one ground.
       	groundPositionsArrayList.add(new Position(new Point(ltx, lty+blkh*2)));
       	groundPositionsArrayList.add(new Position(new Point(ltx+blkw, lty+blkh*2)));
       	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*2, lty+blkh*2)));
     	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*3, lty+blkh*2)));
    	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*4, lty+blkh*2)));

    	// Row two ground.
       	groundPositionsArrayList.add(new Position(new Point(ltx, lty+blkh*3)));
       	groundPositionsArrayList.add(new Position(new Point(ltx+blkw, lty+blkh*3)));
       	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*2, lty+blkh*3)));
     	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*3, lty+blkh*3)));
    	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*4, lty+blkh*3)));

    	// Row three ground.
       	groundPositionsArrayList.add(new Position(new Point(ltx, lty+blkh*4)));
       	groundPositionsArrayList.add(new Position(new Point(ltx+blkw, lty+blkh*4)));
       	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*2, lty+blkh*4)));
     	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*3, lty+blkh*4)));
    	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*4, lty+blkh*4)));

    	// Row four ground.
       	groundPositionsArrayList.add(new Position(new Point(ltx, lty+blkh*5)));
       	groundPositionsArrayList.add(new Position(new Point(ltx+blkw, lty+blkh*5)));
       	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*2, lty+blkh*5)));
     	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*3, lty+blkh*5)));
    	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*4, lty+blkh*5)));

    	// Row five ground.
       	groundPositionsArrayList.add(new Position(new Point(ltx, lty+blkh*6)));
       	groundPositionsArrayList.add(new Position(new Point(ltx+blkw, lty+blkh*6)));
       	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*2, lty+blkh*6)));
     	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*3, lty+blkh*6)));
    	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*4, lty+blkh*6)));

    	// Steep valley.
       	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*2, lty+blkh*7)));
       	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*2, lty+blkh*6)));
     	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*3, lty+blkh*7)));
    	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*2, lty+blkh*8)));

    	// Nest of dog.
    	groundPositionsArrayList.add(new Position(new Point(ltx+blkw*2, lty+blkh*8)));
    }

    public void drawGameBoard(final Context2d context) {
    	for (int i=0; i<getFieldBlocksSize(); i++) {
    		getFieldBlock(i).drawBlock(context);
    	}
    }

    private int getFieldBlocksSize() {
    	return fieldBlocksContainer.size();
    }

    private FieldBlock getFieldBlock(int i) {
    	return fieldBlocksContainer.get(i);
    }

    private ArrayList<Position> getGroundPositionsArrayList() {
    	return groundPositionsArrayList;
    }

    private int getGroundPositionsSize() {
    	return getGroundPositionsArrayList().size();
    }
    
    private Point getPositionPoint(int index) {
    	return getPosition(index).getPoint();
    }

    private Position getPosition(int index) {
    	return getGroundPositionsArrayList().get(index);
    }
 
    public int pickPlayPosition(Point rawPoint) {
		int pos = 0;
		try {
			int count = getGroundPositionsSize() - 1;
			for (int i= 0; i<count; i++) {
				pos = FieldBlock.poinsDistance(getPositionPoint(i), rawPoint) > (FieldBlock.poinsDistance(getPositionPoint(i+1), rawPoint)) ? (i) : (i+1);
			}
		} catch (IndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException("ERROR");
		}

		return pos;
	}

    public void animateIn(final int index, Context2d context, ImageElement element) {
    	getPosition(index).addAnimate(context, element);
    	getPosition(index).drawAnimate(context);
    }
    
    public void animateOut(final int index, Context2d context) {
    	getPosition(index).removeAnimate(context);
    }    
}