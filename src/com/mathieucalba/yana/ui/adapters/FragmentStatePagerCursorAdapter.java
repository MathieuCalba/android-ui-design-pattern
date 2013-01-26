package com.mathieucalba.yana.ui.adapters;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;


public abstract class FragmentStatePagerCursorAdapter extends FragmentStatePagerAdapter {

	protected boolean mDataValid;
	protected Cursor mCursor;
	protected int mRowIDColumn;
	protected SparseArray<Fragment> mFragments;
	protected Bundle mBundle;
	private static final String COLUMNS_UID = "id";

	public FragmentStatePagerCursorAdapter(Cursor c, FragmentManager fm, Bundle b) {
		super(fm);
		init(c, b);
	}

	void init(Cursor c, Bundle b) {
		final boolean cursorPresent = c != null;
		mCursor = c;
		mDataValid = cursorPresent;
		mRowIDColumn = cursorPresent ? c.getColumnIndexOrThrow(COLUMNS_UID) : -1;
		mFragments = new SparseArray<Fragment>();
		mBundle = b;
	}

	public Cursor getCursor() {
		return mCursor;
	}

	@Override
	public int getCount() {
		if (mDataValid && mCursor != null) {
			return mCursor.getCount();
		} else {
			return 0;
		}
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (!mDataValid) {
			throw new IllegalStateException("this should only be called when the cursor is valid");
		}
		if (!mCursor.moveToPosition(position)) {
			throw new IllegalStateException("couldn't move cursor to position " + position);
		}

		final Fragment f = (Fragment) super.instantiateItem(container, position);

		mFragments.put(position, f);
		return f;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		mFragments.remove(position);
	}

	public void changeCursor(Cursor cursor) {
		final Cursor old = swapCursor(cursor);
		if (old != null) {
			old.close();
		}
	}

	public Cursor swapCursor(Cursor newCursor) {
		if (newCursor == mCursor) {
			return null;
		}
		final Cursor oldCursor = mCursor;
		mCursor = newCursor;
		if (newCursor != null) {
			mRowIDColumn = newCursor.getColumnIndexOrThrow(COLUMNS_UID);
			mDataValid = true;
			notifyDataSetChanged();
		} else {
			mRowIDColumn = -1;
			mDataValid = false;
			notifyDataSetChanged();
		}
		return oldCursor;
	}

}
