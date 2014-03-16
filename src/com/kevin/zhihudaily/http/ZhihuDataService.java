package com.kevin.zhihudaily.http;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

import com.kevin.zhihudaily.model.LatestNewsModel;

public interface ZhihuDataService {

    @GET("/news/latest")
    void getLastestNews(Callback<LatestNewsModel> callback);

    @GET("/news/before/{date}")
    void getOldNews(@Path("date") String date, Callback<LatestNewsModel> callback);

    @GET("/news/hot")
    void getHotNews(Callback<LatestNewsModel> callback);
}
