package com.tehmou.chatclient;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

public class MainActivityViewModel {

    private CompositeSubscription subscriptions;
    private final Observable<List<ChatMessage>> messageListObservable;
    private final Action1<String> sendMessageAction;
    private final BehaviorSubject<List<ChatMessage>> messageList =
            BehaviorSubject.create(new ArrayList<>());

    public MainActivityViewModel(Observable<List<ChatMessage>> messageListObservable,
                                 Action1<String> sendMessageAction) {
        this.messageListObservable = messageListObservable;
        this.sendMessageAction = sendMessageAction;
    }

    public void subscribe() {
        unsubscribe();
        subscriptions = new CompositeSubscription();
        subscriptions.add(messageListObservable.subscribe(messageList));
    }

    public void unsubscribe() {
        if (subscriptions != null) {
            subscriptions.unsubscribe();
            subscriptions = null;
        }
    }

    public Observable<List<ChatMessage>> getMessageList() {
        return messageList.asObservable();
    }

    public void sendMessage(String message) {
        this.sendMessageAction.call(message);
    }
}
