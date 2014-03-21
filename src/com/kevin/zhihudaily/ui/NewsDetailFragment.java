package com.kevin.zhihudaily.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kevin.zhihudaily.Constants;
import com.kevin.zhihudaily.R;
import com.kevin.zhihudaily.db.DataCache;
import com.kevin.zhihudaily.model.NewsModel;
import com.squareup.picasso.Picasso;

public class NewsDetailFragment extends Fragment {
    private View mRootView;
    private ImageView mImageView;
    private ExWebView mWebView;
    private NewsModel mNewsModel;
    String html = "<div class=\"main-wrap content-wrap\">\n<div class=\"headline\">\n\n<div class=\"img-place-holder\"></div>\n\n\n</div>\n<div class=\"content-inner\">\n\n\n\n\n<div class=\"question\">\n<h2 class=\"question-title\">你们的最拿手好菜是什么？做法可以分享一下吗？</h2>\n\n<div class=\"answer\">\n\n<div class=\"meta\">\n<img class=\"avatar\" src=\"http://p1.zhimg.com/02/fc/02fcc5074_is.jpg\">\n<span class=\"author\">诺诺粒粒</span>\n</div>\n\n<div class=\"content\">\n<p>一直在捉摸 100 种烧鸡的方式。</p>\r\n<p>各种烧鸡翅不太想提了。</p>\r\n<p><img src=\"http://p3.zhimg.com/52/94/52946266f5ddd4ade2aab4600aef2541_m.jpg\" alt=\"\" /></p>\r\n<p>大盘鸡。</p>\r\n<p>一个同学教的，土豆和鸡块先炸一下。</p>\r\n<p>捞起来，热油放花椒八角，把炸过的鸡和土豆下锅。同时放切好的彩椒蘑菇，和很多豆瓣酱，加水。</p>\r\n<p>我之后就不用盐了，光豆瓣酱就够咸了，只加一点糖，孜然和麻油调味。</p>\r\n<p>收干水出锅前切两段葱。</p>\r\n<p>第一次做的时候，削土豆皮指甲劈了。</p>\r\n<p>啊还有葱油鸡</p>\r\n<p>这个比较麻烦了。因为要熬葱油。</p>\r\n<p><img src=\"http://p2.zhimg.com/08/a6/08a612a811a2a6714ec15d170b25c76b_m.jpg\" alt=\"\" /></p>\r\n<p>用色拉油把绿油油的小葱熬成黄色。然后滤渣，只用油。</p>\r\n<p>接下来就是把鸡煮好了。加了香油的水烧开，鸡放进去，关火盖盖子，用水的余温把鸡烫熟，这样子鸡骨头里才会保留血丝。广东的白切鸡都是这样做的。等水温度降下来，鸡捞出来放盘子里，烧开水，鸡再入水。反复 3,4 次差不多就好了。捞出来鸡切块。</p>\r\n<p>然后呢，葱姜切碎，放进刚刚熬好的葱油里，同时根据自己口味加老抽酱油盐糖。</p>\r\n<p><img src=\"http://p2.zhimg.com/61/e6/61e697cb4e73c8cfeba6f266087342b1_m.jpg\" alt=\"\" /></p>\r\n<p>酱汁做好了，就淋到鸡块上。</p>\r\n<p>这个菜挺烦的，鸡不能放火上直接煮，火开开停停很讨厌。还有熬完葱油一身葱的味道，头发里指甲里都是。</p>\r\n<p><strong>所以如果有姑娘愿意给你烧一个这样的菜，不要求你吃完把她给娶了，至少记得帮着洗碗。</strong></p>\n</div>\n</div>\n\n\n<div class=\"view-more\"><a href=\"http://www.zhihu.com/question/20191205\">查看知乎讨论<span class=\"js-question-holder\"></span></a></div>\n\n</div>\n\n\n</div>\n</div>";

    public static NewsDetailFragment newInstance(NewsModel model) {
        final NewsDetailFragment detailFragment = new NewsDetailFragment();

        final Bundle args = new Bundle();
        // add data to cache
        int id = model.getId();
        DataCache.getInstance().addNewsCache(id, model);
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
        mNewsModel = DataCache.getInstance().getNewsCache(arg.getInt(Constants.INTENT_CACHE_ID));
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

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

        // init up views
        initViews();
    }

    private void initViews() {
        if (mRootView == null) {
            return;
        }

        mImageView = (ImageView) mRootView.findViewById(R.id.iv_image);

        mWebView = (ExWebView) mRootView.findViewById(R.id.wv_webview);

        // set up views
        setupViews();
    }

    private void setupViews() {
        Picasso.with(getActivity()).load(mNewsModel.getImage()).fit().into(mImageView);
        String htmlData = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />" + html;
        // lets assume we have /assets/style.css file
        mWebView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "UTF-8", null);
    }
}
