package com.kevin.zhihudaily.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LatestNewsModel {

    String date = null;
    List<NewsModel> newsList = new ArrayList<NewsModel>();
    boolean is_today = false;
    List<NewsModel> storieList = new ArrayList<NewsModel>();
    String display_date = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<NewsModel> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsModel> newsList) {
        this.newsList = newsList;
    }

    public boolean isIs_today() {
        return is_today;
    }

    public void setIs_today(boolean is_today) {
        this.is_today = is_today;
    }

    public List<NewsModel> getTopStories() {
        return storieList;
    }

    public void setTopStories(List<NewsModel> topStories) {
        this.storieList = topStories;
    }

    public String getDisplay_date() {
        return display_date;
    }

    public void setDisplay_date(String display_date) {
        this.display_date = display_date;
    }

    public boolean parseJSON(JSONObject json) {
        if (json == null) {
            return false;
        }
        this.date = json.optString("date");
        try {
            JSONArray newsaArray = json.getJSONArray("news");
            int size = newsaArray.length();
            for (int i = 0; i < size; i++) {
                NewsModel model = new NewsModel();
                model.parseJSON(newsaArray.getJSONObject(i));
                this.newsList.add(model);
            }

            JSONArray storiesArray = json.getJSONArray("top_stories");
            size = storiesArray.length();
            for (int i = 0; i < size; i++) {
                NewsModel model = new NewsModel();
                model.parseJSON(storiesArray.getJSONObject(i));
                this.storieList.add(model);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.display_date = json.optString("display_date");
        return true;
    }
}
