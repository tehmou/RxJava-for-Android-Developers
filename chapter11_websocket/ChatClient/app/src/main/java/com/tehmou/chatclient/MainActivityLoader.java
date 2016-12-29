package com.tehmou.chatclient;

import android.content.Context;
import android.util.Log;

import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.util.Date;
import java.util.UUID;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivityLoader extends android.support.v4.content.Loader<MainActivityViewModel> {
    private static final String TAG = MainActivityLoader.class.getSimpleName();

    private DataLayer dataLayer;
    private MainActivityViewModel mainActivityViewModel;

    public MainActivityLoader(Context context, DataLayer dataLayer) {
        super(context);
        Log.d(TAG, "MainActivityLoader");
        this.dataLayer = dataLayer;
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading");
        if (mainActivityViewModel != null) {
            deliverResult(mainActivityViewModel);
            return;
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        Log.d(TAG, "onForceLoad");
        dataLayer.connectSocket();
        mainActivityViewModel = new MainActivityViewModel(
                dataLayer.getMessageListStream(),
                dataLayer::sendChatMessage
        );
        mainActivityViewModel.subscribe();
        deliverResult(mainActivityViewModel);
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset");
        dataLayer.disconnectSocket();
        mainActivityViewModel.unsubscribe();
        mainActivityViewModel = null;
    }
}
