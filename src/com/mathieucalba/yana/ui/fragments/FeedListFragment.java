package com.mathieucalba.yana.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class FeedListFragment extends SherlockFragment {

	public static final String EXTRA_FEED_ID = "com.mathieucalba.yana.EXTRA_FEED_ID";

	public static FeedListFragment newInstance(int feedId) {
		final FeedListFragment f = new FeedListFragment();

		final Bundle b = new Bundle();
		b.putInt(EXTRA_FEED_ID, feedId);
		f.setArguments(b);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final TextView tv = new TextView(getActivity());
		tv.setText("blhtrsn blkn");
		return tv;
	}

}
