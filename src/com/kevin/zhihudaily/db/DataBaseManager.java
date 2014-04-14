package com.kevin.zhihudaily.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
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
        Log.e(TAG, "==closeDB==");
        new Exception().printStackTrace();
        db.close();
        // mHelper = null;
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
     * 判断本地数据是否过期，返回true表示已过期需要更新，反之返回false
     * 
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

    public int writeDailyNewsToDB(DailyNewsModel dailyNewsModel) {
        Log.d(TAG, "==writeDailyNewsToDB");
        int count = 0;
        if (dailyNewsModel == null) {
            return 0;
        }

        if (!db.isOpen()) {
            db = mHelper.getReadableDatabase();
        }

        db.beginTransaction();
        try {
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
                Log.e(TAG, "==Image_source" + model.getImage_source());
                values.put(DataBaseConstants.IMAGE_SOURCE, model.getImage_source());
                values.put(DataBaseConstants.IMAGE_URL, model.getImage());
                values.put(DataBaseConstants.IMAGE_THUMBNAIL, model.getThumbnail());
                values.put(DataBaseConstants.SHARE_URL, model.getShare_url());
                values.put(DataBaseConstants.BODY, model.getBody());

                db.insertWithOnConflict(DataBaseConstants.NEWS_TABLE_NAME, null, values,
                        SQLiteDatabase.CONFLICT_REPLACE);

                count++;
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            db.endTransaction();
        }
        return count;
    }

    public int writeNewsToDB(NewsModel model) {
        Log.d(TAG, "==writeDailyNewsToDB");
        int count = 0;
        if (model == null) {
            return 0;
        }

        if (!db.isOpen()) {
            db = mHelper.getReadableDatabase();
        }

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DataBaseConstants.BODY, model.getBody());

            String[] whereArgs = { String.valueOf(model.getId()) };
            db.updateWithOnConflict(DataBaseConstants.NEWS_TABLE_NAME, values, DataBaseConstants.ID + "=?", whereArgs,
                    SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            db.endTransaction();
        }
        return count;
    }

    public int updateNewsBodyToDB(int id, String body) {
        Log.d(TAG, "==updateNewsBodyToDB");
        int count = 0;
        if (id == -1 || body == null) {
            return 0;
        }

        if (!db.isOpen()) {
            db = mHelper.getReadableDatabase();
        }

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DataBaseConstants.BODY, body);
            Log.d(TAG, "==body=" + body);
            //            String[] whereArgs = { String.valueOf(id) };
            //            count = db.updateWithOnConflict(DataBaseConstants.NEWS_TABLE_NAME, values, DataBaseConstants.ID + "=?",
            //                    whereArgs, SQLiteDatabase.CONFLICT_REPLACE);
            db.updateWithOnConflict(DataBaseConstants.NEWS_TABLE_NAME, values, DataBaseConstants.ID + "=" + id, null,
                    SQLiteDatabase.CONFLICT_REPLACE);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            db.endTransaction();
        }
        return count;
    }

    public DailyNewsModel readDaliyNewsList(String date) {
        DailyNewsModel dailyModel = null;
        if (!db.isOpen()) {
            db = mHelper.getReadableDatabase();
        }

        String[] columns = { DataBaseConstants.ID, DataBaseConstants.DATE, DataBaseConstants.GA_PREFIX,
                DataBaseConstants.IS_TOP_STORY, DataBaseConstants.TITLE, DataBaseConstants.URL,
                DataBaseConstants.IMAGE_SOURCE, DataBaseConstants.IMAGE_URL, DataBaseConstants.IMAGE_THUMBNAIL,
                DataBaseConstants.SHARE_URL, DataBaseConstants.BODY };
        String selection = "date=?";
        String[] selectionArgs = { date };
        String orderBy = DataBaseConstants.GA_PREFIX + " DESC" + ", " + DataBaseConstants.ID + " DESC";
        Cursor cursor = db.query(DataBaseConstants.NEWS_TABLE_NAME, columns, selection, selectionArgs, null, null,
                orderBy);
        if (cursor != null && cursor.getCount() > 0) {
            dailyModel = new DailyNewsModel();
            ArrayList<NewsModel> newsList = new ArrayList<NewsModel>();
            ArrayList<NewsModel> topStories = new ArrayList<NewsModel>();
            while (cursor.moveToNext()) {
                NewsModel model = new NewsModel();
                model.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.ID)));
                // Log.d(TAG, "==id=" + model.getId());
                model.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.DATE)));
                model.setGa_prefix(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.GA_PREFIX)));
                // Log.d(TAG, "==ga_prefix=" + model.getGa_prefix());
                model.setIs_top_story(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.IS_TOP_STORY)));
                model.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TITLE)));
                model.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.URL)));
                model.setImage_source(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.IMAGE_SOURCE)));
                model.setImage(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.IMAGE_URL)));
                model.setThumbnail(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.IMAGE_THUMBNAIL)));
                model.setShare_url(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.SHARE_URL)));

                // read body
                model.setBody(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.BODY)));

                newsList.add(model);
                if (model.isIs_top_story() == 1) {
                    topStories.add(model);
                }
            }
            dailyModel.setNewsList(newsList);
            dailyModel.setTopStories(topStories);
            String dateString = newsList.get(0).getDate();
            dailyModel.setDate(dateString);
            // convert string to date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            try {
                Date dateTime = formatter.parse(dateString);
                SimpleDateFormat diaplayFormat = new SimpleDateFormat("yyyy.M.d cccc");
                String displayDate = diaplayFormat.format(dateTime);
                dailyModel.setDisplay_date(displayDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            cursor.close();
        }
        return dailyModel;
    }

    public String readNewsBody(int id) {
        String body = null;
        if (!db.isOpen()) {
            db = mHelper.getReadableDatabase();
        }

        String[] columns = { DataBaseConstants.ID, DataBaseConstants.BODY };
        String selection = "id=?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = db
                .query(DataBaseConstants.NEWS_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            // read body
            body = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.BODY));
            Log.d(TAG, "==body=" + body);
            cursor.close();
        }
        return body;
    }
}
