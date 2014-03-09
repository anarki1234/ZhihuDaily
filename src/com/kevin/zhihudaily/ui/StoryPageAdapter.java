package com.kevin.zhihudaily.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class StoryPageAdapter extends FragmentPagerAdapter {

    public StoryPageAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        TopStoryFragment fragment = null;
        switch (arg0) {
        default:
            fragment = new TopStoryFragment();
            break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 5;
    }

}
