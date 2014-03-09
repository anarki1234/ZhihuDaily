package com.kevin.zhihudaily.http;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

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

    }

}
