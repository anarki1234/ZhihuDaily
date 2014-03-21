package com.kevin.zhihudaily.db;

import java.util.List;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import com.kevin.zhihudaily.model.DailyNewsModel;
import com.kevin.zhihudaily.model.NewsModel;

public class DataCache {

    private static DataCache mDataCache;
    private SparseArray<List<DailyNewsModel>> mDailyMap;
    private NewsModel mNewsCache;
    private static final int CACHE_MAX_SIZE = 10;

    private DataCache() {
        mDailyMap = new SparseArray<List<DailyNewsModel>>();
        mNewsCache = new NewsModel();
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
    public void addDailyCache(String key, List<DailyNewsModel> list) {
        if (mDailyMap.size() >= CACHE_MAX_SIZE) {
            mDailyMap.removeAt(0);
        }
        mDailyMap.put(key.hashCode(), list);

    }

    public void deleteDailyCache(int key) {
        mDailyMap.remove(key);
    }

    public void clearDailyCache() {
        mDailyMap.clear();
    }

    public List<DailyNewsModel> getDailyNewsModels(String key) {
        return mDailyMap.get(key.hashCode());
    }

    public void setNewsCache(NewsModel model) {
        this.mNewsCache = model;
    }

    public NewsModel getNewsCache() {
        return this.mNewsCache;
    }

    public void clearNewsCache() {
        mNewsCache = null;
    }
}
