package com.tehmou.mapsclient;

import android.nfc.Tag;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MapTileUtils {
    private static final String TAG = MapTileUtils.class.getSimpleName();

    public static  Collection<DrawableTile> calculateMapTiles(final double tileSizePx,
                                                              final Integer zoomLevel,
                                                              final PointD viewSize,
                                                              final PointD offset) {
        Log.d(TAG, "calculateMapTiles(" + tileSizePx + ", " + zoomLevel + ", " + viewSize + ", " + offset + ")");

        final int firstTileX = (int) Math.floor(-offset.x / tileSizePx);
        final int firstTileY = (int) Math.floor(-offset.y / tileSizePx);
        final int lastTileX = (int) Math.ceil((-offset.x + viewSize.x) / tileSizePx);
        final int lastTileY = (int) Math.ceil((-offset.y + viewSize.y) / tileSizePx);

        final int left = Math.max(0, firstTileX);
        final int right = Math.min(1 << zoomLevel, lastTileX);
        final int top = Math.max(0, firstTileY);
        final int bottom = Math.min(1 << zoomLevel, lastTileY);

        final List<DrawableTile> mapTileList = new ArrayList<>();
        for (int i = left; i < right; i++) {
            for (int n = top; n < bottom; n++) {
                final DrawableTile mapTile = new DrawableTile(
                        i, n, zoomLevel,
                        i*tileSizePx + offset.x,
                        n*tileSizePx + offset.y,
                        tileSizePx, tileSizePx);
                mapTileList.add(mapTile);
            }
        }
        return mapTileList;
    }
}
