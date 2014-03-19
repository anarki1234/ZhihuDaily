package com.kevin.zhihudaily.http;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.kevin.zhihudaily.db.DataBaseManager;
import com.kevin.zhihudaily.db.DataCache;
import com.kevin.zhihudaily.model.DailyNewsModel;

public class DataService extends IntentService {

    private static final String TAG = "DataService";

    public static final String INTENT_CACHE_ID = "cache_id";
    public static final String INTENT_ACTION_TYPE = "action_type";
    public static final int ACTION_WRITE_DAILY_NEWS = 0x01;
    public static final int ACTION_WRITE_NEWS_DEATIL = 0x02;
    public static final int ACTION_READ_DAILY_NEWS = 0x03;
    public static final int ACTION_READ_NEWS_DEATIL = 0x04;

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
        int action = intent.getIntExtra(INTENT_ACTION_TYPE, -1);
        switch (action) {
        case ACTION_WRITE_DAILY_NEWS:
            int key = intent.getIntExtra(INTENT_CACHE_ID, -1);
            if (key == -1) {
                break;
            }

            List<DailyNewsModel> models = DataCache.getInstance().getDailyNewsModels(key);
            DataBaseManager.getInstance().writeToDB(models);
            DataCache.getInstance().deleteDailyCache(key);
            break;

        default:
            break;
        }

    }

}
