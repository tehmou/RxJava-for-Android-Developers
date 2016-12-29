package com.tehmou.chatclient;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

import rx.Observable;
import rx.subscriptions.BooleanSubscription;

public class SocketUtil {
    private static final String TAG = SocketUtil.class.getSimpleName();

    public static Observable<String> createMessageListener(final Socket socket) {
        return Observable.create(subscriber -> {
            final Emitter.Listener listener =
                    args -> subscriber.onNext(args[0].toString());
            socket.on("chat message", listener);
            subscriber.add(BooleanSubscription.create(
                    () -> {
                        Log.d(TAG, "unsubscribe");
                        socket.off("chat message", listener);
                    }));
        });
    }

    public static Socket createSocket() {
        Socket socket = null;
        try {
            socket = IO.socket("https://blooming-brook-85633.herokuapp.com/");
        } catch (URISyntaxException e) {
            Log.e(TAG, "Error creating socket", e);
        }
        return socket;
    }
}
