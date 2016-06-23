package com.tehmou.flickrclient.network;

import java.util.List;

/**
 * Created by ttuo on 10/03/16.
 */
public class FlickrPhotoInfoResponse {
    private final PhotoInfo photo;

    public FlickrPhotoInfoResponse(PhotoInfo photo) {
        this.photo = photo;
    }

    public PhotoInfo getPhotoInfo() {
        return photo;
    }

    public static class PhotoInfo {
        private final String id;
        private final FlickrOwner owner;
        private final FlickrTitle title;
        private int views;
        private FlickrUrls urls;

        public PhotoInfo(String id, FlickrOwner owner, FlickrTitle title, int views, FlickrUrls urls) {
            this.id = id;
            this.owner = owner;
            this.title = title;
            this.views = views;
            this.urls = urls;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title._content;
        }

        public String getUsername() {
            return owner.username;
        }

        @Override
        public String toString() {
            return title._content + "\n" + owner.username;
        }
    }

    private static class FlickrTitle {
        private final String _content;

        public FlickrTitle(String _content) {
            this._content = _content;
        }
    }

    private static class FlickrUrls {
        private final List<FlickrUrl> urls;

        public FlickrUrls(List<FlickrUrl> urls) {
            this.urls = urls;
        }
    }

    private static class FlickrUrl {
        private final String type;
        private final String content;

        public FlickrUrl(String type, String content) {
            this.type = type;
            this.content = content;
        }
    }

    private static class FlickrOwner {
        private final String username;

        public FlickrOwner(String username) {
            this.username = username;
        }
    }
}
