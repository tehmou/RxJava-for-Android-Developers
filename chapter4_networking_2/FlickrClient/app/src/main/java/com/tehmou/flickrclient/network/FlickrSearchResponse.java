package com.tehmou.flickrclient.network;

import java.util.List;

/**
 * Created by ttuo on 09/03/16.
 */
public class FlickrSearchResponse {
    private final Photos photos;

    private FlickrSearchResponse(Photos photos) {
        this.photos = photos;
    }

    public List<Photo> getPhotos() {
        return photos.photo;
    }

    private static class Photos {
        private final List<Photo> photo;

        public Photos(List<Photo> photo) {
            this.photo = photo;
        }
    }

    public static class Photo {
        private final String id;
        private final String owner;
        private final String title;

        public Photo(String id, String owner, String title) {
            this.id = id;
            this.owner = owner;
            this.title = title;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
