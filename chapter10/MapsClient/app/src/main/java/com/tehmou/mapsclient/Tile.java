package com.tehmou.mapsclient;

/**
 * Created by ttuo on 21/09/16.
 */
public class Tile {
    private final int x;
    private final int y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
