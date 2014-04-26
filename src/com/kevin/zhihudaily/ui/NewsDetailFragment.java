package com.kevin.zhihudaily.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kevin.zhihudaily.Constants;
import com.kevin.zhihudaily.R;
import com.kevin.zhihudaily.ZhihuDailyApplication;
import com.kevin.zhihudaily.db.DataCache;
import com.kevin.zhihudaily.db.DataService;
import com.kevin.zhihudaily.model.NewsModel;
import com.kevin.zhihudaily.view.ExScrollView;
import com.kevin.zhihudaily.view.ExWebView;
import com.squareup.picasso.Picasso;

public class NewsDetailFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NewsDetailFragment";
    private View mRootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ExScrollView mScrollView;

    private ImageView mImageView;
    private TextView mTitleTextView;
    private TextView mSourceTextView;
    private ExWebView mWebView;
    private NewsModel mNewsModel;

    private NewsDetailReadyReceiver mDetailReadyReceiver;

    String html = "<div class=\"main-wrap content-wrap\">\n<div class=\"headline\">\n\n<div class=\"img-place-holder\"></div>\n\n\n</div>\n<div class=\"content-inner\">\n\n\n\n\n<div class=\"question\">\n<h2 class=\"question-title\">���ǵ������ֺò���ʲô���������Է���һ����</h2>\n\n<div class=\"answer\">\n\n<div class=\"meta\">\n<img class=\"avatar\" src=\"http://p1.zhimg.com/02/fc/02fcc5074_is.jpg\">\n<span class=\"author\">ŵŵ����</span>\n</div>\n\n<div class=\"content\">\n<p>һֱ��׽�� 100 ���ռ��ķ�ʽ��</p>\r\n<p>�����ռ��᲻̫�����ˡ�</p>\r\n<p><img src=\"http://p3.zhimg.com/52/94/52946266f5ddd4ade2aab4600aef2541_m.jpg\" alt=\"\" /></p>\r\n<p>���̼���</p>\r\n<p>һ��ͬѧ�̵ģ������ͼ�����ըһ�¡�</p>\r\n<p>�����������ͷŻ����˽ǣ���ը��ļ��������¹�ͬʱ���кõĲʽ�Ģ�����ͺܶඹ�꽴����ˮ��</p>\r\n<p>��֮��Ͳ������ˣ��ⶹ�꽴�͹����ˣ�ֻ��һ���ǣ���Ȼ�����͵�ζ��</p>\r\n<p>�ո�ˮ����ǰ�����δС�</p>\r\n<p>��һ������ʱ��������Ƥָ�����ˡ�</p>\r\n<p>�����д��ͼ�</p>\r\n<p>����Ƚ��鷳�ˡ���ΪҪ�����͡�</p>\r\n<p><img src=\"http://p2.zhimg.com/08/a6/08a612a811a2a6714ec15d170b25c76b_m.jpg\" alt=\"\" /></p>\r\n<p>��ɫ���Ͱ������͵�С�а��ɻ�ɫ��Ȼ������ֻ���͡�</p>\r\n<p>���������ǰѼ�����ˡ��������͵�ˮ�տ������Ž�ȥ���ػ�Ǹ��ӣ���ˮ�����°Ѽ����죬�����Ӽ���ͷ��Żᱣ��Ѫ˿���㶫�İ��м������������ġ���ˮ�¶Ƚ����������̳�����������տ�ˮ��������ˮ������ 3,4 �β��ͺ��ˡ��̳������п顣</p>\r\n<p>Ȼ���أ��н����飬�Ž�ոհ��õĴ����ͬʱ����Լ���ζ���ϳ齴�����ǡ�</p>\r\n<p><img src=\"http://p2.zhimg.com/61/e6/61e697cb4e73c8cfeba6f266087342b1_m.jpg\" alt=\"\" /></p>\r\n<p>��֭�����ˣ����ܵ������ϡ�</p>\r\n<p>�����ͦ���ģ������ܷŻ���ֱ���󣬻𿪿�ͣͣ�����ᡣ���а������һ��е�ζ����ͷ����ָ���ﶼ�ǡ�</p>\r\n<p><strong>��������й���Ը�������һ������Ĳˣ���Ҫ�����������Ȣ�ˣ����ټǵð���ϴ�롣</strong></p>\n</div>\n</div>\n\n\n<div class=\"view-more\"><a href=\"http://www.zhihu.com/question/20191205\">�鿴֪������<span class=\"js-question-holder\"></span></a></div>\n\n</div>\n\n\n</div>\n</div>";

    public static NewsDetailFragment newInstance(NewsModel model) {
        final NewsDetailFragment detailFragment = new NewsDetailFragment();

        final Bundle args = new Bundle();
        // add data to cache
        int id = model.getId();
        //        DataCache.getInstance().addNewsCache(id, model);
        args.putString(Constants.INTENT_NEWS_DATE, model.getDate());
        args.putInt(Constants.INTENT_CACHE_ID, id);
        detailFragment.setArguments(args);

        return detailFragment;
    }

    /**
     * Empty constructor as per the Fragment documentation
     */
    public NewsDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        mNewsModel = DataCache.getInstance().getNewsModelByDateAndID(arg.getString(Constants.INTENT_NEWS_DATE),
                arg.getInt(Constants.INTENT_CACHE_ID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mRootView = inflater.inflate(R.layout.fragment_news_detail, container, false);

        return mRootView;
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();

        mRootView = null;
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mDetailReadyReceiver);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

        // init up views
        initViews();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // set up views
        //        setupViews();
    }

    private void initViews() {
        if (mRootView == null) {
            return;
        }

        mDetailReadyReceiver = new NewsDetailReadyReceiver();
        IntentFilter dataIntentFilter = new IntentFilter(Constants.ACTION_NOTIFY_NEWS_DETAIL_READY);
        dataIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mDetailReadyReceiver, dataIntentFilter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mScrollView = (ExScrollView) mRootView.findViewById(R.id.scroll_view);
        mScrollView.setOnScrollChangedListener(mOnScrollChangedListener);

        mImageView = (ImageView) mRootView.findViewById(R.id.iv_image);
        mTitleTextView = (TextView) mRootView.findViewById(R.id.tv_news_title);
        mSourceTextView = (TextView) mRootView.findViewById(R.id.tv_news_image_source);

        mWebView = (ExWebView) mRootView.findViewById(R.id.wv_webview);
        mWebView.setBackgroundColor(0);
        mWebView.getSettings().setAppCacheEnabled(true);
        String str = getActivity().getApplicationContext().getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(str);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(1);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);

        // set up views
        setupViews();

        mRootView.setOnClickListener((OnClickListener) getActivity());
    }

    private void setupViews() {
        String url = mNewsModel.getImage();
        if (url != null && !url.isEmpty()) {
            Picasso.with(getActivity()).load(mNewsModel.getImage()).placeholder(R.drawable.image_top_default).fit()
                    .centerCrop().into(mImageView);
        }

        mTitleTextView.setText(mNewsModel.getTitle());
        mSourceTextView.setText(mNewsModel.getImage_source());

        updateNewsDetail();

        //        String htmlData = optimizeHtml(html);
        //        // lets assume we have /assets/style.css file
        //        mWebView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "UTF-8", null);
    }

    private String optimizeHtml(String body) {
        String html = "";
        String tag = "";
        tag += "large";
        html = String
                .format("<!doctype html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width,user-scalable=no\"><link href=\"news_qa.min.css\" rel=\"stylesheet\"><style>.headline .img-place-holder{height:0}</style><script src=\"img_replace.js\"></script></head><body className=\"%s\">",
                        tag);
        html += body;

        //        String str3 = String
        //                .format("<script>window.news_id=%s;</script><script src=\"http://daily.zhihu.com/js/zepto.min.js\"></script><script src=\"http://news-at.zhihu.com/js/hot-comments.ios.3.js\"></script>",
        //                        arrayOfObject2);
        //        html += str3;

        html += "</body></html>";

        return html;
    }

    private void updateNewsDetail() {
        String body = mNewsModel.getBody();
        if (body != null) {
            updateWebView(body);
        } else {
            if (ZhihuDailyApplication.sIsConnected) {
                //                requestNewsDetail();
                Intent intent = new Intent(getActivity(), DataService.class);
                intent.putExtra(Constants.INTENT_ACTION_TYPE, Constants.ACTION_GET_NEWS_DETAIL);
                intent.putExtra(Constants.INTENT_NEWS_DATE, mNewsModel.getDate());
                intent.putExtra(Constants.INTENT_NEWS_ID, mNewsModel.getId());
                getActivity().startService(intent);
            } else {
                readNewsDetailFromDB();
            }
        }
    }

    private void readNewsDetailFromDB() {
        // Read db data
        Intent intent = new Intent(getActivity(), DataService.class);
        intent.putExtra(Constants.INTENT_ACTION_TYPE, Constants.ACTION_READ_NEWS_DEATIL);
        intent.putExtra(Constants.INTENT_NEWS_DATE, mNewsModel.getDate());
        intent.putExtra(Constants.INTENT_NEWS_ID, mNewsModel.getId());
        getActivity().startService(intent);
    }

    private void updateWebView(String body) {
        String htmldata = optimizeHtml(body);
        mWebView.loadDataWithBaseURL("file:///android_asset/", htmldata, "text/html", "UTF-8", null);
    }

    private class AsyncRunnable implements Runnable {
        private NewsModel model;

        private AsyncRunnable(NewsModel model) {
            this.model = model;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            String htmldata = optimizeHtml(mNewsModel.getBody());
            mWebView.loadDataWithBaseURL("file:///android_asset/", htmldata, "text/html", "UTF-8", null);
        }

    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    private class NewsDetailReadyReceiver extends BroadcastReceiver {

        private NewsDetailReadyReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String date = intent.getStringExtra(Constants.INTENT_NEWS_DATE);
            int id = intent.getIntExtra(Constants.INTENT_NEWS_ID, -1);
            if (id == mNewsModel.getId()) {
                String body = DataCache.getInstance().getNewsBodyByDateAndID(date, id);
                mNewsModel.setBody(body);

                // Update ui
                updateWebView(body);

                // Write to db
                //                Intent dbintent = new Intent(getActivity(), DataService.class);
                //                dbintent.putExtra(Constants.INTENT_NEWS_ID, id);
                //                dbintent.putExtra(Constants.INTENT_NEWS_BODY, body);
                //                dbintent.putExtra(Constants.INTENT_ACTION_TYPE, Constants.ACTION_WRITE_NEWS_DEATIL);
                //                getActivity().startService(dbintent);
            }
        }

    }

    private ExScrollView.OnScrollChangedListener mOnScrollChangedListener = new ExScrollView.OnScrollChangedListener() {
        @SuppressLint("NewApi")
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            final int headerHeight = mImageView.getHeight()
                    - ((NewsDetailActivity) getActivity()).getActionBar().getHeight();

            final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
            final int newAlpha = (int) (ratio * 255);
            ((NewsDetailActivity) getActivity()).setActionBarAlpha(newAlpha);

            if (newAlpha >= 240) {
                ((NewsDetailActivity) getActivity()).setActionBarTitle(mNewsModel.getTitle());
            } else {
                ((NewsDetailActivity) getActivity()).setActionBarTitle("");
            }
        }
    };

}
