package com.mathieucalba.yana.ui.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.mathieucalba.yana.R;
import com.mathieucalba.yana.utils.ServiceUtils;


public class HomeActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);

		initActionBar();

		refreshData();
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

}
