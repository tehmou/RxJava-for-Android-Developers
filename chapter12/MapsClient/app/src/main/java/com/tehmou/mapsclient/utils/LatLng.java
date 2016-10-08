package com.tehmou.mapsclient.utils;

public class LatLng {
    final private double lat;
    final private double lng;

    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public int hashCode() {
        int hash = Double.valueOf(lat).hashCode();
        hash += 31 * hash + Double.valueOf(lng).hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof LatLng && hashCode() == o.hashCode();
    }
}
