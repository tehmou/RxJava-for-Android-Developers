package com.tehmou.mapsclient.utils;

/**
 * Mercator map projection utilities.
 *
 * https://google-developers.appspot.com/maps/documentation/javascript/examples/map-coordinates
 * http://paulbourke.net/geometry/transformationprojection/
 * http://en.wikipedia.org/wiki/Mercator_projection
 */
public class CoordinateProjection {
    final private int tileSize;
    final private PointD pxOrigin;
    final private double pxPerLonDegree;
    final private double pxPerLonRadian;

    public CoordinateProjection(final int tileSize) {
        this.tileSize = tileSize;
        this.pxOrigin = new PointD(tileSize / 2.0, tileSize / 2.0);
        this.pxPerLonDegree = tileSize / 360.0;
        this.pxPerLonRadian = tileSize / (2 * Math.PI);
    }

    public PointD fromLatLngToPoint(double lat, double lng, int zoom) {
        final int numTiles = 1 << zoom;
        final double sinY = Math.sin(Math.toRadians(lat));
        final double boundSinY = Math.max(Math.min(sinY, 0.999999), -0.999999);
        final double x = pxOrigin.x + lng * pxPerLonDegree;
        final double y = pxOrigin.y + 0.5 * Math.log((1 + boundSinY) / (1 - boundSinY)) * -pxPerLonRadian;
        return new PointD(x * numTiles, y * numTiles);
    }

    public LatLng fromPointToLatLng(final PointD point, final int zoom) {
        final int numTiles = 1 << zoom;
        final double x = point.x / numTiles;
        final double y = point.y / numTiles;
        final double lng = (x - pxOrigin.x) / pxPerLonDegree;
        final double latRadians = (y - pxOrigin.y) / -pxPerLonRadian;
        final double lat = Math.toDegrees(2 * Math.atan(Math.exp(latRadians)) - Math.PI / 2);
        return new LatLng(lat, lng);
    }

    public int pxSize(final int zoom) {
        return tileSize * (1 << zoom);
    }
}
