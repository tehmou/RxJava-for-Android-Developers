package com.tehmou.newsreader.network;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by ttuo on 21/02/16.
 */
public class RawNetworkObservable {
    private static final String TAG = RawNetworkObservable.class.getSimpleName();

    private RawNetworkObservable() {

    }

    public static Observable<Response> create(final String url) {
        return Observable.create(
                new Observable.OnSubscribe<Response>() {
                    OkHttpClient client = new OkHttpClient();
                    @Override
                    public void call(Subscriber<? super Response> subscriber) {
                        try {
                            Response response = client.newCall(new Request.Builder().url(url).build()).execute();
                            subscriber.onNext(response);
                            subscriber.onCompleted();
                            if (!response.isSuccessful()) subscriber.onError(new Exception("error"));
                        } catch (IOException e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<String> getString(String url) {
        return create(url)
                .map(response -> {
                    try {
                        return response.body().string();
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading url " + url);
                    }
                    return null;
                });
    }
}
