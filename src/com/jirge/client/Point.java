package com.jirge.client;

public class Point implements Comparable<Point>{
	private double px;
	private double py;

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
	
    public boolean equals(Object object) {
    	if (!(object instanceof Point))
    		return false;

        Point p = (Point)object;
        return ((p.getX() == getX()) && (p.getY() == this.getY()));
    }
	
	public int compareTo(Point otherPoint) {
        if ((otherPoint.getX() == getX()) && (otherPoint.getY() == this.getY()))
        	return 1;
        else
        	return 0;    		 
	 }
}