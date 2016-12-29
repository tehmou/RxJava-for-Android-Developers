package com.tehmou.mapsclient.network;

import android.graphics.Bitmap;
import android.util.Log;

import rx.Observable;

public class MapNetworkAdapterSimple implements MapNetworkAdapter {
    private static final String TAG = MapNetworkAdapterSimple.class.getCanonicalName();
    final private NetworkClient networkClient;
    final private String urlFormat;

    public MapNetworkAdapterSimple(
            final NetworkClient networkClient,
            final String urlFormat) {
        this.networkClient = networkClient;
        this.urlFormat = urlFormat;
    }

    public Observable<Bitmap> getMapTile(final int zoom, final int x, final int y) {
        Log.d(TAG, "getMapTile(" + zoom + ", " + x + ", " + y + ")");
        final String url = String.format(urlFormat, zoom, x, y);
        return networkClient
                .loadBitmap(url);
    }

    @Override
    public int getTileSizePx() {
        return 256;
    }
}
