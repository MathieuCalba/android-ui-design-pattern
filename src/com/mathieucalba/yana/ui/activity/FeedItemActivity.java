package com.mathieucalba.yana.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.mathieucalba.yana.BuildConfig;
import com.mathieucalba.yana.R;
import com.mathieucalba.yana.provider.YANAContract;
import com.mathieucalba.yana.ui.adapters.FeedItemAdapter;
import com.mathieucalba.yana.utils.LoaderUtils;


public class FeedItemActivity extends SherlockFragmentActivity implements LoaderCallbacks<Cursor>, OnPageChangeListener {

	private static final String TAG = FeedItemActivity.class.getSimpleName();

	private static final int LOADER_ID_FEED_LIST = 1301292212;

	public static final String EXTRA_CATEGORY_ID = "com.mathieucalba.yana.EXTRA_CATEGORY_ID";
	public static final String EXTRA_FEED_ID = "com.mathieucalba.yana.EXTRA_FEED_ID";
	public static final String EXTRA_ITEM_ID = "com.mathieucalba.yana.EXTRA_ITEM_ID";

	private ViewPager mViewPager;
	private FeedItemAdapter mFeedItemAdapter;
	private int mCategoryId = -1;
	private int mFeedId;
	private int mCurrentItemId;
	private int mCurrentItemPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_item);

		final Intent i = getIntent();
		if (i != null) {
			mFeedId = i.getIntExtra(EXTRA_FEED_ID, -1);
		}
		if (mFeedId == -1) {
			throw new IllegalArgumentException(
					"You must create this fragment from FeedListFragment#newInstance(int feedId) method to provide a bundle of arguments");
		}

		if (i != null) {
			mCategoryId = i.getIntExtra(EXTRA_CATEGORY_ID, -1);
		}

		if (i != null) {
			mCurrentItemId = i.getIntExtra(EXTRA_ITEM_ID, -1);
		}
		if (mCurrentItemId == -1) {
			throw new IllegalArgumentException("You must create this activity with an extras " + EXTRA_ITEM_ID);
		}

		initActionBar();

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mFeedItemAdapter = new FeedItemAdapter(getSupportFragmentManager());
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setAdapter(mFeedItemAdapter);
	}

	@Override
	protected void onStart() {
		super.onStart();

		loadArticles();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.homeAsUp) {
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void initActionBar() {
		final ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowTitleEnabled(true);
		ab.setTitle(null);
	}

	private void loadArticles() {
		final Bundle b = new Bundle();
		b.putInt(EXTRA_FEED_ID, mFeedId);
		b.putInt(EXTRA_CATEGORY_ID, mCategoryId);
		// b.putInt(EXTRA_NB_ITEM_ID, nbItemToLoad);
		LoaderUtils.restartLoader(this, LOADER_ID_FEED_LIST + mFeedId * 100 + mCategoryId, b, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle b) {
		final int realId = id - mFeedId * 100 - mCategoryId;
		if (realId == LOADER_ID_FEED_LIST) {
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

					// final int limit = b.getInt(EXTRA_NB_ITEM_ID, DEFAULT_NB_ITEM);

					return new CursorLoader(this, uri, YANAContract.ArticleTable.PROJ_LIST.COLS, null, null, YANAContract.ArticleTable.DEFAULT_SORT);
					// + LIMIT_0_X + limit
				}
			}
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		final int id = loader.getId();
		final int realId = id - mFeedId * 100 - mCategoryId;
		if (realId == LOADER_ID_FEED_LIST) {
			int position = -1;
			if (cursor != null && cursor.moveToFirst()) {
				do {
					final int idItem = cursor.getInt(YANAContract.ArticleTable.PROJ_LIST.ID);
					if (idItem == mCurrentItemId) {
						position = cursor.getPosition();
					}
				} while (cursor.moveToNext());
			}

			mFeedItemAdapter.swapCursor(cursor);

			if (position > -1) {
				mViewPager.setCurrentItem(position, true);
				getSupportActionBar().setTitle(position + 1 + " / " + cursor.getCount());
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		final int id = loader.getId();
		if (BuildConfig.DEBUG) {
			Log.e(TAG, "onLoaderReset:" + id);
		}

		if (id == LOADER_ID_FEED_LIST) {
			mFeedItemAdapter.swapCursor(null);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		if (positionOffset > 0.5F) {
			position++;
		}
		getSupportActionBar().setTitle(position + 1 + " / " + mFeedItemAdapter.getCount());
	}

	@Override
	public void onPageSelected(int position) {
		getSupportActionBar().setTitle(position + 1 + " / " + mFeedItemAdapter.getCount());
		mCurrentItemPosition = position;
	}

}
