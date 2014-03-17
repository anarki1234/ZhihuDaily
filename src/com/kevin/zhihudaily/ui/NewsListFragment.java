package com.kevin.zhihudaily.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.taptwo.android.widget.CircleFlowIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.kevin.zhihudaily.R;
import com.kevin.zhihudaily.http.ZhihuRequest;
import com.kevin.zhihudaily.model.DailyNewsModel;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mRootView = inflater.inflate(R.layout.fragment_news_list, container,
				false);
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

		mHeaderView = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_list_header, null);

		mFlowAdapter = new TopStoryAdapter(getActivity());
		mViewFlow = (HeaderViewFlow) mHeaderView.findViewById(R.id.viewflow);
		mViewFlow.setAdapter(mFlowAdapter);

		mIndicator = (CircleFlowIndicator) mHeaderView
				.findViewById(R.id.viewflowindic);
		mViewFlow.setFlowIndicator(mIndicator);

		mListView = (MainListView) mRootView.findViewById(R.id.content_list);
		mListView.addHeaderView(mHeaderView);

		mListAdpater = new NewsListAdapter(getActivity());
		mListView.setAdapter(mListAdpater);
		mViewFlow.setListView(mListView);

		// request latest news

		requestLatestNews();
	}

	private void requestLatestNews() {
		ZhihuRequest.get(ZhihuRequest.GET_LATEST_NEWS, null,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Throwable e,
							JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, e, errorResponse);
						Log.e(TAG, "==onFailure==" + errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseBody) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, responseBody);
						Log.d(TAG, "==onSuccess==" + responseBody);
						Gson gson = new Gson();
						DailyNewsModel model = gson.fromJson(responseBody,
								DailyNewsModel.class);
						List<DailyNewsModel> list = new ArrayList<DailyNewsModel>();
						list.add(model);
						mListAdpater.updateList(list);
					}

					@Override
					protected Object parseResponse(String responseBody)
							throws JSONException {
						// TODO Auto-generated method stub
						return super.parseResponse(responseBody);
					}

				});
	}
}
