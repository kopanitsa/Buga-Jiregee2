package com.jirge.client;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

public class RectangleBlock extends FieldBlock {
    final CssColor originColor = CssColor.make("rgba(255,255,100,0.6)");

	RectangleBlock(Point... points) {
		super(points);
	}
		
	@Override
	public void drawBlock(Context2d context) {
		if (context == null) {
			return;
		}

		context.save();
		context.setFillStyle(originColor);	
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
