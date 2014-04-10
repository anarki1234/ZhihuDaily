package com.kevin.zhihudaily.db;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.kevin.zhihudaily.Constants;
import com.kevin.zhihudaily.http.BroadcastNotifier;
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

            DailyNewsModel model = DataCache.getInstance().getDailyNewsModel(key);
            DataBaseManager.getInstance().writeDailyNewsToDB(model);

            break;
        case Constants.ACTION_WRITE_NEWS_DEATIL:
            int id = intent.getIntExtra(Constants.INTENT_NEWS_ID, -1);
            if (id == -1) {
                break;
            }

            NewsModel newsModel = DataCache.getInstance().getNewsCache(id);
            DataBaseManager.getInstance().writeNewsToDB(newsModel);
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
            String body = DataBaseManager.getInstance().readNewsBody(news_id);
            if (body != null) {
                // Update to cache
                DataCache.getInstance().updateNewsBodyByID(news_date, news_id, body);

                // Notify ui to update
                mBroadcastNotifier.notifyNewsBodyDataReady(news_date, news_id);
            }
            break;
        default:
            break;
        }

    }

}
