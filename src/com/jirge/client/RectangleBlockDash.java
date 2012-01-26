package com.jirge.client;

import com.google.gwt.canvas.dom.client.Context2d;

public class RectangleBlockDash extends FieldBlock {
	RectangleBlockDash(Point... points) {
		super(points);
	}

	@Override
	public void drawBlock(Context2d context) {
		if (context == null) {
			return;
		}

		context.beginPath();
		context.moveTo(getPoint(0).getX(), getPoint(0).getY());
		context.lineTo(getPoint(1).getX(), getPoint(1).getY());
		context.lineTo(getPoint(2).getX(), getPoint(2).getY());
		context.lineTo(getPoint(3).getX(), getPoint(3).getY());
		context.lineTo(getPoint(0).getX(), getPoint(0).getY());
		context.moveTo(getPoint(3).getX(), getPoint(3).getY());
		context.lineTo(getPoint(1).getX(), getPoint(1).getY());

		context.closePath();
		context.stroke();
	}	
}
