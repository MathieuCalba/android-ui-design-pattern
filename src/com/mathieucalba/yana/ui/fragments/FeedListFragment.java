package com.mathieucalba.yana.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.mathieucalba.yana.R;


public class FeedListFragment extends SherlockFragment {

	public static final String EXTRA_FEED_ID = "com.mathieucalba.yana.EXTRA_FEED_ID";
	public static final String EXTRA_CATEGORY_ID = "com.mathieucalba.yana.EXTRA_CATEGORY_ID";

	private int mFeedId = -1;
	private int mCategoryId = -1;

	public static FeedListFragment newInstance(int feedId, int categoryId) {
		final FeedListFragment f = new FeedListFragment();

		final Bundle b = new Bundle();
		b.putInt(EXTRA_FEED_ID, feedId);
		b.putInt(EXTRA_CATEGORY_ID, categoryId);
		f.setArguments(b);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle b = getArguments();
		if (b != null) {
			mFeedId = b.getInt(EXTRA_FEED_ID, -1);
		}

		if (mFeedId == -1) {
			throw new IllegalArgumentException(
					"You must create this fragment from FeedListFragment#newInstance(int feedId) method to provide a bundle of arguments");
		}

		if (b != null) {
			mCategoryId = b.getInt(EXTRA_CATEGORY_ID, -1);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_feed_list, container, false);

		return v;
	}

	public void setCategoryId(int categoryId) {
		mCategoryId = categoryId;
		loadFeedContent();
	}

	private void loadFeedContent() {
	}

}
