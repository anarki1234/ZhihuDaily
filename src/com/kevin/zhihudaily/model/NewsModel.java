package com.kevin.zhihudaily.model;

import org.json.JSONObject;

public class NewsModel extends BaseNewsModel {
    String body = null;

    //    String js = null;

    //    String css = null;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    //    public String getJs() {
    //        return js;
    //    }
    //
    //    public void setJs(String js) {
    //        this.js = js;
    //    }

    //    public String getCss() {
    //        return css;
    //    }
    //
    //    public void setCss(String css) {
    //        this.css = css;
    //    }

    @Override
    public boolean parseJSON(JSONObject json) {
        // TODO Auto-generated method stub
        if (json == null) {
            return false;
        }
        this.body = json.optString("body");
        //        this.js = json.optString("js");
        //        this.css = json.optString("css");
        return super.parseJSON(json);
    }
}
