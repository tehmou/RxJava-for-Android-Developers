package com.tehmou.mapsclient;

public class Tile {
    private final int x;
    private final int y;
    private final int zoom;

    public Tile(int x, int y, int zoom) {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZoom() {
        return zoom;
    }

    public int tileHashCode() {
        int hash = zoom;
        hash = 31 * hash + x;
        hash = 31 * hash + y;
        return hash;
    }
}
