package com.kevin.zhihudaily.db;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kevin.zhihudaily.model.DailyNewsModel;
import com.kevin.zhihudaily.model.NewsModel;

public class DataBaseManager {
    private DataBaseHelper mHelper;
    private SQLiteDatabase db;

    public DataBaseManager(Context context) {
        mHelper = new DataBaseHelper(context);

        db = mHelper.getWritableDatabase();
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
