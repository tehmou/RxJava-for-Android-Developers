package com.tehmou.chatclient;

import android.util.Log;

import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DataLayer {
    private static final String TAG = DataLayer.class.getSimpleName();
    private Gson gson;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageApi chatMessageApi;

    private Subscription messageSubscription;
    private Socket socket;

    public DataLayer() {
        gson = new Gson();
        chatMessageRepository = new ChatMessageRepository();
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

    public void connectSocket() {
        socket = SocketUtil.createSocket();
        socket.connect();

        messageSubscription = SocketUtil.createMessageListener(socket)
                .subscribe(messageString -> {
                    Log.d(TAG, "chat message: " + messageString);
                    ChatMessage message = gson.fromJson(messageString, ChatMessage.class);
                    chatMessageRepository.put(new ChatMessage(message, false));
                });
    }

    public void disconnectSocket() {
        if (messageSubscription != null) {
            messageSubscription.unsubscribe();
            messageSubscription = null;
        }
        if (socket != null) {
            socket.disconnect();
            socket = null;
        }
    }

    public Observable<List<ChatMessage>> getMessageListStream() {
        return chatMessageRepository.getMessageListStream();
    }

    public void sendChatMessage(String message) {
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
}
