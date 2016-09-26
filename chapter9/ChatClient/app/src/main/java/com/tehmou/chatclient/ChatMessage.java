package com.tehmou.chatclient;

public class ChatMessage {
    private String id;
    private String message;
    private long timestamp;

    // Exclude from serialisation.
    private boolean isPending = false;

    public ChatMessage(String id, String message, long timestamp, boolean isPending) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.isPending = isPending;
    }

    public ChatMessage(ChatMessage chatMessage, boolean isPending) {
        this.id = chatMessage.id;
        this.message = chatMessage.message;
        this.timestamp = chatMessage.timestamp;
        this.isPending = isPending;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isPending() {
        return isPending;
    }

    @Override
    public String toString() {
        return message + (isPending ? " (pending)" : "");
    }
}
