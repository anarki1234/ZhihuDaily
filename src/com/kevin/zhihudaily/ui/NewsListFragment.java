package com.kevin.zhihudaily.ui;

import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kevin.zhihudaily.R;

public class NewsListFragment extends Fragment {

    private View mRootView;

    private ListView mListView;
    private NewsListAdapter mListAdpater;

    private View mHeaderView;

    private ViewFlow mViewFlow;

    private CircleFlowIndicator mIndicator;

    private TopStoryAdapter mFlowAdapter;

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

        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_list_header, null);

        mFlowAdapter = new TopStoryAdapter(getActivity());
        mViewFlow = (ViewFlow) mHeaderView.findViewById(R.id.viewflow);
        mViewFlow.setAdapter(mFlowAdapter);

        mIndicator = (CircleFlowIndicator) mHeaderView.findViewById(R.id.viewflowindic);
        mViewFlow.setFlowIndicator(mIndicator);

        mListView = (ListView) mRootView.findViewById(R.id.content_list);
        mListView.addHeaderView(mHeaderView);

        mListAdpater = new NewsListAdapter(getActivity());
        mListView.setAdapter(mListAdpater);
    }
}
