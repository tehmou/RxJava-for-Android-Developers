package com.tehmou.filebrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class FileBrowserViewModel {

    private static final String TAG = FileBrowserViewModel.class.getSimpleName();
    private final Observable<File> selectedFile;
    private final Observable<List<File>> files;

    public FileBrowserViewModel(File root,
                                Observable<File> listItemClickObservable,
                                Observable<Void> previousButtonObservable,
                                Observable<Void> rootButtonObservable) {
        BehaviorSubject<File> selectedFileBehavior = BehaviorSubject.create(root);

        Observable<File> previousFileEvent = previousButtonObservable
                .map(event -> selectedFileBehavior.getValue().getParentFile());

        Observable<File> rootEvent = rootButtonObservable.map(event -> root);

        Observable.merge(
                listItemClickObservable,
                previousFileEvent,
                rootEvent).subscribe(selectedFileBehavior);

        selectedFile = selectedFileBehavior;
        files = selectedFile
                .subscribeOn(Schedulers.io())
                .flatMap(this::createFilesObservable);
    }

    public Observable<File> getSelectedFile() {
        return selectedFile;
    }

    public Observable<List<File>> getFiles() {
        return files;
    }


    private List<File> getFiles(final File f) {
        List<File> fileList = new ArrayList<>();
        File[] files = f.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.isHidden() && file.canRead()) {
                    fileList.add(file);
                }
            }
        }

        return fileList;
    }

    Observable<List<File>> createFilesObservable(
            final File f) {
        return Observable.create(subscriber -> {
            try {
                final List<File> fileList = getFiles(f);
                subscriber.onNext(fileList);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
