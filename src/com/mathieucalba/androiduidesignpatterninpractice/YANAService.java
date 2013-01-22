package com.mathieucalba.androiduidesignpatterninpractice;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class YANAService extends IntentService {

	private static final String TAG = YANAService.class.getSimpleName();

	private static final String EXTRA_API_ID = "com.mathieucalba.androiduidesignpatterninpractice.EXTRA_API_ID";

	private static final int DEFAULT_API_ID = -1;
	private static final int API_INIT = 1301222226;

	public YANAService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onHandleIntent(" + intent.getExtras() + ")");
		}

		final Bundle extras = intent.getExtras();
		if (extras == null) {
			return;
		}

		final int idApi = extras.getInt(EXTRA_API_ID, -1);
		switch (idApi) {
			case API_INIT:
				initData();
				break;

			default:
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "- no corresponding Id API for : " + idApi);
				}
				break;
		}
	}

	private void initData() {

	}

}
