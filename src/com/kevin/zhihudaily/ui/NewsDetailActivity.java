package com.kevin.zhihudaily.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;

import com.kevin.zhihudaily.BuildConfig;
import com.kevin.zhihudaily.Constants;
import com.kevin.zhihudaily.R;
import com.kevin.zhihudaily.imageutil.Utils;

public class NewsDetailActivity extends FragmentActivity implements OnClickListener {

    private DetailPagerAdapter mAdapter;
    private ViewPager mPager;
    private int mNewsNum = 1;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (BuildConfig.DEBUG) {
            Utils.enableStrictMode();
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.news_detail_pager);

        // Fetch screen height and width, to use as our max size when loading images as this
        // activity runs full screen
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        Intent intent = getIntent();
        if (intent != null) {
            mNewsNum = getIntent().getIntExtra(Constants.INTENT_NEWS_NUM, 1);
        }

        // Set up ViewPager and backing adapter
        mAdapter = new DetailPagerAdapter(getSupportFragmentManager(), mNewsNum);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setPageMargin((int) getResources().getDimension(R.dimen.news_detail_pager_margin));
        mPager.setOffscreenPageLimit(2);

        // Set up activity to go full screen
        getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);

        // Enable some additional newer visibility and ActionBar features to create a more
        // immersive photo viewing experience
        if (Utils.hasHoneycomb()) {
            final ActionBar actionBar = getActionBar();

            // Hide title text and set home as up
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);

            // Hide and show the ActionBar as the visibility changes
            mPager.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int vis) {
                    if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
                        actionBar.hide();
                    } else {
                        actionBar.show();
                    }
                }
            });

            // Start low profile mode and hide ActionBar
            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
            actionBar.hide();
        }

        // Set the current item based on the extra passed in to this activity
        final int extraCurrentItem = getIntent().getIntExtra(Constants.INTENT_NEWS_INDEX, -1);
        if (extraCurrentItem != -1) {
            mPager.setCurrentItem(extraCurrentItem);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    private class DetailPagerAdapter extends FragmentStatePagerAdapter {

        private final int mSize;

        public DetailPagerAdapter(FragmentManager fm, int size) {
            super(fm);
            // TODO Auto-generated constructor stub
            mSize = size;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mSize;
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            return NewsDetailFragment.newInstance();
        }
    }

    /**
     * Set on the ImageView in the ViewPager children fragments, to enable/disable low profile mode
     * when the ImageView is touched.
     */
    @TargetApi(11)
    @Override
    public void onClick(View v) {
        final int vis = mPager.getSystemUiVisibility();
        if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        } else {
            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        }
    }
}
