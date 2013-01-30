package com.mathieucalba.yana.ui.fragments;

import org.apache.http.protocol.HTTP;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.mathieucalba.yana.R;
import com.mathieucalba.yana.provider.YANAContract;
import com.mathieucalba.yana.ui.activity.FeedItemActivity;
import com.mathieucalba.yana.ui.widgets.GestureWebView;
import com.mathieucalba.yana.utils.LoaderUtils;


public class FeedItemFragment extends SherlockFragment implements LoaderCallbacks<Cursor>, OnGestureListener {

	private static final String BASE_URL = "http://www.google.com";
	private static final String TEXT_HTML = "text/html";

	public static final String EXTRA_ITEM_ID = FeedItemActivity.EXTRA_ITEM_ID;

	private static final int LOADER_ID_BASE_FEED_ITEM = 1301292000;

	private GestureWebView mWebView;
	private int mItemId;
	private Cursor mCursor;

	public static FeedItemFragment newInstance(int itemId) {
		final Bundle b = new Bundle();
		b.putInt(EXTRA_ITEM_ID, itemId);

		final FeedItemFragment f = new FeedItemFragment();
		f.setArguments(b);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle b = getArguments();
		if (b != null) {
			mItemId = b.getInt(EXTRA_ITEM_ID, -1);
		}
		if (mItemId == -1) {
			throw new IllegalArgumentException("You must create this fragment with an extra " + EXTRA_ITEM_ID + " using newInstance() method");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_feed_item, container, false);
		mWebView = (GestureWebView) v.findViewById(R.id.webview);
		mWebView.init(this);
		mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();

		loadArticleDetails();
	}

	private void loadArticleDetails() {
		final Bundle b = new Bundle();
		b.putInt(EXTRA_ITEM_ID, mItemId);
		LoaderUtils.restartLoader(this, LOADER_ID_BASE_FEED_ITEM + mItemId, b, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle b) {
		final int realId = id - mItemId;
		if (realId == LOADER_ID_BASE_FEED_ITEM) {
			if (b != null) {
				final int itemId = b.getInt(EXTRA_ITEM_ID, -1);
				if (itemId != -1) {
					final Uri uri = YANAContract.ArticleTable.buildUriWithArticleId(itemId);
					return new CursorLoader(getActivity(), uri, YANAContract.ArticleTable.PROJ_DETAIL.COLS, null, null, null);
				}
			}
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		final int id = loader.getId();
		final int realId = id - mItemId;
		if (realId == LOADER_ID_BASE_FEED_ITEM) {
			mCursor = cursor;

			if (cursor != null && cursor.moveToFirst()) {
				final String html = cursor.getString(YANAContract.ArticleTable.PROJ_DETAIL.CONTENT);
				mWebView.loadDataWithBaseURL(BASE_URL, html, TEXT_HTML, HTTP.UTF_8, null);
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		final int id = loader.getId();
		final int realId = id - mItemId;
		if (realId == LOADER_ID_BASE_FEED_ITEM) {
			mCursor = null;
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		final float x1 = e1.getX();
		final float y1 = e1.getY();
		final float x2 = e2.getX();
		final float y2 = e2.getY();

		final float dx = x1 - x2;
		final float dy = y1 - y2;
		final int height = mWebView.getHeight();
		if (Math.abs(dx) < Math.abs(dy) && Math.abs(dy) > (float) height / 4) {
			showActionBar(dy < 0);
		}
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	private void showActionBar(boolean state) {
		if (getSherlockActivity() != null && !isDetached()) {
			final ActionBar ab = getSherlockActivity().getSupportActionBar();
			if (state) {
				ab.show();
			} else {
				ab.hide();
			}
		}
	}

}
