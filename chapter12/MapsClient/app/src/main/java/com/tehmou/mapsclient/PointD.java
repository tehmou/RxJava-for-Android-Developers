package com.tehmou.mapsclient;

/**
 * Created by ttuo on 27/08/14.
 */
public class PointD {
    final public double x;
    final public double y;

    public PointD(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "PointD(" + x + ", " + y + ")";
    }
}
