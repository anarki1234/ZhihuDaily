package com.kevin.zhihudaily.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.kevin.zhihudaily.Constants;
import com.kevin.zhihudaily.http.BroadcastNotifier;
import com.kevin.zhihudaily.http.ZhihuRequest;
import com.kevin.zhihudaily.model.DailyNewsModel;
import com.kevin.zhihudaily.model.NewsModel;

public class DataService extends IntentService {

    private static final String TAG = "DataService";

    private BroadcastNotifier mBroadcastNotifier = new BroadcastNotifier(this);

    public DataService() {
        super("DataService");
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.e(TAG, "==onCreate");
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.e(TAG, "==onDestroy");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        Log.e(TAG, "==onHandleIntent");
        int action = intent.getIntExtra(Constants.INTENT_ACTION_TYPE, -1);
        switch (action) {
        case Constants.ACTION_WRITE_DAILY_NEWS:
            String key = intent.getStringExtra(Constants.INTENT_CACHE_ID);
            if (key == null) {
                break;
            }

            //            DailyNewsModel model = DataCache.getInstance().getDailyNewsModel(key);
            //            DataBaseManager.getInstance().writeDailyNewsToDB(model);

            break;
        case Constants.ACTION_WRITE_NEWS_DEATIL:
            int id = intent.getIntExtra(Constants.INTENT_NEWS_ID, -1);
            String body = intent.getStringExtra(Constants.INTENT_NEWS_BODY);
            if (id == -1 || body == null) {
                break;
            }

            DataBaseManager.getInstance().updateNewsBodyToDB(id, body, null);
            break;
        case Constants.ACTION_READ_DAILY_NEWS:
            String date = intent.getStringExtra(Constants.INTENT_NEWS_DATE);
            if (date == null) {
                break;
            }
            DailyNewsModel dailyNewsModel = DataBaseManager.getInstance().readDaliyNewsList(date);
            if (dailyNewsModel != null) {
                DataCache.getInstance().addDailyCache(dailyNewsModel.getDate(), dailyNewsModel);

                // notify ui to update
                mBroadcastNotifier.notifyDailyNewsDataReady(date);
            }
            break;
        case Constants.ACTION_READ_NEWS_DEATIL:
            int news_id = intent.getIntExtra(Constants.INTENT_NEWS_ID, -1);
            String news_date = intent.getStringExtra(Constants.INTENT_NEWS_DATE);
            if (news_date == null || news_id == -1) {
                break;
            }
            //            String news_body = DataBaseManager.getInstance().readNewsBody(news_id);
            NewsModel model = DataBaseManager.getInstance().readNewsBodyAndImageSource(news_id);
            if (model != null) {
                // Update to cache
                DataCache.getInstance().updateNewsDetailByID(news_date, news_id, model.getBody(),
                        model.getImage_source());

                // Notify ui to update
                mBroadcastNotifier.notifyNewsBodyDataReady(news_date, news_id);
            }
            break;
        case Constants.ACTION_GET_TODAY_NEWS:
            requestTodayNews();
            break;
        case Constants.ACTION_GET_DAILY_NEWS:
            String date1 = intent.getStringExtra(Constants.INTENT_NEWS_DATE);
            requestDailyNewsByDate(date1);
            break;
        case Constants.ACTION_GET_NEWS_DETAIL:
            String date2 = intent.getStringExtra(Constants.INTENT_NEWS_DATE);
            int id2 = intent.getIntExtra(Constants.INTENT_NEWS_ID, -1);
            requestNewsDetail(date2, id2);
            break;
        case Constants.ACTION_START_OFFLINE_DOWNLOAD:
            String date3 = intent.getStringExtra(Constants.INTENT_NEWS_DATE);
            startOfflineDownload(date3);
            break;
        default:
            break;
        }

    }

    private void requestTodayNews() {
        //        Log.d(TAG, "==IN=" + SystemClock.currentThreadTimeMillis());
        DailyNewsModel model = ZhihuRequest.getRequestService().getDailyNewsToday();
        //        Log.d(TAG, "==Model=" + model.getDisplay_date());
        //        Log.d(TAG, "==OUT=" + SystemClock.currentThreadTimeMillis());

        if (model != null) {
            int newTimeStamp = Integer.valueOf(model.getNewsList().get(0).getGa_prefix());

            int dataStatus = DataBaseManager.getInstance().checkDataExpire(newTimeStamp);
            if (dataStatus >= 0) {
                if (dataStatus > 0) {
                    // update timestamp
                    DataBaseManager.getInstance().setDataTimeStamp(newTimeStamp);
                }

                DataCache.getInstance().addDailyCache(model.getDate(), model);

                // notify ui to update
                mBroadcastNotifier.notifyDailyNewsDataReady(model.getDate());
            }

        }
    }

    private void requestDailyNewsByDate(String date) {
        DailyNewsModel model = ZhihuRequest.getRequestService().getDailyNewsByDate(date);
        if (model != null) {
            DataCache.getInstance().addDailyCache(model.getDate(), model);

            // notify ui to update
            mBroadcastNotifier.notifyDailyNewsDataReady(model.getDate());
        }
    }

    private void requestNewsDetail(String date, int id) {
        NewsModel model = ZhihuRequest.getRequestService().getNewsById(id);
        //        Log.d(TAG, "==ModelBody=" + model.getBody());
        if (model != null) {
            DataCache.getInstance().updateNewsDetailByID(date, id, model.getBody(), model.getImage_source());

            // Notify ui to update
            mBroadcastNotifier.notifyNewsBodyDataReady(date, id);
        }
    }

    private void startOfflineDownload(String date) {
        Calendar calendar = Calendar.getInstance();
        Date todayDate = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        String todayDateString = formatter.format(todayDate);

        DailyNewsModel model;
        if (todayDateString.equals(date)) {
            model = ZhihuRequest.getRequestService().getDailyNewsToday();
        } else {
            model = ZhihuRequest.getRequestService().getDailyNewsByDate(date);

        }
        if (model != null) {

            int newTimeStamp = Integer.valueOf(model.getNewsList().get(0).getGa_prefix());
            if (DataBaseManager.getInstance().checkDataExpire(newTimeStamp) >= 0) {

            }

            DataBaseManager.getInstance().writeDailyNewsToDB(model);

            ArrayList<NewsModel> list = (ArrayList<NewsModel>) model.getNewsList();
            int size = list.size();
            if (size > 0) {
                int incr = 100 / size + 1;
                int progress = 0;
                //                ArrayList<NewsModel> newslist = new ArrayList<NewsModel>();
                for (NewsModel news : list) {
                    news = ZhihuRequest.getRequestService().getNewsById(news.getId());
                    //                    newslist.add(news);
                    if (news != null) {
                        //                        Log.d(TAG, "==startOfflineDownload  image_source" + news.getImage_source());
                        DataBaseManager.getInstance().updateNewsBodyToDB(news.getId(), news.getBody(),
                                news.getImage_source());
                    }

                    // notify ui to update
                    progress += incr;
                    mBroadcastNotifier.notifyProgress(progress);

                }

                // Write to DB
                //                DataBaseManager.getInstance().updateNewsListToDB(newslist);

                // notify ui to update
                mBroadcastNotifier.notifyProgress(100);
            }
        }
    }

}
