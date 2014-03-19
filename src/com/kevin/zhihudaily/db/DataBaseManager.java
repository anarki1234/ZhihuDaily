package com.kevin.zhihudaily.db;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseBooleanArray;

import com.kevin.zhihudaily.model.DailyNewsModel;
import com.kevin.zhihudaily.model.NewsModel;

public class DataBaseManager {
    private static final String TAG = "DataBaseManager";
    private DataBaseHelper mHelper;
    private SQLiteDatabase db;
    private static DataBaseManager mInstance;
    private static Context mContext;

    private DataBaseManager(Context context) {
        mHelper = new DataBaseHelper(context);

        db = mHelper.getWritableDatabase();

        initDataTimeStamp();
    }

    public static DataBaseManager newInstance(Context context) {
        if (mInstance == null) {
            mContext = context;
            mInstance = new DataBaseManager(context);
        }
        return mInstance;
    }

    public static DataBaseManager getInstance() {
        return mInstance;
    }

    public void closeDB() {
        db.close();
        mHelper = null;
    }

    private void initDataTimeStamp() {
        SharedPreferences timestamp = mContext.getSharedPreferences(DataBaseConstants.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        DataBaseConstants.TIME_STAMP_ID = timestamp.getInt(DataBaseConstants.SP_TIME_STAMP, 0);
    }

    public int getDataTimeStamp() {
        return DataBaseConstants.TIME_STAMP_ID;
    }

    /**
     * �жϱ��������Ƿ���ڣ�����true��ʾ�ѹ�����Ҫ���£���֮����false
     * @param timestamp
     * @return
     */
    public boolean checkDataExpire(int timestamp) {
        if (timestamp > DataBaseConstants.TIME_STAMP_ID) {
            return true;
        } else {
            return false;
        }
    }

    public void setDataTimeStamp(int timestamp) {
        DataBaseConstants.TIME_STAMP_ID = timestamp;
        SharedPreferences sp = mContext.getSharedPreferences(DataBaseConstants.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(DataBaseConstants.SP_TIME_STAMP, timestamp);
        editor.commit();
    }

    public int writeToDB(List<DailyNewsModel> list) {
        Log.e(TAG, "==writeToDB");
        int count = 0;
        if (list == null || list.size() == 0) {
            return 0;
        }

        db.beginTransaction();
        try {
            for (DailyNewsModel dailyNewsModel : list) {
                String date = dailyNewsModel.getDate();

                List<NewsModel> topList = dailyNewsModel.getTopStories();
                SparseBooleanArray topIDMap = new SparseBooleanArray();
                for (NewsModel topModel : topList) {
                    topIDMap.put(topModel.getId(), true);
                }

                List<NewsModel> newList = dailyNewsModel.getNewsList();
                for (NewsModel model : newList) {
                    ContentValues values = new ContentValues();
                    values.put(DataBaseConstants.ID, model.getId());
                    values.put(DataBaseConstants.DATE, date);
                    if (topIDMap.get(model.getId())) {
                        values.put(DataBaseConstants.IS_TOP_STORY, 1);
                    } else {
                        values.put(DataBaseConstants.IS_TOP_STORY, 0);
                    }

                    values.put(DataBaseConstants.GA_PREFIX, model.getGa_prefix());
                    values.put(DataBaseConstants.TITLE, model.getTitle());
                    values.put(DataBaseConstants.URL, model.getUrl());
                    values.put(DataBaseConstants.IMAGE_SOURCE, model.getImage_source());
                    values.put(DataBaseConstants.IMAGE_URL, model.getImage());
                    values.put(DataBaseConstants.IMAGE_THUMBNAIL, model.getThumbnail());
                    values.put(DataBaseConstants.SHARE_URL, model.getShare_url());
                    values.put(DataBaseConstants.BODY, model.getBody());

                    db.insertWithOnConflict(DataBaseConstants.NEWS_TABLE_NAME, null, values,
                            SQLiteDatabase.CONFLICT_REPLACE);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            db.endTransaction();
        }
        return count;
    }

    public List<DailyNewsModel> readDaliyNewsList(String date) {
        return null;
    }

    public List<NewsModel> readNewsDetail(String date) {
        return null;
    }
}