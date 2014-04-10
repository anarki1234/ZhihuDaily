package com.kevin.zhihudaily.db;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import com.kevin.zhihudaily.model.DailyNewsModel;
import com.kevin.zhihudaily.model.NewsModel;

public class DataCache {

    private static DataCache mDataCache;
    private SparseArray<DailyNewsModel> mDailyMap;
    private SparseArray<NewsModel> mNewsMap;
    private static final int CACHE_MAX_SIZE = 10;
    private SparseArray<String> mBodyMap;

    private DataCache() {
        mDailyMap = new SparseArray<DailyNewsModel>();
        mNewsMap = new SparseArray<NewsModel>();
        mBodyMap = new SparseArray<String>();
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

    public boolean updateNewsBodyByID(String key, int id, String body) {
        DailyNewsModel dailyModel = mDailyMap.get(key.hashCode());
        ArrayList<NewsModel> list = (ArrayList<NewsModel>) dailyModel.getNewsList();
        for (NewsModel model : list) {
            if (model.getId() == id) {
                model.setBody(body);
                return true;
            }
        }
        return false;
    }

    public NewsModel getNewsModelByDateAndID(String key, int id) {
        if (key == null || id == -1) {
            return null;
        }
        DailyNewsModel dailyModel = mDailyMap.get(key.hashCode());
        ArrayList<NewsModel> list = (ArrayList<NewsModel>) dailyModel.getNewsList();
        for (NewsModel model : list) {
            if (model.getId() == id) {
                return model;
            }
        }
        return null;
    }

    public String getNewsBodyByDateAndID(String key, int id) {
        DailyNewsModel dailyModel = mDailyMap.get(key.hashCode());
        ArrayList<NewsModel> list = (ArrayList<NewsModel>) dailyModel.getNewsList();
        for (NewsModel model : list) {
            if (model.getId() == id) {
                return model.getBody();
            }
        }
        return null;
    }

    public void addNewsBodyCache(int id, String body) {
        mBodyMap.put(id, body);
    }

    public String getNewsBodyCache(int id) {
        return mBodyMap.get(id);
    }

    public void clearNewsBodyCache() {
        mBodyMap.clear();
    }
}
