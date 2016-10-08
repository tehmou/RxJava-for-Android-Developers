package com.tehmou.mapsclient.network;

import android.graphics.Bitmap;

import rx.Observable;

public interface NetworkClient {
    Observable<String> loadString(final String url);
    Observable<Bitmap> loadBitmap(final String url);
}