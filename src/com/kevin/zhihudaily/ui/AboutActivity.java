package com.kevin.zhihudaily.ui;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.kevin.zhihudaily.R;

public class AboutActivity extends ActionBarActivity {

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // Hide title text and set home as up
        getActionBar().setIcon(R.drawable.topbar_icon);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff33b5e5")));

        // set action bar title text color to white
        int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        TextView title = (TextView) findViewById(titleId);
        title.setTextColor(this.getResources().getColor(R.color.white));

        setContentView(R.layout.activity_about);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatService.onResume(this);
    }

}
