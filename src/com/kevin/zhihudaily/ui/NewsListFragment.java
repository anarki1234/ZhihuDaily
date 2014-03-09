package com.kevin.zhihudaily.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.kevin.zhihudaily.R;
import com.viewpagerindicator.LinePageIndicator;

public class NewsListFragment extends Fragment {

    private View mRootView;

    private ListView mListView;
    private NewsListAdapter mListAdpater;

    private View mHeaderView;
    private ViewPager mPager;
    private LinePageIndicator mIndicator;
    private PagerAdapter mPageAdapter;

    private ViewFlipper mFlipper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mRootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        return mRootView;
        //        return super.onCreateView(inflater, container, savedInstanceState);
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

        // set up views()
        initViews();
    }

    private void initViews() {
        if (mRootView == null) {
            return;
        }

        mPageAdapter = new StoryPageAdapter(getFragmentManager());

        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_list_header, null);
        //        mPager = (ViewPager) mHeaderView.findViewById(R.id.pager);
        //        mPager.setAdapter(mPageAdapter);
        mFlipper = (ViewFlipper) mHeaderView.findViewById(R.id.vf_story_flipper);
        mFlipper.startFlipping();

        mIndicator = (LinePageIndicator) mHeaderView.findViewById(R.id.indicator);
        //        mIndicator.setViewPager(mPager);

        mListView = (ListView) mRootView.findViewById(R.id.content_list);
        mListView.addHeaderView(mHeaderView);

        mListAdpater = new NewsListAdapter(getActivity());
        mListView.setAdapter(mListAdpater);
    }
}
