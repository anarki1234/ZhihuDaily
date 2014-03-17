package com.kevin.zhihudaily.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MainListView extends PinnedSectionListView {

	private float xDistance, yDistance, xLast, yLast;

	public MainListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();

			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;

			if (xDistance > yDistance) {
				return false; // 表示向下传递事件
			}
		}

		return super.onInterceptTouchEvent(ev);
	}

}
