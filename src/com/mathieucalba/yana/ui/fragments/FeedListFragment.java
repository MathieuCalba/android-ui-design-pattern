package com.mathieucalba.yana.ui.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ViewAnimator;

import com.actionbarsherlock.app.SherlockFragment;
import com.mathieucalba.yana.R;
import com.mathieucalba.yana.provider.YANAContract;
import com.mathieucalba.yana.ui.adapters.FeedListAdapter;
import com.mathieucalba.yana.utils.LoaderUtils;


public class FeedListFragment extends SherlockFragment implements LoaderCallbacks<Cursor> {

	public static final String EXTRA_FEED_ID = "com.mathieucalba.yana.EXTRA_FEED_ID";
	public static final String EXTRA_CATEGORY_ID = "com.mathieucalba.yana.EXTRA_CATEGORY_ID";
	private static final String EXTRA_NB_ITEM_ID = "com.mathieucalba.yana.EXTRA_NB_ITEM_ID";

	private static final int LOADER_ID_BASE_FEED_LIST = 1301260000;

	private static final String LIMIT_0_X = " LIMIT 0,";
	private static final int DEFAULT_NB_ITEM = 20;

	private int mFeedId = -1;
	private int mCategoryId = -1;
	private int mCurrentNbItem = 0;
	private FeedListAdapter mFeedListAdapter;

	private ViewAnimator mViewAnimator;
	private ListView mListView;

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
		mViewAnimator = (ViewAnimator) inflater.inflate(R.layout.fragment_feed_list, container, false);
		mViewAnimator.setDisplayedChild(0);
		mListView = (ListView) mViewAnimator.findViewById(R.id.list_view);
		return mViewAnimator;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mFeedListAdapter = new FeedListAdapter(getActivity());
		mListView.setAdapter(mFeedListAdapter);
	}

	@Override
	public void onStart() {
		super.onStart();

		loadFeedContent();
	}

	public void setCategoryId(int categoryId) {
		mCategoryId = categoryId;
		loadFeedContent();
	}

	private void loadFeedContent() {
		loadFeedContent(DEFAULT_NB_ITEM);
	}

	private void loadFeedContent(int nbItemToLoad) {
		final Bundle b = new Bundle();
		b.putInt(EXTRA_FEED_ID, mFeedId);
		b.putInt(EXTRA_CATEGORY_ID, mCategoryId);
		b.putInt(EXTRA_NB_ITEM_ID, nbItemToLoad);
		LoaderUtils.restartLoader(this, LOADER_ID_BASE_FEED_LIST + mFeedId * 100 + mCategoryId, b, this);
	}

	private void loadFeedContentMoreItem() {
		loadFeedContent(DEFAULT_NB_ITEM + mCurrentNbItem);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle b) {
		final int realId = id - mFeedId * 100 - mCategoryId;
		if (realId == LOADER_ID_BASE_FEED_LIST) {
			if (b != null) {
				final int feedId = b.getInt(EXTRA_FEED_ID, -1);
				if (feedId != -1) {
					final int categoryId = b.getInt(EXTRA_CATEGORY_ID, -1);

					Uri uri = null;
					if (categoryId == -1) {
						uri = YANAContract.ArticleTable.buildUriWithFeedId(feedId);
					} else {
						uri = YANAContract.ArticleTable.buildUriWithFeedIdAndCategoryId(feedId, categoryId);
					}

					//					final int limit = b.getInt(EXTRA_NB_ITEM_ID, DEFAULT_NB_ITEM);

					return new CursorLoader(getActivity(), uri, YANAContract.ArticleTable.PROJ_LIST.COLS, null, null,
							YANAContract.ArticleTable.DEFAULT_SORT);// +
					// LIMIT_0_X
					// +
					// limit
				}
			}
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		final int id = loader.getId();
		final int realId = id - mFeedId * 100 - mCategoryId;
		if (realId == LOADER_ID_BASE_FEED_LIST) {
			mFeedListAdapter.swapCursor(cursor);
			if (cursor != null) {
				mViewAnimator.setDisplayedChild(1);
				mCurrentNbItem = cursor.getCount();
			} else {
				mViewAnimator.setDisplayedChild(2);
				mCurrentNbItem = 0;
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		final int id = loader.getId();
		final int realId = id - mFeedId * 100 - mCategoryId;
		if (realId == LOADER_ID_BASE_FEED_LIST) {
			mFeedListAdapter.swapCursor(null);
			mViewAnimator.setDisplayedChild(2);
			mCurrentNbItem = 0;
		}
	}

}
