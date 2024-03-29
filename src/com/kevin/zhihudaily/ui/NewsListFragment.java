package com.kevin.zhihudaily.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.taptwo.android.widget.CircleFlowIndicator;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.baidu.mobstat.StatService;
import com.kevin.zhihudaily.Constants;
import com.kevin.zhihudaily.DebugLog;
import com.kevin.zhihudaily.R;
import com.kevin.zhihudaily.ZhihuDailyApplication;
import com.kevin.zhihudaily.db.DataBaseManager;
import com.kevin.zhihudaily.db.DataCache;
import com.kevin.zhihudaily.db.DataService;
import com.kevin.zhihudaily.model.DailyNewsModel;
import com.kevin.zhihudaily.model.NewsModel;
import com.kevin.zhihudaily.ui.NewsListAdapter.ListItem;
import com.kevin.zhihudaily.view.HeaderViewFlow;
import com.kevin.zhihudaily.view.TopStoryAdapter;

public class NewsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View mRootView;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MainListView mListView;
    private NewsListAdapter mListAdpater;

    private View mHeaderView;
    private HeaderViewFlow mViewFlow;

    // Foot view for loading more list items
    private View mFooterView = null;

    /**
     * A mark for reset all list data
     */
    private boolean mIsResetList = false;

    private CircleFlowIndicator mIndicator;

    private TopStoryAdapter mFlowAdapter;

    private int mHeaderHeight;

    private DataReadyReceiver mDataReadyReceiver;

    private Date mTodayDate;
    private String mTodayDateString;
    private String mIndexDate;
    private int preDays = 0;

    NotificationCompat.Builder mNotifyBuilder;
    NotificationManager mNotificationManager;
    private int mNotifyID = 0x0007;

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

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mDataReadyReceiver);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

        // set up views()
        initViews();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initViews() {
        if (mRootView == null) {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        mTodayDate = calendar.getTime();

        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyBuilder = new NotificationCompat.Builder(getActivity()).setSmallIcon(R.drawable.push_icon)
                .setContentTitle(getString(R.string.data_cache))
                .setContentText(getString(R.string.data_cache_inprocess)).setAutoCancel(true);

        mDataReadyReceiver = new DataReadyReceiver();
        IntentFilter dataIntentFilter = new IntentFilter(Constants.ACTION_NOTIFY_DAILY_NEWS_READY);
        dataIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mDataReadyReceiver, dataIntentFilter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_list_header, null);

        mFlowAdapter = new TopStoryAdapter(getActivity());
        mViewFlow = (HeaderViewFlow) mHeaderView.findViewById(R.id.viewflow);
        mViewFlow.setAdapter(mFlowAdapter, 5);
        mViewFlow.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                DebugLog.e("==OnItemClickListener== arg2:" + arg2);
            }
        });

        mIndicator = (CircleFlowIndicator) mHeaderView.findViewById(R.id.viewflowindic);
        mViewFlow.setFlowIndicator(mIndicator);

        mListView = (MainListView) mRootView.findViewById(R.id.content_list);
        mListView.addHeaderView(mHeaderView);
        mListView.setOnScrollListener(mOnScrollListener);

        mFooterView = getActivity().getLayoutInflater().inflate(R.layout.list_footer, null);
        mFooterView.setVisibility(View.GONE);

        mListAdpater = new NewsListAdapter(getActivity());
        mListView.setAdapter(mListAdpater);
        mViewFlow.setListView(mListView);
        mListView.setOnItemClickListener(new ListItemClickListener());

        // request latest news
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        String todayDate = formatter.format(mTodayDate);
        mIndexDate = todayDate;
        mTodayDateString = todayDate;
        requestNewsList(todayDate, true);

    }

    private void requestNewsList(String date, boolean isToday) {
        if (ZhihuDailyApplication.sIsConnected) {
            DebugLog.d("==NET-Mode==" + date);
            if (isToday) {
                Intent intent = new Intent(getActivity(), DataService.class);
                intent.putExtra(Constants.INTENT_ACTION_TYPE, Constants.ACTION_GET_TODAY_NEWS);
                getActivity().startService(intent);
            } else {
                Intent intent = new Intent(getActivity(), DataService.class);
                intent.putExtra(Constants.INTENT_ACTION_TYPE, Constants.ACTION_GET_DAILY_NEWS);
                intent.putExtra(Constants.INTENT_NEWS_DATE, date);
                getActivity().startService(intent);
            }

        } else {
            DebugLog.d("==DB-Mode==" + date);
            if (isToday) {
                readLastestNewsFromDB();
            } else {
                date = getPreDateString(date, "yyyyMMdd");
                DebugLog.d("==PreDate==" + date);
                readNewsFromDBByDate(date);
            }
        }

    }

    private void readLastestNewsFromDB() {
        // Read db data
        Intent intent = new Intent(getActivity(), DataService.class);
        intent.putExtra(Constants.INTENT_ACTION_TYPE, Constants.ACTION_READ_LASTEST_NEWS);
        //        intent.putExtra(Constants.INTENT_NEWS_DATE, date);
        getActivity().startService(intent);
    }

    private void readNewsFromDBByDate(String date) {
        // Read db data
        Intent intent = new Intent(getActivity(), DataService.class);
        intent.putExtra(Constants.INTENT_ACTION_TYPE, Constants.ACTION_READ_DAILY_NEWS);
        intent.putExtra(Constants.INTENT_NEWS_DATE, date);
        getActivity().startService(intent);
    }

    private class ListItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            // ListItem item = (ListItem) mListAdpater.getItem(position);
            ListItem item = (ListItem) parent.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_NUM, item.getSectionSize());
            intent.putExtra(Constants.INTENT_NEWS_INDEX, item.getIndexOfDay());
            intent.putExtra(Constants.INTENT_NEWS_DATE, item.getDate());
            DebugLog.d("==index=" + item.getIndexOfDay() + "==pos=" + position);

            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        mIsResetList = true;
        requestNewsList(mTodayDateString, true);
    }

    public String calendarToString(Calendar calendar, String dateFormat) {
        if (calendar == null || dateFormat == null) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.CHINA);
        return simpleDateFormat.format(calendar.getTime());
    }

    public Calendar stringToCalendar(String dateString, String dateFormat) {
        if (dateString == null || dateFormat == null) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.CHINA);
        try {
            simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
        return simpleDateFormat.getCalendar();
    }

    private String getPreDateString(String dateString, String dateFormat) {
        String preDataString = null;
        if (dateString == null || dateFormat == null) {
            return null;
        }

        Calendar calendar = stringToCalendar(dateString, dateFormat);
        calendar.add(Calendar.DATE, -1);

        preDataString = calendarToString(calendar, dateFormat);
        return preDataString;
    }

    private OnScrollListener mOnScrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // TODO Auto-generated method stub
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                    mFooterView.setVisibility(View.VISIBLE);
                    mListView.setSelection(view.getCount() - 1);

                    // calculate date
                    //                    Calendar calendar = Calendar.getInstance();
                    //                    calendar.add(Calendar.DATE, 0 - preDays);
                    //                    String date = calendarToString(calendar, "yyyyMMdd");
                    String date = mIndexDate;

                    DebugLog.d("==preDate==" + date);
                    requestNewsList(date, false);
                    preDays++;
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // TODO Auto-generated method stub
            int visiblePostion = mListView.getFirstVisiblePosition();
            int newAlpha;
            if (visiblePostion == 0) {
                View c = mListView.getChildAt(0);
                int scrolly = -c.getTop() + mListView.getFirstVisiblePosition() * c.getHeight();
                // Log.e(TAG, "==onScrollStateChanged==  c.getTop()=" +
                // c.getTop() + "   c.getHeight()=" + c.getHeight()
                // + "  Pos=" + mListView.getFirstVisiblePosition());
                mHeaderHeight = mHeaderView.getHeight();
                final float ratio = (float) Math.min(Math.max(scrolly, 0), mHeaderHeight) / mHeaderHeight;
                newAlpha = (int) (ratio * 255);
                // Log.e(TAG, "==onScrollStateChanged==  Y=" + scrolly +
                // "   mHeaderHeight=" + mHeaderHeight + "  Alpha="
                // + newAlpha);
                ((MainActivity) getActivity()).setActionBarAlpha(newAlpha);

            } else {
                newAlpha = 255;
                //                ((MainActivity) getActivity()).setActionBarAlpha(newAlpha);
                ((MainActivity) getActivity()).setActionBarAlpha(0);
            }

        }

    };

    private class DataReadyReceiver extends BroadcastReceiver {
        // Prevents instantiation
        private DataReadyReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // String action = intent.getAction();
            int action_type = intent.getIntExtra(Constants.INTENT_ACTION_TYPE, -1);
            if (action_type != Constants.ACTION_START_OFFLINE_DOWNLOAD) {
                String date = intent.getStringExtra(Constants.EXTRA_CACHE_KEY);

                // update index date
                mIndexDate = date;
                DebugLog.d("==Index date==" + mIndexDate);

                DailyNewsModel model = DataCache.getInstance().getDailyNewsModel(date);

                updateNewsList(model);

            } else {
                int progress = intent.getIntExtra(Constants.EXTRA_PROGRESS_PROGRESS, -1);
                updateNotification(progress);
            }

        }

    }

    private void updateNewsList(DailyNewsModel model) {

        // Set SwipeRefreshLayout to stop
        mSwipeRefreshLayout.setRefreshing(false);

        // Hide Loading footer view
        mFooterView.setVisibility(View.GONE);

        // Add date to each news model
        String date = model.getDate();
        for (NewsModel news : model.getNewsList()) {
            news.setDate(date);
        }
        ArrayList<NewsModel> hotList = (ArrayList<NewsModel>) model.getTopStories();
        if (hotList != null) {
            for (NewsModel news : hotList) {
                news.setDate(date);
            }
        }

        if (mIsResetList) {
            ArrayList<DailyNewsModel> list = new ArrayList<DailyNewsModel>();
            list.add(model);
            mListAdpater.updateAllList(list);

            ArrayList<NewsModel> newslist = new ArrayList<NewsModel>();
            newslist.addAll(model.getTopStories());
            mFlowAdapter.updateAllList(newslist);

            mIsResetList = false;
        } else {
            mListAdpater.updateList(model);

            mFlowAdapter.updateList(model.getTopStories());
        }

        int newTimeStamp = Integer.valueOf(model.getNewsList().get(0).getGa_prefix());

        //        // Add to cache and write to db
        //        DataCache.getInstance().addDailyCache(model.getDate(), model);

        if (DataBaseManager.getInstance().checkDataExpire(newTimeStamp) >= 0) {
            // Write to db
            Intent intent = new Intent(getActivity(), DataService.class);
            intent.putExtra(Constants.INTENT_CACHE_ID, model.getDate());
            intent.putExtra(Constants.INTENT_ACTION_TYPE, Constants.ACTION_WRITE_DAILY_NEWS);
            getActivity().startService(intent);

            // Auto start data cache
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ZhihuDailyApplication.sNetworkType == ConnectivityManager.TYPE_WIFI) {
                        startDataCache(mTodayDateString);
                    }
                }
            }, 2000);
        }
    }

    private void startDataCache(String date) {
        Intent intent = new Intent(ZhihuDailyApplication.getInstance().getApplicationContext(), DataService.class);
        intent.putExtra(Constants.INTENT_ACTION_TYPE, Constants.ACTION_START_OFFLINE_DOWNLOAD);
        intent.putExtra(Constants.INTENT_NEWS_DATE, date);
        ZhihuDailyApplication.getInstance().getApplicationContext().startService(intent);

        // Show notification
        showNotification();
    }

    private void showNotification() {
        mNotifyBuilder.setProgress(100, 0, false);
        mNotificationManager.notify(mNotifyID, mNotifyBuilder.build());
    }

    private void updateNotification(int progress) {
        if (progress >= 100) {
            progress = 100;
            mNotifyBuilder.setContentText(getString(R.string.data_cache_complete));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    clearNotification();
                }
            }, 5000);
        }
        mNotifyBuilder.setProgress(100, progress, false);
        mNotificationManager.notify(mNotifyID, mNotifyBuilder.build());
    }

    private void clearNotification() {
        mNotificationManager.cancel(mNotifyID);
    }
}
