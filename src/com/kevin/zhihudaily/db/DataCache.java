package com.kevin.zhihudaily.db;

import java.util.List;

import android.util.SparseArray;

import com.kevin.zhihudaily.model.DailyNewsModel;

public class DataCache {

    private static DataCache mDataCache;
    private SparseArray<List<DailyNewsModel>> mDailyMap;

    private DataCache() {
        mDailyMap = new SparseArray<List<DailyNewsModel>>();
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

    public void addDailyCache(int key, List<DailyNewsModel> list) {
        mDailyMap.put(key, list);
    }

    public void deleteDailyCache(int key) {
        mDailyMap.remove(key);
    }

    public void clearDailyCache() {
        mDailyMap.clear();
    }

    public List<DailyNewsModel> getDailyNewsModels(int key) {
        return mDailyMap.get(key);
    }
}
