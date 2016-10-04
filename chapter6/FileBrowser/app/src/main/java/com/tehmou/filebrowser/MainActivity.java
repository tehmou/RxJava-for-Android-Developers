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
import android.widget.ListView;

import com.jakewharton.rxbinding.view.RxView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

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

        final ListView listView = (ListView) findViewById(R.id.list_view);
        FileListAdapter adapter =
                new FileListAdapter(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        final File root = new File(
                Environment.getExternalStorageDirectory().getPath());

        Observable<File> listItemClickObservable = listItemFileClickEvent(listView);

        Observable<Void> previousButtonObservable =
                RxView.clicks(findViewById(R.id.previous_button));

        Observable<Void> rootButtonObservable =
                RxView.clicks(findViewById(R.id.root_button));

        FileBrowserViewModel viewModel = new FileBrowserViewModel(
                root,
                listItemClickObservable,
                previousButtonObservable,
                rootButtonObservable
        );

        viewModel.getFiles()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        files -> {
                            Log.d(TAG, "Updating adapter with " + files.size() + " items");
                            adapter.clear();
                            adapter.addAll(files);
                        },
                        e -> Log.e(TAG, "Error reading files", e));

        viewModel.getSelectedFile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> setTitle(file.getAbsolutePath()));
    }

    private Observable<File> listItemFileClickEvent(ListView listView) {
        return Observable.create(subscriber ->
                listView.setOnItemClickListener(
                        (parent, view, position, id) -> {
                            final File file = (File) view.getTag();
                            if (file.isDirectory()) {
                                subscriber.onNext(file);
                            }
                        }));
    }
}
