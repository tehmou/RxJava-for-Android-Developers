package com.tehmou.filebrowser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by ttuo on 15/03/16.
 */
public class FileListAdapter extends ArrayAdapter<File> {
    public FileListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public FileListAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public FileListAdapter(Context context, int resource, File[] objects) {
        super(context, resource, objects);
    }

    public FileListAdapter(Context context, int resource, int textViewResourceId, File[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public FileListAdapter(Context context, int resource, List<File> objects) {
        super(context, resource, objects);
    }

    public FileListAdapter(Context context, int resource, int textViewResourceId, List<File> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        final File file = getItem(position);
        ((TextView) convertView).setText(file.isDirectory() ? file.getName() + "/" : file.getName());
        convertView.setTag(file);
        return convertView;
    }
}
