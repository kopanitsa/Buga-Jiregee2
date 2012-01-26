package com.jirge.client;

public class Point {
	double px;
	double py;

	public Point(double px, double py) {
		this.px = px;
		this.py = py;
	}
	
	public double getX() {
		return this.px;
	}
	
	public double getY() {
		return this.py;
	}
}