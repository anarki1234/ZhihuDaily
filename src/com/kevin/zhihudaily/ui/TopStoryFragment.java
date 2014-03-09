package com.kevin.zhihudaily.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kevin.zhihudaily.R;

public class TopStoryFragment extends Fragment {

    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mRootView = inflater.inflate(R.layout.fragment_top_story, container, false);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();

        mRootView = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

        // set up views
        initViews();
    }

    public void initViews() {
        if (mRootView == null) {
            return;
        }
    }
}
