package com.kevin.zhihudaily.ui;

import org.taptwo.android.widget.ViewFlow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class HeaderViewFlow extends ViewFlow {

    private ListView mListView;

    public HeaderViewFlow(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public HeaderViewFlow(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public void setListView(ListView listView) {
        mListView = listView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (mListView != null) {
            mListView.requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int h, int v, int oldh, int oldv) {
        // TODO Auto-generated method stub
        super.onScrollChanged(h, v, oldh, oldv);
        if (mListView != null) {
            mListView.requestDisallowInterceptTouchEvent(false);
        }
    }

}
