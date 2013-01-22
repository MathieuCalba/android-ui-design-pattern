package com.mathieucalba.yana.ui.activity;


import android.app.Activity;
import android.view.Menu;

import com.mathieucalba.yana.R;
import com.mathieucalba.yana.utils.ServiceUtils;


public class HomeActivity extends Activity implements LoaderCallbacks<Cursor> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		refreshData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

	private void refreshData() {
		ServiceUtils.startInitData(this);
	}

}
