package com.mathieucalba.yana.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;

public class GestureWebView extends WebView {

	private GestureDetectorCompat mGestureDetector;

	public GestureWebView(Context context) {
		super(context);
	}

	public GestureWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GestureWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressWarnings("deprecation")
	public GestureWebView(Context context, AttributeSet attrs, int defStyle, boolean privateBrowsing) {
		super(context, attrs, defStyle, privateBrowsing);
	}

	public void init(GestureDetector.OnGestureListener listener) {
		mGestureDetector = new GestureDetectorCompat(getContext(), listener);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector != null) {
			mGestureDetector.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

}
