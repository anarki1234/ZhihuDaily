package com.kevin.zhihudaily.db;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import com.kevin.zhihudaily.model.DailyNewsModel;
import com.kevin.zhihudaily.model.NewsModel;

public class DataCache {

    private static DataCache mDataCache;
    private SparseArray<DailyNewsModel> mDailyMap;
    private SparseArray<NewsModel> mNewsMap;
    private static final int CACHE_MAX_SIZE = 10;

    private DataCache() {
        mDailyMap = new SparseArray<DailyNewsModel>();
        mNewsMap = new SparseArray<NewsModel>();
    }

    public static DataCache getInstance() {
        if (mDataCache == null) {
            mDataCache = new DataCache();
        }
        return mDataCache;
    }

    public void clearAllCache() {
        clearDailyCache();
    }

    @SuppressLint("NewApi")
    public void addDailyCache(String key, DailyNewsModel model) {
        if (mDailyMap.size() >= CACHE_MAX_SIZE) {
            mDailyMap.removeAtRange(0, CACHE_MAX_SIZE / 4);
        }
        mDailyMap.put(key.hashCode(), model);

    }

    public void deleteDailyCache(int key) {
        mDailyMap.remove(key);
    }

    public void clearDailyCache() {
        mDailyMap.clear();
    }

    public DailyNewsModel getDailyNewsModel(String key) {
        return mDailyMap.get(key.hashCode());
    }

    public void addNewsCache(int id, NewsModel model) {
        mNewsMap.put(id, model);
    }

    public NewsModel getNewsCache(int id) {
        return mNewsMap.get(id);
    }

    public void clearNewsCache() {
        mNewsMap.clear();
    }
}
