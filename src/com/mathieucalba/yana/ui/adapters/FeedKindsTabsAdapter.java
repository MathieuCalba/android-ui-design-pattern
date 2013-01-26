package com.mathieucalba.yana.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.mathieucalba.yana.provider.YANAContract;
import com.mathieucalba.yana.ui.fragments.FeedListFragment;


public class FeedKindsTabsAdapter extends FragmentStatePagerCursorAdapter {

	public FeedKindsTabsAdapter(FragmentManager fm) {
		super(null, fm, null);
	}

	@Override
	public Fragment getItem(int position) {
		if (mCursor != null && position >= 0 && position < mCursor.getCount() && mCursor.moveToPosition(position)) {
			final int feedId = mCursor.getInt(YANAContract.FeedTable.PROJ.ID);
			return FeedListFragment.newInstance(feedId);
		}
		return FeedListFragment.newInstance(-1);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (mCursor != null && position >= 0 && position < mCursor.getCount() && mCursor.moveToPosition(position)) {
			final String feedName = mCursor.getString(YANAContract.FeedTable.PROJ.NAME);
			return feedName;
		}
		return "No Feed Name";
	}

}
