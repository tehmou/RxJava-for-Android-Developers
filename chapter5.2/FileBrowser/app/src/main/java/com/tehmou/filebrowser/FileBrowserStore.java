package com.tehmou.filebrowser;

import java.io.File;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class FileBrowserStore {
    private final BehaviorSubject<File> selectedFile = BehaviorSubject.create();

    public Observable<File> getSelectedFile() {
        return selectedFile.asObservable();
    }

    public void setSelectedFile(File value) {
        selectedFile.onNext(value);
    }
}
