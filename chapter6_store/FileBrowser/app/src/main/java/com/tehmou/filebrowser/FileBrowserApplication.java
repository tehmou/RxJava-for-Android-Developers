package com.tehmou.filebrowser;

import android.app.Application;

public class FileBrowserApplication extends Application {
    private final FileBrowserStore store = new FileBrowserStore();

    public FileBrowserStore getStore() {
        return store;
    }
}
