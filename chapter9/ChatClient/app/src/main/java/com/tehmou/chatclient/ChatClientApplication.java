package com.tehmou.chatclient;

import android.app.Application;

public class ChatClientApplication extends Application {
    private DataLayer dataLayer;

    public ChatClientApplication() {
        dataLayer = new DataLayer();
    }

    // In a production project this could be injected with Dagger, but to keep the example simple
    // we keep it in the Application life cycle by using Application directly.
    public DataLayer getDataLayer() {
        return dataLayer;
    }
}
