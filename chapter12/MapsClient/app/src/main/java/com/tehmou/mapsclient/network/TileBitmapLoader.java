package com.tehmou.mapsclient.network;

import android.graphics.Bitmap;
import android.util.Log;

import com.tehmou.mapsclient.DrawableTile;
import com.tehmou.mapsclient.Tile;
import com.tehmou.mapsclient.TileBitmap;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class TileBitmapLoader {
    private static final String TAG = TileBitmapLoader.class.getSimpleName();
    private final MapNetworkAdapter mapNetworkAdapter;
    private final Map<Integer, Bitmap> loadedTileBitmaps = new ConcurrentHashMap<>();
    private final Subject<Void, Void> tilesUpdateSubject = PublishSubject.create();

    public TileBitmapLoader(final MapNetworkAdapter mapNetworkAdapter) {
        this.mapNetworkAdapter = mapNetworkAdapter;
    }

    public void load(Collection<DrawableTile> mapTileDrawables) {
        Observable.from(mapTileDrawables)
                .filter(mapTileDrawable ->
                        !loadedTileBitmaps.containsKey(mapTileDrawable.tileHashCode()))
                .flatMap(this::loadTileBitmap)
                .map(mapTileBitmap -> {
                    if (mapTileBitmap != null && mapTileBitmap.getBitmap() != null) {
                        loadedTileBitmaps.put(mapTileBitmap.getMapTile().tileHashCode(),
                                mapTileBitmap.getBitmap());
                    }
                    return loadedTileBitmaps;
                })
                .subscribe(tile -> tilesUpdateSubject.onNext(null));
    }

    public Bitmap getBitmap(Tile tile) {
        final int hash = tile.tileHashCode();
        if (loadedTileBitmaps.containsKey(hash)) {
            return loadedTileBitmaps.get(hash);
        }
        return null;
    }

    public Observable<Void> bitmapsLoadedEvent() {
        return tilesUpdateSubject.asObservable();
    }

    private Observable<TileBitmap> loadTileBitmap(final Tile mapTile) {
        Log.d(TAG, "Loading bitmap for tile " + mapTile.toString());
        return MapTileUtils.loadMapTile(mapNetworkAdapter).call(mapTile);
    }
}
