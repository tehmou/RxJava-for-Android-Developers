package com.tehmou.mapsclient.network;

import android.util.Log;

import com.tehmou.mapsclient.Tile;
import com.tehmou.mapsclient.TileBitmap;

import rx.Observable;
import rx.functions.Func1;

public class MapTileNetworkUtils {
    private static final String TAG = MapTileNetworkUtils.class.getSimpleName();

    static public Func1<Tile, Observable<TileBitmap>> loadMapTile(
            final MapNetworkAdapter mapNetworkAdapter) {
        return mapTile -> mapNetworkAdapter.getMapTile(
                mapTile.getZoom(), mapTile.getX(), mapTile.getY())
                .map(bitmap -> new TileBitmap(mapTile, bitmap))
                .onErrorResumeNext(throwable -> {
                    Log.e(TAG, "Error loading tile (" + mapTile + ")", throwable);
                    throwable.printStackTrace();
                    return Observable.just(new TileBitmap(mapTile, null));
                });
    }
}
