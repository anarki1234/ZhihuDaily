package com.kevin.zhihudaily.http;

import retrofit.RestAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ZhihuRequest {

    private static ZhihuRequestService mRequestService;

    public static final String BASE_URL = "http://news.at.zhihu.com/api/2";

    private static AsyncHttpClient mHttpClient = new AsyncHttpClient();

    /**
     * 最新消息
     */
    public static final String GET_LATEST_NEWS = "/news/latest";

    /**
     * 启动界面图像获取
     * （start-image/后的数字为图像的分辨率，测试了少数分辨率，发现接受如下几种
     * 格式：320*432，480*728，720*1184，1080*1776。若格式为这些之外的，获取的图像为空）
     */
    public static final String GET_START_IMAGE_XH = "/start-image/1080*1776";

    public static final String GET_START_IMAGE_H = "/start-image/720*1184";

    public static final String GET_START_IMAGE_M = "/start-image/480*728";

    public static final String GET_START_IMAGE_L = "/start-image/320*432";

    /**
     * 过往消息：
     * http://news.at.zhihu.com/api/2/news/before/20131119 
     * 说明：
     * a) 若果需要查询11月18日的消息，before/后的数字应为20131119
     * b) 知乎日报的生日为2013年5月19日，故before/后数字小于此的只会接受到空消息
     * c) 输入的今日之后的日期仍然获得今日内容，但是格式不同于最新消息的JSON格式
     */
    public static final String GET_OLD_NEWS = "/news/before/";

    /**
     * 热门消息
     */
    public static final String GET_HOT_NEWS = "/news/hot";

    /**
     * 软件推广
     */
    public static final String GET_PROMOTION = "/promotion/android?1386064554385 ";

    /**
     * 栏目总览
     *
     */
    public static final String GET_SECTIONS = "/sections";

    /**
     *  栏目具体消息查看：
     *  http://news-at.zhihu.com/api/2/section/1 （URL最后的数字参考『栏目总览』中的id属性）
     */
    public static final String GET_SECTION_BY_TYPE = "/section/";

    /**
     *  栏目具体消息查看：
     *  http://news-at.zhihu.com/api/2/section/1 （URL最后的数字参考『栏目总览』中的id属性）
     *  往前：http://news-at.zhihu.com/api/2/section/2/before/1384124400 
     * （加上一个时间戳，下一个时间戳的时间详见JSON数据最后）
     */
    public static final String GET_SECTION_BY_DATE = "http://news-at.zhihu.com/api/2/section/2/before/";

    private static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mHttpClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;

    }

    public static void getNewsDetail(String url, AsyncHttpResponseHandler responseHandler) {
        mHttpClient.get(url, null, responseHandler);
    }

    public static void getDailyNewsToday(AsyncHttpResponseHandler responseHandler) {
        get(GET_LATEST_NEWS, null, responseHandler);
    }

    public static void getDailyNewsByDate(String date, AsyncHttpResponseHandler responseHandler) {
        get(GET_OLD_NEWS + date, null, responseHandler);
    }

    public static ZhihuRequestService getRequestService() {
        if (mRequestService == null) {
            RestAdapter adapter = new RestAdapter.Builder().setEndpoint(BASE_URL).build();

            mRequestService = adapter.create(ZhihuRequestService.class);
        }
        return mRequestService;
    }
}
