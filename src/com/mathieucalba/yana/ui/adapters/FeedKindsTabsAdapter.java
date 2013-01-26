package com.mathieucalba.yana.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.mathieucalba.yana.provider.YANAContract;
import com.mathieucalba.yana.ui.fragments.FeedListFragment;


public class FeedKindsTabsAdapter extends FragmentStatePagerCursorAdapter {

	private int mCategoryId = -1;

	public FeedKindsTabsAdapter(FragmentManager fm) {
		this(fm, -1);
	}

	public FeedKindsTabsAdapter(FragmentManager fm, int categoryId) {
		super(null, fm, null);

		mCategoryId = categoryId;
	}

	@Override
	public Fragment getItem(int position) {
		if (mCursor != null && position >= 0 && position < mCursor.getCount() && mCursor.moveToPosition(position)) {
			final int feedId = mCursor.getInt(YANAContract.FeedTable.PROJ.ID);
			return FeedListFragment.newInstance(feedId, mCategoryId);
		}
		return FeedListFragment.newInstance(-1, -1);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (mCursor != null && position >= 0 && position < mCursor.getCount() && mCursor.moveToPosition(position)) {
			final String feedName = mCursor.getString(YANAContract.FeedTable.PROJ.NAME);
			return feedName;
		}
		return "No Feed Name";
	}

	public void setCategoryId(int categoryId) {
		mCategoryId = categoryId;
		if (mFragments != null) {
			for (int i = 0; i < mFragments.length; i++) {
				final FeedListFragment f = (FeedListFragment) mFragments[i];
				if (f != null) {
					f.setCategoryId(categoryId);
				}
			}
		}
	}

}
