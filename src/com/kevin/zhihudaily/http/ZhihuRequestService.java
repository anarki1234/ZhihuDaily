package com.kevin.zhihudaily.http;

import retrofit.http.GET;
import retrofit.http.Path;

import com.kevin.zhihudaily.model.DailyNewsModel;
import com.kevin.zhihudaily.model.NewsModel;

public interface ZhihuRequestService {

    @GET("/news/latest")
    DailyNewsModel getDailyNewsToday();

    @GET("/news/{id}")
    NewsModel getNewsById(@Path("id") int id);

    @GET("/news/before/{date}")
    DailyNewsModel getDailyNewsByDate(@Path("date") String date);
}
