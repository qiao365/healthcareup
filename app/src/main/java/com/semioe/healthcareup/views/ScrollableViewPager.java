package com.semioe.healthcareup.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ScrollableViewPager extends ViewPager {

	private static final String TAG = ScrollableViewPager.class.getSimpleName();

	private boolean scrollable = false;

	public ScrollableViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollableViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return scrollable && super.onInterceptTouchEvent(arg0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return scrollable && super.onTouchEvent(arg0);
	}
}
