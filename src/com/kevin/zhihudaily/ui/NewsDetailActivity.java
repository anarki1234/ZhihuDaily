package com.kevin.zhihudaily.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.kevin.zhihudaily.BuildConfig;
import com.kevin.zhihudaily.Constants;
import com.kevin.zhihudaily.R;
import com.kevin.zhihudaily.db.DataCache;
import com.kevin.zhihudaily.imageutil.Utils;
import com.kevin.zhihudaily.model.DailyNewsModel;
import com.kevin.zhihudaily.model.NewsModel;

public class NewsDetailActivity extends ActionBarActivity implements OnClickListener {

    private static final String TAG = "NewsDetailActivity";
    private DetailPagerAdapter mAdapter;
    private ViewPager mPager;
    private Drawable mActionBarDrawable;

    private DailyNewsModel mDailyNewsModel;
    private int mNewsNum = 1;
    private NewsModel mSelectModel = new NewsModel();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (BuildConfig.DEBUG) {
            Utils.enableStrictMode();
        }
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_news_detail);

        // Fetch screen height and width, to use as our max size when loading images as this
        // activity runs full screen
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        Intent intent = getIntent();
        String dateKey = intent.getStringExtra(Constants.INTENT_NEWS_DATE);
        if (intent != null) {
            mNewsNum = intent.getIntExtra(Constants.INTENT_NEWS_NUM, 1);
            mSelectModel.setId(intent.getIntExtra(Constants.INTENT_NEWS_ID, -1));
            mSelectModel.setTitle(intent.getStringExtra(Constants.INTENT_NEWS_TITLE));
            mSelectModel.setUrl(intent.getStringExtra(Constants.INTENT_NEWS_URL));
            mSelectModel.setImage_source(intent.getStringExtra(Constants.INTENT_NEWS_IMAGE_SOURCE));
            mSelectModel.setImage(intent.getStringExtra(Constants.INTENT_NEWS_IMAGE_URL));
        }

        // Read from cache for viewpager data
        mDailyNewsModel = DataCache.getInstance().getDailyNewsModel(dateKey);
        mNewsNum = mDailyNewsModel.getNewsList().size();

        // Set up ViewPager and backing adapter
        mAdapter = new DetailPagerAdapter(getSupportFragmentManager(), mNewsNum, mDailyNewsModel);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setPageMargin((int) getResources().getDimension(R.dimen.news_detail_pager_margin));
        mPager.setOffscreenPageLimit(2);
        mPager.setOnPageChangeListener(mPageChangeListener);

        // Set animation
        //        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        //        mPager.setPageTransformer(true, new DepthPageTransformer());

        // Enable some additional newer visibility and ActionBar features to create a more
        // immersive photo viewing experience

        // Hide title text and set home as up
        getActionBar().setIcon(R.drawable.topbar_icon);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33000000")));

        // Set the title to empty char
        getActionBar().setTitle("");

        mActionBarDrawable = new ColorDrawable(Color.parseColor("#ff33b5e5"));
        mActionBarDrawable.setAlpha(0);
        //        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        getActionBar().setBackgroundDrawable(mActionBarDrawable);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mActionBarDrawable.setCallback(mDrawableCallback);
        }
        // Hide and show the ActionBar as the visibility changes
        mPager.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int vis) {
                if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
                    getActionBar().hide();
                } else {
                    getActionBar().show();
                }
            }
        });

        // Start low profile mode and hide ActionBar
        //        mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        //        getActionBar().hide();

        // Set the current item based on the extra passed in to this activity
        int extraCurrentItem = getIntent().getIntExtra(Constants.INTENT_NEWS_INDEX, -1);
        //        Log.e(TAG, "==pageindex==" + extraCurrentItem);
        if (extraCurrentItem != -1) {
            mPager.setCurrentItem(extraCurrentItem);
        } else {
            ArrayList<NewsModel> list = (ArrayList<NewsModel>) mDailyNewsModel.getNewsList();
            extraCurrentItem = getIndexById(list, mSelectModel.getId());
            //            Log.e(TAG, "==pageindex_new==" + extraCurrentItem);
            if (extraCurrentItem != -1) {
                mPager.setCurrentItem(extraCurrentItem);
            }
        }
    }

    private int getIndexById(List<NewsModel> list, int id) {
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                index = i;
                return index;
            }
        }
        return index;
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
        private final DailyNewsModel mDailyNewsModel;

        public DetailPagerAdapter(FragmentManager fm, int size, DailyNewsModel model) {
            super(fm);
            // TODO Auto-generated constructor stub
            mSize = size;
            mDailyNewsModel = model;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mSize;
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            return NewsDetailFragment.newInstance(mDailyNewsModel.getNewsList().get(position));
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

    private Drawable.Callback mDrawableCallback = new Drawable.Callback() {
        @SuppressLint("NewApi")
        @Override
        public void invalidateDrawable(Drawable who) {
            getActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
        }
    };

    public void setActionBarAlpha(int alpha) {
        mActionBarDrawable.setAlpha(alpha);
    }

    @SuppressLint("NewApi")
    public void setActionBarTitle(String title) {
        if (title != null) {
            getActionBar().setTitle(title);
        }
    }

    private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {

        @SuppressLint("NewApi")
        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            NewsModel model = mDailyNewsModel.getNewsList().get(position);
            String curTitle = getActionBar().getTitle().toString();
            if (!curTitle.equals("")) {
                getActionBar().setTitle(model.getTitle());
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // TODO Auto-generated method stub

        }
    };
}
