package com.tehmou.filebrowser;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.jakewharton.rxbinding.view.RxView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    ListView listView;
    FileListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else {
            initWithPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        initWithPermissions();
    }

    private void initWithPermissions() {
        FileBrowserStore store = ((FileBrowserApplication) getApplication()).getStore();
        initMembers();
        initFileList(store);
        initInputs(store.getSelectedFile(), store::setSelectedFile);
        initRendering(store.getSelectedFile(), store.getFiles());
    }

    private void initFileList(FileBrowserStore store) {
        store.getSelectedFile()
                .subscribeOn(Schedulers.io())
                .flatMap(MainActivity::createFilesObservable)
                .subscribe(store::setFiles);
    }

    private static Observable<File> getSelectedFile(ListView listView,
                                                    View previousButton,
                                                    View rootButton,
                                                    Observable<File> selectedFileObservable) {
        final File root = new File(
                Environment.getExternalStorageDirectory().getPath());

        Observable<File> listItemClickObservable =
                Observable.create(subscriber ->
                        listView.setOnItemClickListener(
                                (parent, view, position, id) -> {
                                    final File file = (File) view.getTag();
                                    Log.d(TAG, "Selected: " + file);
                                    if (file.isDirectory()) {
                                        subscriber.onNext(file);
                                    }
                                }));

        Observable<File> previousButtonObservable =
                RxView.clicks(previousButton)
                        .withLatestFrom(selectedFileObservable,
                                (ignore, selectedFile) -> selectedFile.getParentFile());

        Observable<File> rootButtonObservable =
                RxView.clicks(rootButton)
                        .map(event -> root);

        return Observable.merge(
                listItemClickObservable,
                previousButtonObservable,
                rootButtonObservable).startWith(root);
    }

    private void initMembers() {
        listView = (ListView) findViewById(R.id.list_view);
        adapter =
                new FileListAdapter(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

    }

    private void initInputs(Observable<File> selectedFile,
                            Action1<File> setSelectedFile) {
        getSelectedFile(
                listView,
                findViewById(R.id.previous_button),
                findViewById(R.id.root_button),
                selectedFile
        ).subscribe(setSelectedFile);
    }

    private void initRendering(Observable<File> selectedFile,
                               Observable<List<File>> fileList) {
        fileList
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        files -> {
                            Log.d(TAG, "Updating adapter with " + files.size() + " items");
                            adapter.clear();
                            adapter.addAll(files);
                        },
                        e -> Log.e(TAG, "Error reading files", e),
                        () -> Log.d(TAG, "Completed"));

        selectedFile
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> setTitle(file.getAbsolutePath()));
    }

    private static List<File> getFiles(final File f) {
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

    private static Observable<List<File>> createFilesObservable(final File f) {
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
