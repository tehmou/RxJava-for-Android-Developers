package com.tehmou.filebrowser;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class FileBrowserStore {
    private final BehaviorSubject<File> selectedFile = BehaviorSubject.create();
    private final BehaviorSubject<List<File>> files = BehaviorSubject.create();

    public Observable<File> getSelectedFile() {
        return selectedFile.asObservable();
    }

    public void setSelectedFile(File value) {
        selectedFile.onNext(value);
    }

    public Observable<List<File>> getFiles() {
        return files.asObservable();
    }

    public void setFiles(List<File> value) {
        files.onNext(value);
    }
}
