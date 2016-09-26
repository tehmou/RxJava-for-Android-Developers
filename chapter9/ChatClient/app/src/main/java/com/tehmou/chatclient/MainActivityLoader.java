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

    private Gson gson;
    private final ChatMessageRepository chatMessageRepository = new ChatMessageRepository();
    private final ChatMessageApi chatMessageApi;

    private Subscription messageSubscription;
    private Socket socket;
    private MainActivityViewModel mainActivityViewModel;

    public MainActivityLoader(Context context) {
        super(context);
        Log.d(TAG, "MainActivityLoader");

        gson = new Gson();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://blooming-brook-85633.herokuapp.com/")
                .build();
        chatMessageApi = retrofit.create(ChatMessageApi.class);
        chatMessageApi.messages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messages -> {
                    for (String messageJson : messages) {
                        ChatMessage chatMessage = gson.fromJson(messageJson, ChatMessage.class);
                        chatMessageRepository.put(new ChatMessage(chatMessage, false));
                    }
                });
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

        socket = SocketUtil.createSocket();
        socket.connect();

        messageSubscription = SocketUtil.createMessageListener(socket)
                .subscribe(messageString -> {
                    Log.d(TAG, "chat message: " + messageString);
                    ChatMessage message = gson.fromJson(messageString, ChatMessage.class);
                    chatMessageRepository.put(new ChatMessage(message, false));
                });

        mainActivityViewModel = new MainActivityViewModel(
                chatMessageRepository.getMessageListStream(),
                message -> {
                    ChatMessage chatMessage = new ChatMessage(
                            UUID.randomUUID().toString(),
                            message,
                            new Date().getTime(),
                            true
                    );
                    chatMessageRepository.put(chatMessage);
                    String json = gson.toJson(chatMessage);
                    Log.d(TAG, "sending message: " + json);
                    socket.emit("chat message", json);
                }
        );
        mainActivityViewModel.subscribe();
        deliverResult(mainActivityViewModel);
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset");
        messageSubscription.unsubscribe();
        mainActivityViewModel.unsubscribe();
        mainActivityViewModel = null;
        socket.disconnect();
        socket = null;
    }
}
