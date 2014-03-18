package com.kevin.zhihudaily.db;

import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kevin.zhihudaily.model.DailyNewsModel;
import com.kevin.zhihudaily.model.NewsModel;

public class DataBaseManager {
    private DataBaseHelper mHelper;
    private SQLiteDatabase db;
    private static DataBaseManager mInstance;

    private DataBaseManager(Context context) {
        mHelper = new DataBaseHelper(context);

        db = mHelper.getWritableDatabase();
    }

    public static DataBaseManager newInstance(Context context) {
        if (mInstance == null) {
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

    public int writeToDB(List<DailyNewsModel> list) {
        int count = 0;
        if (list == null || list.size() == 0) {
            return 0;
        }

        db.beginTransaction();
        try {
            for (DailyNewsModel dailyNewsModel : list) {
                String date = dailyNewsModel.getDate();

                List<NewsModel> topList = dailyNewsModel.getTopStories();
                HashMap<Integer, Boolean> topIDMap = new HashMap<Integer, Boolean>();
                for (NewsModel topModel : topList) {
                    topIDMap.put(topModel.getId(), true);
                }

                List<NewsModel> newList = dailyNewsModel.getNewsList();
                for (NewsModel model : newList) {
                    ContentValues values = new ContentValues();
                    values.put(DataBaseConstants.ID, model.getId());
                    values.put(DataBaseConstants.DATE, date);
                    if (topIDMap.containsKey(model.getId())) {
                        values.put(DataBaseConstants.IS_TOP_STORY, 1);
                    } else {
                        values.put(DataBaseConstants.IS_TOP_STORY, 0);
                    }

                    values.put(DataBaseConstants.TITLE, model.getTitle());
                    values.put(DataBaseConstants.URL, model.getUrl());
                    values.put(DataBaseConstants.IMAGE_SOURCE, model.getImage_source());
                    values.put(DataBaseConstants.IMAGE_URL, model.getImage());
                    values.put(DataBaseConstants.IMAGE_THUMBNAIL, model.getThumbnail());
                    values.put(DataBaseConstants.SHARE_URL, model.getShare_url());
                    values.put(DataBaseConstants.BODY, model.getBody());

                    long rowid = db.insertWithOnConflict(DataBaseConstants.NEWS_TABLE_NAME, null, values,
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
