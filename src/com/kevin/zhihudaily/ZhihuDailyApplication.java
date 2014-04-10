package com.kevin.zhihudaily;

import android.app.Application;

import com.kevin.zhihudaily.db.DataBaseManager;
import com.kevin.zhihudaily.db.DataCache;
import com.kevin.zhihudaily.imageutil.Utils;

public class ZhihuDailyApplication extends Application {

    private static ZhihuDailyApplication mInstance;
    public static boolean sIsConnected = false;
    public static int sNetworkType = -1;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Utils.enableStrictMode();
        }

        mInstance = this;

        // init database
        DataBaseManager.newInstance(this);
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();

        DataBaseManager.getInstance().closeDB();
        DataCache.getInstance().clearAllCache();
    }

    public static ZhihuDailyApplication getInstance() {
        return mInstance;
    }
}
