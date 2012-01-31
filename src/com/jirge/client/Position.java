package com.jirge.client;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;

public class Position {
	private Point position;
	private ImageElement animate;

	public Position(Point point) {
		this.position = point;
	}

	synchronized public void addAnimate(final Context2d context, final ImageElement animate) {
		this.animate = animate;
		drawAnimate(context);
	}

	synchronized public void removeAnimate(final Context2d context) {
		animate = null;
	}

	private void drawAnimate(final Context2d context) {
		context.drawImage(animate, (position.getX() - animate.getWidth()/2), (position.getY() - animate.getHeight()/2));
	}
	
	public Point getPoint() {
		return position;
	}

	public void refreshAnimate(final Context2d context) {
		if (animate != null) {
			drawAnimate(context);
		}
	}
}