package com.tehmou.chatclient;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class MessageUtil {
    public static Observable<List<String>> accumulateMessages(
            Observable<String> messageObservable) {
        return messageObservable.scan(new ArrayList<String>(),
                (list, value) -> {
                    list.add(value);
                    return list;
                });
    }
}
