package com.kevin.zhihudaily.db;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.kevin.zhihudaily.Constants;
import com.kevin.zhihudaily.model.DailyNewsModel;

public class DataService extends IntentService {

    private static final String TAG = "DataService";

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
            DataBaseManager.getInstance().writeToDB(model);

            break;

        default:
            break;
        }

    }

}
