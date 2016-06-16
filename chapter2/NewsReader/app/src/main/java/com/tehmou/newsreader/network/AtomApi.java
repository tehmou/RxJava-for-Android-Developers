package com.tehmou.newsreader.network;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Url;

/**
 * Created by ttuo on 21/02/16.
 */
public interface AtomApi {
    @GET
    Call<ResponseBody> getFeed(@Url String url);
}
