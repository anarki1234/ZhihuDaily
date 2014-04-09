package com.kevin.zhihudaily;

public class Constants {

    public static final String INTENT_CACHE_ID = "cache_id";
    public static final String INTENT_ACTION_TYPE = "action_type";

    public static final int ACTION_WRITE_DAILY_NEWS = 0x01;
    public static final int ACTION_WRITE_NEWS_DEATIL = 0x02;
    public static final int ACTION_READ_DAILY_NEWS = 0x03;
    public static final int ACTION_READ_NEWS_DEATIL = 0x04;

    public static final String INTENT_NEWS_NUM = "news_num";
    public static final String INTENT_NEWS_INDEX = "news_index";
    public static final String INTENT_NEWS_DATE = "news_date";
    public static final String INTENT_NEWS_ID = "news_id";
    public static final String INTENT_NEWS_TITLE = "news_title";
    public static final String INTENT_NEWS_URL = "news_url";
    public static final String INTENT_NEWS_IMAGE_SOURCE = "news_image_source";
    public static final String INTENT_NEWS_IMAGE_URL = "news_image_url";
    public static final String INTENT_NEWS_BODY = "news_body";

    // Defines a custom Intent action
    public static final String ACTION_BROADCAST = "com.kevin.zhihudaily.BROADCAST";

    // Defines the key for the status "extra" in an Intent
    public static final String EXTRA_NETWORK_ISCONNECTED = "com.kevin.zhihudaily.NETWORK_ISCONNECTED";
    public static final String EXTRA_NETWORK_TYPE = "com.kevin.zhihudaily.NETWORK_TYPE";

}
