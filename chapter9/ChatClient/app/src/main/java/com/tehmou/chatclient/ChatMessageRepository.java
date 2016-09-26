package com.tehmou.chatclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Do not do this at home! This class is a trivial implementation of a repository and not optimized
 * for heavy usage or even thread safe. See for instance reark.io for more proper versions.
 *
 */
public class ChatMessageRepository {
    private final Map<String, ChatMessage> messageMap = new HashMap<>();
    private final BehaviorSubject<List<ChatMessage>> chatMessageListSubject = BehaviorSubject.create();

    // Internal function to calculate any changes in the hash and to publish it in the stream.
    // It's not pretty but for this purpose it is good enough.
    private void updateList() {
        ArrayList<ChatMessage> messageList = new ArrayList<>();
        messageList.addAll(messageMap.values());
        messageList.sort((a, b) -> {
            if (a.getTimestamp() > b.getTimestamp()) {
                return 1;
            } else if (a.getTimestamp() < b.getTimestamp()) {
                return -1;
            }
            return 0;
        });
        chatMessageListSubject.onNext(messageList);
    }

    public void put(ChatMessage chatMessage) {
        messageMap.put(chatMessage.getId(), chatMessage);
        updateList();
    }

    public Observable<List<ChatMessage>> getMessageListStream() {
        return chatMessageListSubject.asObservable();
    }
}
