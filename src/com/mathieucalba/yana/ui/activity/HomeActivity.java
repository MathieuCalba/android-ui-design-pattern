package com.mathieucalba.yana.ui.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.mathieucalba.yana.BuildConfig;
import com.mathieucalba.yana.R;
import com.mathieucalba.yana.provider.YANAContract;
import com.mathieucalba.yana.ui.adapters.CategoriesMenuAdapter;
import com.mathieucalba.yana.ui.adapters.FeedKindsTabsAdapter;
import com.mathieucalba.yana.utils.LoaderUtils;
import com.mathieucalba.yana.utils.ServiceUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.TabPageIndicator;


public class HomeActivity extends SherlockFragmentActivity implements LoaderCallbacks<Cursor>, OnNavigationListener {

	private static final String TAG = HomeActivity.class.getSimpleName();

	private static final int LOADER_ID_CATEGORIES = 1301250153;
	private static final int LOADER_ID_FEED_KINDS = 1301260040;

	private CategoriesMenuAdapter mCategoriesMenuAdapter;
	private FeedKindsTabsAdapter mFeedKindsTabsAdapter;
	private TabPageIndicator mTabPageIndicator;
	private ViewPager mViewPager;
	private int mCategoryId = -1;

	protected ImageLoader mImageLoader = ImageLoader.getInstance();

	private boolean mInstanceStateSaved = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);

		initActionBar();

		refreshData();

		initSpinnerCategories();

		initTabsFeedKind();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		mInstanceStateSaved = true;
	}

	@Override
	protected void onDestroy() {
		if (!mInstanceStateSaved) {
			mImageLoader.stop();
		}
		super.onDestroy();
	}

	private void refreshData() {
		ServiceUtils.startInitData(this);
		setRefreshingState(true);
	}

	private void initActionBar() {
		setRefreshingState(false);

		final ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(false);
		ab.setDisplayShowTitleEnabled(false);
	}

	public void setRefreshingState(boolean isRefreshing) {
		setSupportProgressBarIndeterminateVisibility(isRefreshing);
	}

	private void initSpinnerCategories() {
		mCategoriesMenuAdapter = new CategoriesMenuAdapter(this);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSupportActionBar().setListNavigationCallbacks(mCategoriesMenuAdapter, this);

		loadCategories();
	}

	private void loadCategories() {
		LoaderUtils.restartLoader(this, LOADER_ID_CATEGORIES, null, this);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		if (itemPosition == 0) {
			mCategoryId = -1;
		} else {
			final Cursor c = (Cursor) mCategoriesMenuAdapter.getItem(itemPosition - 1);
			if (c != null) {
				mCategoryId = c.getInt(YANAContract.CategoryTable.PROJ.ID);
			}
		}

		mFeedKindsTabsAdapter.setCategoryId(mCategoryId);

		return true;
	}

	private void initTabsFeedKind() {
		mTabPageIndicator = (TabPageIndicator) findViewById(R.id.indicator);
		mViewPager = (ViewPager) findViewById(R.id.pager_list);

		if (mViewPager != null) {
			mFeedKindsTabsAdapter = new FeedKindsTabsAdapter(getSupportFragmentManager());
			mViewPager.setAdapter(mFeedKindsTabsAdapter);
			if (mTabPageIndicator != null) {
				mTabPageIndicator.setViewPager(mViewPager);
			}
		}

		loadFeedKinds();
	}

	private void loadFeedKinds() {
		LoaderUtils.restartLoader(this, LOADER_ID_FEED_KINDS, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int idLoader, Bundle bundle) {
		switch (idLoader) {
			case LOADER_ID_CATEGORIES:
				return new CursorLoader(this, YANAContract.CategoryTable.buildUri(), YANAContract.CategoryTable.PROJ.COLS, null, null,
						YANAContract.CategoryTable.DEFAULT_SORT);

			case LOADER_ID_FEED_KINDS:
				return new CursorLoader(this, YANAContract.FeedTable.buildUri(), YANAContract.FeedTable.PROJ.COLS, null, null,
						YANAContract.FeedTable.DEFAULT_SORT);

			default:
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "Error, id for cursor unknown-id=[" + idLoader + "]");
				}
				break;
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		int id = -1;
		if (loader != null) {
			id = loader.getId();
		}

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoadFinished:" + id + "; cursor:" + cursor + " with count:" + (cursor != null ? cursor.getCount() : 0));
		}

		switch (id) {
			case LOADER_ID_CATEGORIES:
				mCategoriesMenuAdapter.swapCursor(cursor);
				break;

			case LOADER_ID_FEED_KINDS:
				mFeedKindsTabsAdapter.swapCursor(cursor);
				mTabPageIndicator.notifyDataSetChanged();
				break;
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		final int id = loader.getId();
		if (BuildConfig.DEBUG) {
			Log.e(TAG, "onLoaderReset:" + id);
		}

		switch (id) {
			case LOADER_ID_CATEGORIES:
				mCategoriesMenuAdapter.swapCursor(null);
				break;

			case LOADER_ID_FEED_KINDS:
				mFeedKindsTabsAdapter.swapCursor(null);
				mTabPageIndicator.notifyDataSetChanged();
				break;
			default:
				break;
		}
	}

}
