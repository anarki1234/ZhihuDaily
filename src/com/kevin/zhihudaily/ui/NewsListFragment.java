package com.kevin.zhihudaily.ui;

import org.taptwo.android.widget.CircleFlowIndicator;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kevin.zhihudaily.R;
import com.kevin.zhihudaily.http.ZhihuDataService;
import com.kevin.zhihudaily.http.ZhihuRequest;
import com.kevin.zhihudaily.model.LatestNewsModel;

public class NewsListFragment extends Fragment {

    protected static final String TAG = "NewsListFragment";

    private View mRootView;

    private ListView mListView;
    private NewsListAdapter mListAdpater;

    private View mHeaderView;

    private HeaderViewFlow mViewFlow;

    private CircleFlowIndicator mIndicator;

    private TopStoryAdapter mFlowAdapter;

    private RestAdapter mRestAdapter;
    private ZhihuDataService mDataService;

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
        mViewFlow = (HeaderViewFlow) mHeaderView.findViewById(R.id.viewflow);
        mViewFlow.setAdapter(mFlowAdapter);

        mIndicator = (CircleFlowIndicator) mHeaderView.findViewById(R.id.viewflowindic);
        mViewFlow.setFlowIndicator(mIndicator);

        mListView = (ListView) mRootView.findViewById(R.id.content_list);
        mListView.addHeaderView(mHeaderView);

        mListAdpater = new NewsListAdapter(getActivity());
        mListView.setAdapter(mListAdpater);
        mViewFlow.setListView(mListView);

        // request latest news
        mRestAdapter = new RestAdapter.Builder().setEndpoint(ZhihuRequest.BASE_URL).build();
        mDataService = mRestAdapter.create(ZhihuDataService.class);

        requestLatestNews();
    }

    private void requestLatestNews() {
        Callback<LatestNewsModel> callback = new Callback<LatestNewsModel>() {

            @Override
            public void failure(RetrofitError arg0) {
                // TODO Auto-generated method stub
                Log.d(TAG, "==failure==" + arg0);
            }

            @Override
            public void success(LatestNewsModel arg0, Response arg1) {
                // TODO Auto-generated method stub
                Log.d(TAG, "==success==" + arg0);
            }
        };

        mDataService.getLastestNews(callback);
    }
}
