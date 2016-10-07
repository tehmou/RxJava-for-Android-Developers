package com.tehmou.mapsclient;

public class DrawableTile extends Tile {
    private final float screenX;
    private final float screenY;
    private final float width;
    private final float height;

    public DrawableTile(Tile tile, float screenX, float screenY, float width, float height) {
        this(tile.getX(), tile.getY(), tile.getZoom(), screenX, screenY, width, height);
    }

    public DrawableTile(int x, int y, int zoom, float screenX, float screenY, float width, float height) {
        super(x, y, zoom);
        this.screenX = screenX;
        this.screenY = screenY;
        this.width = width;
        this.height = height;
    }

    public float getScreenX() {
        return screenX;
    }

    public float getScreenY() {
        return screenY;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
