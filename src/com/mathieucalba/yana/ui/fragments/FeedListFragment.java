package com.mathieucalba.yana.ui.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.mathieucalba.yana.R;
import com.mathieucalba.yana.model.FeedsData;
import com.mathieucalba.yana.provider.YANAContract;
import com.mathieucalba.yana.ui.activity.FeedItemActivity;
import com.mathieucalba.yana.ui.adapters.FeedListAdapter;
import com.mathieucalba.yana.utils.LoaderUtils;


public class FeedListFragment extends SherlockFragment implements LoaderCallbacks<Cursor>, OnItemClickListener {

	public static final String EXTRA_FEED_ID = "com.mathieucalba.yana.EXTRA_FEED_ID";
	public static final String EXTRA_CATEGORY_ID = "com.mathieucalba.yana.EXTRA_CATEGORY_ID";

	private static final int LOADER_ID_BASE_FEED_LIST = 1301260000;

	private static final String STATE_CHECKED_POSITION = "com.mathieucalba.yana.STATE_CHECKED_POSITION";

	private int mFeedId = -1;
	private int mCategoryId = -1;
	private int mCurrentNbItem = 0;
	private int mCheckedPosition = -1;
	private FeedListAdapter mFeedListAdapter;

	private ListView mListView;
	private View mEmptyView;

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

		mListView = (ListView) v.findViewById(R.id.list_view);
		mListView.setOnItemClickListener(this);
		mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

		mEmptyView = v.findViewById(R.id.empty_view);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mFeedListAdapter = new FeedListAdapter(getActivity());
		mListView.setAdapter(mFeedListAdapter);

		if (savedInstanceState != null) {
			mCheckedPosition = savedInstanceState.getInt(STATE_CHECKED_POSITION, -1);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		loadFeedContent();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(STATE_CHECKED_POSITION, mCheckedPosition);
	}

	public void setCategoryId(int categoryId) {
		mCategoryId = categoryId;
		loadFeedContent();
	}

	private void loadFeedContent() {
		final Bundle b = new Bundle();
		b.putInt(EXTRA_FEED_ID, mFeedId);
		b.putInt(EXTRA_CATEGORY_ID, mCategoryId);
		LoaderUtils.restartLoader(this, LOADER_ID_BASE_FEED_LIST + mFeedId * 100 + mCategoryId, b, this);
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

					return new CursorLoader(getActivity(), uri, YANAContract.ArticleTable.PROJ_LIST.COLS, null, null, YANAContract.ArticleTable.DEFAULT_SORT);
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
			if (cursor != null && cursor.moveToFirst()) {
				mListView.setVisibility(View.VISIBLE);
				mEmptyView.setVisibility(View.GONE);
				mCurrentNbItem = cursor.getCount();
			} else {
				mListView.setVisibility(View.GONE);
				mEmptyView.setVisibility(View.VISIBLE);
				mCurrentNbItem = 0;
			}
			mFeedListAdapter.swapCursor(cursor);

			if (mCheckedPosition >= 0) {
				mListView.setItemChecked(mCheckedPosition, true);
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		final int id = loader.getId();
		final int realId = id - mFeedId * 100 - mCategoryId;
		if (realId == LOADER_ID_BASE_FEED_LIST) {
			mFeedListAdapter.swapCursor(null);
			mCurrentNbItem = 0;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// Briefs don't have content details
		if (mFeedId != FeedsData.FEED_IDS.BRIEF) {
			final Intent intent = new Intent(getActivity(), FeedItemActivity.class);
			intent.putExtra(FeedItemActivity.EXTRA_CATEGORY_ID, mCategoryId);
			intent.putExtra(FeedItemActivity.EXTRA_FEED_ID, mFeedId);
			final Cursor c = (Cursor) mFeedListAdapter.getItem(position);
			if (c != null) {
				final int idItem = c.getInt(YANAContract.ArticleTable.PROJ_LIST.ID);
				intent.putExtra(FeedItemActivity.EXTRA_ITEM_ID, idItem);
			}
			startActivity(intent);

			mListView.setItemChecked(position, true);
			mCheckedPosition = position;
		}
	}

}
