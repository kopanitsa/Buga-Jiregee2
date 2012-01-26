package com.jirge.client;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;

public class TrapezoidBlock extends FieldBlock {
	
	public TrapezoidBlock(Point... points) {
		super(points);
	}
	
	@Override
	public void drawBlock(Context2d context) {
		if (context == null) {
			return;
		}

		if (getSize() < 1) {
			GWT.log("the point size is zero");
//			Log.debug("the point size is zero: " + Integer.toString(getSize()));
		}		

		/*
		context.strokeRect(10, 10, 100, 100);		
		context.beginPath();
		context.moveTo(50, 50);
		context.lineTo(200, 200);
*/

		GWT.log("getPoint(0) : " + Double.toString(getPoint(0).getX()) + " " + Double.toString(getPoint(0).getY()));

		context.moveTo(getPoint(0).getX(), getPoint(0).getY());
		context.lineTo(getPoint(1).getX(), getPoint(1).getY());
		context.lineTo(getPoint(2).getX(), getPoint(2).getY());
		context.lineTo(getPoint(3).getX(), getPoint(3).getY());
		context.lineTo(getPoint(0).getX(), getPoint(0).getY());
		
		context.closePath();
		context.stroke();
	}	
}
