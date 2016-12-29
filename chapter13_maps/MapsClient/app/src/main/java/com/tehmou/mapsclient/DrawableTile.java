package com.tehmou.mapsclient;

public class DrawableTile extends Tile {
    private final double screenX;
    private final double screenY;
    private final double width;
    private final double height;

    public DrawableTile(Tile tile, double screenX, double screenY, double width, double height) {
        this(tile.getX(), tile.getY(), tile.getZoom(), screenX, screenY, width, height);
    }

    public DrawableTile(int x, int y, int zoom, double screenX, double screenY, double width, double height) {
        super(x, y, zoom);
        this.screenX = screenX;
        this.screenY = screenY;
        this.width = width;
        this.height = height;
    }

    public double getScreenX() {
        return screenX;
    }

    public double getScreenY() {
        return screenY;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
