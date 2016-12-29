package com.tehmou.newsreader.pojo;

/**
 * Created by ttuo on 20/02/16.
 */
public class ListItem {
    private final String title;

    public ListItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
