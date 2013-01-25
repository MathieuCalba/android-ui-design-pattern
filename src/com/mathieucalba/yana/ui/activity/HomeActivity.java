package com.mathieucalba.yana.ui.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.mathieucalba.yana.BuildConfig;
import com.mathieucalba.yana.R;
import com.mathieucalba.yana.provider.YANAContract;
import com.mathieucalba.yana.ui.adapters.CategoriesMenuAdapter;
import com.mathieucalba.yana.utils.LoaderUtils;
import com.mathieucalba.yana.utils.ServiceUtils;


public class HomeActivity extends SherlockFragmentActivity implements LoaderCallbacks<Cursor>, OnNavigationListener {

	private static final String TAG = HomeActivity.class.getSimpleName();

	private static final int LOADER_ID_CATEGORIES = 1301250153;

	private CategoriesMenuAdapter mCategoriesMenuAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		refreshData();

		initActionBar();

		initSpinnerCategories();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

	private void refreshData() {
		ServiceUtils.startInitData(this);
	}

	private void initActionBar() {
		final ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(false);
		ab.setTitle(null);
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int idLoader, Bundle bundle) {
		if (idLoader == LOADER_ID_CATEGORIES) {
			return new CursorLoader(this, YANAContract.CategoryTable.buildUri(), YANAContract.CategoryTable.PROJ.COLS, null, null,
					YANAContract.CategoryTable.DEFAULT_SORT);
		} else {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "Error, id for cursor unknown-id=[" + idLoader + "]");
			}
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		int id = -1;
		if (loader != null) {
			id = loader.getId();
		}
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoadFinished:" + id + "; cursor:" + cursor);
		}

		if (id == LOADER_ID_CATEGORIES) {
			if (cursor != null) {
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "onLoadFinished: count=" + cursor.getCount());
				}
			}

			mCategoriesMenuAdapter.swapCursor(cursor);

			// getSupportActionBar().setSelectedNavigationItem(0);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		final int id = loader.getId();
		if (BuildConfig.DEBUG) {
			Log.e(TAG, "onLoaderReset:" + id);
		}
		if (id == LOADER_ID_CATEGORIES) {
			mCategoriesMenuAdapter.swapCursor(null);
		}
	}

}
