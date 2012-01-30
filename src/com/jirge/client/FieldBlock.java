package com.jirge.client;

import java.util.ArrayList;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.jirge.shared.UpdateBoardInfo;

public class FieldBlock {
    private ArrayList<Point> pointsArrayList = new ArrayList<Point>(0);

	public FieldBlock(Point... points) {
		if (points == null) {
			GWT.log("FieldBlock(): points is null");
			return;
		}

		for (Point point : points) {
			pointsArrayList.add(point);			
		}
	}

	public void drawBlock(Context2d context) {
		if (context == null) {
			return;
		}
	}

	public void playerIn(UpdateBoardInfo player) {
		
	}

	public void playerOut(UpdateBoardInfo player) {

	}

	public Point getPoint(final int i) {
		Point point = null;
		try {
			point = pointsArrayList.get(i);
		} catch (IndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException("ERROR");
		}

		return point;
	}

	public int getSize() {
		return pointsArrayList.size();
	}

	public static double poinsDistance(Point pX, Point pY) {
		GWT.log("poinsDistance() position point (X, Y) " + String.valueOf(pX.getX()) + "," + String.valueOf(pX.getY()));
		return Math.sqrt((pY.getX() - pX.getX()) * (pY.getX() - pX.getX())) + ((pY.getY() - pX.getY()) * (pY.getY() - pX.getY()));
	}

	private Point playerPosition(Point clickPoint) {
		Point point = null;

		try {
			int pos = 0;
			int count = getSize() - 1;
			for (int i= 0; i<count; i++) {
				pos = poinsDistance(getPoint(i), clickPoint) > (poinsDistance(getPoint(i+1), clickPoint)) ? (i) : (i+1);
			}
			point = getPoint(pos);
		} catch (IndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException("ERROR");
		}

		return point;
	}
}