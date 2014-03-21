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
    String html = "<div class=\"main-wrap content-wrap\">\n<div class=\"headline\">\n\n<div class=\"img-place-holder\"></div>\n\n\n</div>\n<div class=\"content-inner\">\n\n\n\n\n<div class=\"question\">\n<h2 class=\"question-title\">���ǵ������ֺò���ʲô���������Է���һ����</h2>\n\n<div class=\"answer\">\n\n<div class=\"meta\">\n<img class=\"avatar\" src=\"http://p1.zhimg.com/02/fc/02fcc5074_is.jpg\">\n<span class=\"author\">ŵŵ����</span>\n</div>\n\n<div class=\"content\">\n<p>һֱ��׽�� 100 ���ռ��ķ�ʽ��</p>\r\n<p>�����ռ��᲻̫�����ˡ�</p>\r\n<p><img src=\"http://p3.zhimg.com/52/94/52946266f5ddd4ade2aab4600aef2541_m.jpg\" alt=\"\" /></p>\r\n<p>���̼���</p>\r\n<p>һ��ͬѧ�̵ģ������ͼ�����ըһ�¡�</p>\r\n<p>�����������ͷŻ����˽ǣ���ը���ļ��������¹���ͬʱ���кõĲʽ�Ģ�����ͺܶඹ�꽴����ˮ��</p>\r\n<p>��֮��Ͳ������ˣ��ⶹ�꽴�͹����ˣ�ֻ��һ���ǣ���Ȼ�����͵�ζ��</p>\r\n<p>�ո�ˮ����ǰ�����δС�</p>\r\n<p>��һ������ʱ��������Ƥָ�����ˡ�</p>\r\n<p>�����д��ͼ�</p>\r\n<p>����Ƚ��鷳�ˡ���ΪҪ�����͡�</p>\r\n<p><img src=\"http://p2.zhimg.com/08/a6/08a612a811a2a6714ec15d170b25c76b_m.jpg\" alt=\"\" /></p>\r\n<p>��ɫ���Ͱ������͵�С�а��ɻ�ɫ��Ȼ��������ֻ���͡�</p>\r\n<p>���������ǰѼ�����ˡ��������͵�ˮ�տ������Ž�ȥ���ػ�Ǹ��ӣ���ˮ�����°Ѽ����죬�����Ӽ���ͷ��Żᱣ��Ѫ˿���㶫�İ��м������������ġ���ˮ�¶Ƚ����������̳�����������տ�ˮ��������ˮ������ 3,4 �β��ͺ��ˡ��̳������п顣</p>\r\n<p>Ȼ���أ��н����飬�Ž��ոհ��õĴ����ͬʱ�����Լ���ζ���ϳ齴�����ǡ�</p>\r\n<p><img src=\"http://p2.zhimg.com/61/e6/61e697cb4e73c8cfeba6f266087342b1_m.jpg\" alt=\"\" /></p>\r\n<p>��֭�����ˣ����ܵ������ϡ�</p>\r\n<p>�����ͦ���ģ������ܷŻ���ֱ���󣬻𿪿�ͣͣ�����ᡣ���а������һ��е�ζ����ͷ����ָ���ﶼ�ǡ�</p>\r\n<p><strong>��������й���Ը�������һ�������Ĳˣ���Ҫ������������Ȣ�ˣ����ټǵð���ϴ�롣</strong></p>\n</div>\n</div>\n\n\n<div class=\"view-more\"><a href=\"http://www.zhihu.com/question/20191205\">�鿴֪������<span class=\"js-question-holder\"></span></a></div>\n\n</div>\n\n\n</div>\n</div>";

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
