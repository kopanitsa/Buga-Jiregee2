package com.jirge.client;

import java.util.ArrayList;

import com.google.gwt.canvas.dom.client.Context2d;

public class GameBoard {
	ArrayList<FieldBlock> fieldBlocksTable = new ArrayList<FieldBlock>(0);
	ArrayList<Point> groundPositionsArrayList = new ArrayList<Point>(0);

	private int blkw, blkh;
	private double ltx, lty; 

    GameBoard(final Point topLeftPoint, int blockWidth, int blockHeight) {
    	this.ltx = topLeftPoint.getX();
    	this.lty = topLeftPoint.getY();
    	this.blkw = blockWidth;
    	this.blkh = blockHeight;

    	fieldBlocksTable.clear();
    	
    	fieldBlocksTable.add(new TrapezoidBlock(new Point(ltx+blkw, lty), new Point(ltx+blkw*2, lty), new Point(ltx+blkw*2, lty+blkh), new Point(ltx+blkw*1.5, lty+blkh)));
    	fieldBlocksTable.add(new TrapezoidBlock(new Point(ltx+blkw*2, lty), new Point(ltx+blkw*3, lty), new Point(ltx+blkw*2.5, lty+blkh), new Point(ltx+blkw*2, lty+blkh)));
    	fieldBlocksTable.add(new TriangleBlock(new Point(ltx+blkw*1.5, lty+blkh), new Point(ltx+blkw*2, lty+blkh), new Point(ltx+blkw*2, lty+blkh*2)));
    	fieldBlocksTable.add(new TriangleBlock(new Point(ltx+blkw*2, lty+blkh), new Point(ltx+blkw*2.5, lty+blkh), new Point(ltx+blkw*2, lty+blkh*2)));

    	fieldBlocksTable.add(new RectangleBlock(new Point(ltx, lty+blkh*2), new Point(ltx+blkw, lty+blkh*2), new Point(ltx+blkw, lty+blkh*3), new Point(ltx, lty+blkh*3)));
    	fieldBlocksTable.add(new RectangleBlockDash(new Point(ltx+blkw, lty+blkh*2), new Point(ltx+blkw*2, lty+blkh*2), new Point(ltx+blkw*2, lty+blkh*3), new Point(ltx+blkw, lty+blkh*3)));
    	fieldBlocksTable.add(new RectangleBlock(new Point(ltx+blkw*2, lty+blkh*2), new Point(ltx+blkw*3, lty+blkh*2), new Point(ltx+blkw*3, lty+blkh*3), new Point(ltx+blkw*2, lty+blkh*3)));
    	fieldBlocksTable.add(new RectangleBlockDash(new Point(ltx+blkw*3, lty+blkh*2), new Point(ltx+blkw*4, lty+blkh*2), new Point(ltx+blkw*4, lty+blkh*3), new Point(ltx+blkw*3, lty+blkh*3)));

    	fieldBlocksTable.add(new RectangleBlockDash(new Point(ltx, lty+blkh*3), new Point(ltx+blkw, lty+blkh*3), new Point(ltx+blkw, lty+blkh*4), new Point(ltx, lty+blkh*4)));
    	fieldBlocksTable.add(new RectangleBlock(new Point(ltx+blkw, lty+blkh*3), new Point(ltx+blkw*2, lty+blkh*3), new Point(ltx+blkw*2, lty+blkh*4), new Point(ltx+blkw, lty+blkh*4)));
    	fieldBlocksTable.add(new RectangleBlockDash(new Point(ltx+blkw*2, lty+blkh*3), new Point(ltx+blkw*3, lty+blkh*3), new Point(ltx+blkw*3, lty+blkh*4), new Point(ltx+blkw*2, lty+blkh*4)));
    	fieldBlocksTable.add(new RectangleBlock(new Point(ltx+blkw*3, lty+blkh*3), new Point(ltx+blkw*4, lty+blkh*3), new Point(ltx+blkw*4, lty+blkh*4), new Point(ltx+blkw*3, lty+blkh*4)));

    	fieldBlocksTable.add(new RectangleBlock(new Point(ltx, lty+blkh*4), new Point(ltx+blkw, lty+blkh*4), new Point(ltx+blkw, lty+blkh*5), new Point(ltx, lty+blkh*5)));
    	fieldBlocksTable.add(new RectangleBlockDash(new Point(ltx+blkw, lty+blkh*4), new Point(ltx+blkw*2, lty+blkh*4), new Point(ltx+blkw*2, lty+blkh*5), new Point(ltx+blkw, lty+blkh*5)));
    	fieldBlocksTable.add(new RectangleBlock(new Point(ltx+blkw*2, lty+blkh*4), new Point(ltx+blkw*3, lty+blkh*4), new Point(ltx+blkw*3, lty+blkh*5), new Point(ltx+blkw*2, lty+blkh*5)));
    	fieldBlocksTable.add(new RectangleBlockDash(new Point(ltx+blkw*3, lty+blkh*4), new Point(ltx+blkw*4, lty+blkh*4), new Point(ltx+blkw*4, lty+blkh*5), new Point(ltx+blkw*3, lty+blkh*5)));    	

    	fieldBlocksTable.add(new RectangleBlockDash(new Point(ltx, lty+blkh*5), new Point(ltx+blkw, lty+blkh*5), new Point(ltx+blkw, lty+blkh*6), new Point(ltx, lty+blkh*6)));
    	fieldBlocksTable.add(new RectangleBlock(new Point(ltx+blkw, lty+blkh*5), new Point(ltx+blkw*2, lty+blkh*5), new Point(ltx+blkw*2, lty+blkh*6), new Point(ltx+blkw, lty+blkh*6)));
    	fieldBlocksTable.add(new RectangleBlockDash(new Point(ltx+blkw*2, lty+blkh*5), new Point(ltx+blkw*3, lty+blkh*5), new Point(ltx+blkw*3, lty+blkh*6), new Point(ltx+blkw*2, lty+blkh*6)));
    	fieldBlocksTable.add(new RectangleBlock(new Point(ltx+blkw*3, lty+blkh*5), new Point(ltx+blkw*4, lty+blkh*5), new Point(ltx+blkw*4, lty+blkh*6), new Point(ltx+blkw*3, lty+blkh*6)));    	

    	fieldBlocksTable.add(new TriangleBlock(new Point(ltx+blkw, lty+blkh*7), new Point(ltx+blkw*2, lty+blkh*6), new Point(ltx+blkw*2, lty+blkh*7)));
    	fieldBlocksTable.add(new TriangleBlock(new Point(ltx+blkw*2, lty+blkh*6), new Point(ltx+blkw*3, lty+blkh*7), new Point(ltx+blkw*2, lty+blkh*7)));    	
    	fieldBlocksTable.add(new TriangleBlock(new Point(ltx+blkw, lty+blkh*7), new Point(ltx+blkw*2, lty+blkh*7), new Point(ltx+blkw*2, lty+blkh*8)));
    	fieldBlocksTable.add(new TriangleBlock(new Point(ltx+blkw*2, lty+blkh*7), new Point(ltx+blkw*3, lty+blkh*7), new Point(ltx+blkw*2, lty+blkh*8)));
    	
    	groundPositionsArrayList.clear();

    	groundPositionsArrayList.add(new Point(ltx+blkw, lty));
    	groundPositionsArrayList.add(new Point(ltx+blkw*2, lty));
    	groundPositionsArrayList.add(new Point(ltx+blkw*3, lty));
    	groundPositionsArrayList.add(new Point(ltx+blkw*1.5, lty+blkh));
    	groundPositionsArrayList.add(new Point(ltx+blkw*2, lty+blkh));
       	groundPositionsArrayList.add(new Point(ltx+blkw*2.5, lty+blkh));
           	
       	groundPositionsArrayList.add(new Point(ltx, lty+blkh*2));
       	groundPositionsArrayList.add(new Point(ltx+blkw, lty+blkh*2));
       	groundPositionsArrayList.add(new Point(ltx+blkw*2, lty+blkh*2));
     	groundPositionsArrayList.add(new Point(ltx+blkw*3, lty+blkh*2));
    	groundPositionsArrayList.add(new Point(ltx+blkw*4, lty+blkh*2));

       	groundPositionsArrayList.add(new Point(ltx, lty+blkh*3));
       	groundPositionsArrayList.add(new Point(ltx+blkw, lty+blkh*3));
       	groundPositionsArrayList.add(new Point(ltx+blkw*2, lty+blkh*3));
     	groundPositionsArrayList.add(new Point(ltx+blkw*3, lty+blkh*3));
    	groundPositionsArrayList.add(new Point(ltx+blkw*4, lty+blkh*3));

       	groundPositionsArrayList.add(new Point(ltx, lty+blkh*4));
       	groundPositionsArrayList.add(new Point(ltx+blkw, lty+blkh*4));
       	groundPositionsArrayList.add(new Point(ltx+blkw*2, lty+blkh*4));
     	groundPositionsArrayList.add(new Point(ltx+blkw*3, lty+blkh*4));
    	groundPositionsArrayList.add(new Point(ltx+blkw*4, lty+blkh*4));

       	groundPositionsArrayList.add(new Point(ltx, lty+blkh*5));
       	groundPositionsArrayList.add(new Point(ltx+blkw, lty+blkh*5));
       	groundPositionsArrayList.add(new Point(ltx+blkw*2, lty+blkh*5));
     	groundPositionsArrayList.add(new Point(ltx+blkw*3, lty+blkh*5));
    	groundPositionsArrayList.add(new Point(ltx+blkw*4, lty+blkh*5));

       	groundPositionsArrayList.add(new Point(ltx, lty+blkh*6));
       	groundPositionsArrayList.add(new Point(ltx+blkw, lty+blkh*6));
       	groundPositionsArrayList.add(new Point(ltx+blkw*2, lty+blkh*6));
     	groundPositionsArrayList.add(new Point(ltx+blkw*3, lty+blkh*6));
    	groundPositionsArrayList.add(new Point(ltx+blkw*4, lty+blkh*6));

       	groundPositionsArrayList.add(new Point(ltx+blkw*2, lty+blkh*7));
       	groundPositionsArrayList.add(new Point(ltx+blkw*2, lty+blkh*6));
     	groundPositionsArrayList.add(new Point(ltx+blkw*3, lty+blkh*7));
    	groundPositionsArrayList.add(new Point(ltx+blkw*2, lty+blkh*8));    	
    }

    public void drawGameBoard(final Context2d context) {
    	for (int i=0; i<getFieldBlocksSize(); i++) {
    		getFieldBlock(i).drawBlock(context);
    	}
    }

    private int getFieldBlocksSize() {
    	return fieldBlocksTable.size();
    }

    private FieldBlock getFieldBlock(int i) {
    	return fieldBlocksTable.get(i);
    }

    private ArrayList<Point> getGroundPositionsArrayList() {
    	return groundPositionsArrayList;
    }

    private int getGroundPositionsSize() {
    	return getGroundPositionsArrayList().size();
    }
    
    private Point getPoint(int index) {
    	return getGroundPositionsArrayList().get(index);
    }
    
    public int pickPlayPosition(Point rawPoint) {
		int pos = 0;
		try {
			int count = getGroundPositionsSize() - 1;
			for (int i= 0; i<count; i++) {
				pos = FieldBlock.poinsDistance(getPoint(i), rawPoint) > (FieldBlock.poinsDistance(getPoint(i+1), rawPoint)) ? (i) : (i+1);
			}
		} catch (IndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException("ERROR");
		}

		return pos;    
	}
}