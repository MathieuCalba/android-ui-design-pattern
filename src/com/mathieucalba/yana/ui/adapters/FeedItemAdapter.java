package com.mathieucalba.yana.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.mathieucalba.yana.provider.YANAContract;
import com.mathieucalba.yana.ui.fragments.FeedItemFragment;


public class FeedItemAdapter extends FragmentStatePagerCursorAdapter {

	public FeedItemAdapter(FragmentManager fm) {
		super(null, fm, null);
	}

	@Override
	public Fragment getItem(int position) {
		if (mCursor != null && position >= 0 && position < mCursor.getCount() && mCursor.moveToPosition(position)) {
			final int itemId = mCursor.getInt(YANAContract.ArticleTable.PROJ_LIST.ID);
			return FeedItemFragment.newInstance(itemId);
		}
		return FeedItemFragment.newInstance(-1);
	}

}
