package com.jirge.client;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;

public class RectangleBlock extends FieldBlock {

	RectangleBlock(Point... points) {
		super(points);
	}

	@Override
	public void drawBlock(final Context2d context) {
		if (context == null) {
			GWT.log(this.getClass().getName() + " : " + "invalid context");
			return;
		}

		if (getSize() < 1) {
			GWT.log(this.getClass().getName() + " : " + "the points array is empty");
			return;
		}

		context.save();
		context.beginPath();
		context.moveTo(getPoint(0).getX(), getPoint(0).getY());
		context.lineTo(getPoint(1).getX(), getPoint(1).getY());
		context.lineTo(getPoint(2).getX(), getPoint(2).getY());
		context.lineTo(getPoint(3).getX(), getPoint(3).getY());
		context.lineTo(getPoint(0).getX(), getPoint(0).getY());
		context.lineTo(getPoint(2).getX(), getPoint(2).getY());
		context.closePath();
		context.stroke();
	}
}