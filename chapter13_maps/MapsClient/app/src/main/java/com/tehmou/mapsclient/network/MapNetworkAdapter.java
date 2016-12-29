package com.tehmou.mapsclient.network;

import android.graphics.Bitmap;

import rx.Observable;

public interface MapNetworkAdapter {
    Observable<Bitmap> getMapTile(int zoom, int x, int y);
    int getTileSizePx();
}
