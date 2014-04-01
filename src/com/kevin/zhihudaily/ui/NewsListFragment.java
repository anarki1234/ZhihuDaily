package com.kevin.zhihudaily.ui;

import org.apache.http.Header;
import org.json.JSONObject;
import org.taptwo.android.widget.CircleFlowIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.kevin.zhihudaily.Constants;
import com.kevin.zhihudaily.R;
import com.kevin.zhihudaily.db.DataBaseManager;
import com.kevin.zhihudaily.db.DataCache;
import com.kevin.zhihudaily.db.DataService;
import com.kevin.zhihudaily.http.ZhihuRequest;
import com.kevin.zhihudaily.model.DailyNewsModel;
import com.kevin.zhihudaily.ui.NewsListAdapter.ListItem;
import com.loopj.android.http.JsonHttpResponseHandler;

public class NewsListFragment extends Fragment {

    protected static final String TAG = "NewsListFragment";

    private View mRootView;

    private MainListView mListView;
    private NewsListAdapter mListAdpater;

    private View mHeaderView;

    private HeaderViewFlow mViewFlow;

    private CircleFlowIndicator mIndicator;

    private TopStoryAdapter mFlowAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mRootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        return mRootView;
        // return super.onCreateView(inflater, container, savedInstanceState);
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

        mListView = (MainListView) mRootView.findViewById(R.id.content_list);
        mListView.addHeaderView(mHeaderView);

        mListAdpater = new NewsListAdapter(getActivity());
        mListView.setAdapter(mListAdpater);
        mViewFlow.setListView(mListView);
        mListView.setOnItemClickListener(new ListItemClickListener());
        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

            }
        });

        // request latest news

        requestLatestNews();
    }

    private void requestLatestNews() {
        ZhihuRequest.get(ZhihuRequest.GET_LATEST_NEWS, null, new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, e, errorResponse);
                Log.e(TAG, "==onFailure==" + errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, responseBody);
                Log.d(TAG, "==onSuccess==" + responseBody);
                Gson gson = new Gson();
                DailyNewsModel model = gson.fromJson(responseBody, DailyNewsModel.class);

                mListAdpater.updateList(model);

                mFlowAdapter.updateList(model.getTopStories());

                int newTimeStamp = Integer.valueOf(model.getNewsList().get(0).getGa_prefix());

                // Add to cache and write to db
                DataCache.getInstance().addDailyCache(model.getDate(), model);

                if (DataBaseManager.getInstance().checkDataExpire(newTimeStamp)) {
                    // Write to db
                    Intent intent = new Intent(getActivity(), DataService.class);
                    intent.putExtra(Constants.INTENT_CACHE_ID, model.getDate());
                    intent.putExtra(Constants.INTENT_ACTION_TYPE, Constants.ACTION_WRITE_DAILY_NEWS);
                    getActivity().startService(intent);

                    // update timestamp
                    DataBaseManager.getInstance().setDataTimeStamp(newTimeStamp);
                }

            }

        });
    }

    private class ListItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            //            ListItem item = (ListItem) mListAdpater.getItem(position);
            ListItem item = (ListItem) parent.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_NUM, item.getSectionSize());
            intent.putExtra(Constants.INTENT_NEWS_INDEX, item.getIndexOfDay());
            intent.putExtra(Constants.INTENT_NEWS_DATE, item.getDate());
            Log.d(TAG, "==index=" + item.getIndexOfDay() + "==pos=" + position);

            startActivity(intent);
        }
    }
}
