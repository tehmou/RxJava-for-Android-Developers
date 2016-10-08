package com.tehmou.mapsclient;

import android.graphics.Bitmap;

public class TileBitmap {
    final private Tile mapTile;
    final private Bitmap bitmap;

    public TileBitmap(Tile mapTile, Bitmap bitmap) {
        this.mapTile = mapTile;
        this.bitmap = bitmap;
    }

    public Tile getMapTile() {
        return mapTile;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
