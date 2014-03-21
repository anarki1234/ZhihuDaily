package com.kevin.zhihudaily.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kevin.zhihudaily.R;

public class NewsDetailFragment extends Fragment {
    private View mRootView;
    private ImageView mImageView;
    private ExWebView mWebView;

    public static NewsDetailFragment newInstance() {
        final NewsDetailFragment detailFragment = new NewsDetailFragment();

        final Bundle args = new Bundle();

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
        getArguments();
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

    }
}
