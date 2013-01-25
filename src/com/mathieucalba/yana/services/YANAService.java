package com.mathieucalba.yana.services;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.mathieucalba.yana.BuildConfig;
import com.mathieucalba.yana.R;
import com.mathieucalba.yana.model.FeedsData;
import com.mathieucalba.yana.provider.YANAContract;


public class YANAService extends IntentService {

	private static final String TAG = YANAService.class.getSimpleName();

	public static final String EXTRA_API_ID = "com.mathieucalba.yana.EXTRA_API_ID";

	private static final int DEFAULT_API_ID = -1;
	public static final int API_INIT = 1301222226;

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

		final int idApi = extras.getInt(EXTRA_API_ID, DEFAULT_API_ID);
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

	private static final String PREF_DATA_NAME = "com.mathieucalba.yana.PREF_DATA_NAME";
	private static final String PREF_DATA_IS_INIT_KEY = "com.mathieucalba.yana.PREF_DATA_IS_INIT_KEY";

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void initData() {
		final SharedPreferences pref = getSharedPreferences(PREF_DATA_NAME, MODE_PRIVATE);
		final boolean isDataInit = pref.getBoolean(PREF_DATA_IS_INIT_KEY, false);

		if (!isDataInit) {
			final ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

			batch.add(createArticleInsertOpe(
					//
					21, //
					"consectetur adipisicing elit", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.MOBILE, //
					1358897479));

			batch.add(createArticleInsertOpe(
					//
					22, //
					"sed do eiusmod tempor incididunt", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.MOBILE, //
					1358896479));

			batch.add(createArticleInsertOpe(
					//
					23, //
					"ut labore et dolore magna aliqua", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.MOBILE, //
					1358895479));

			batch.add(createArticleInsertOpe(
					//
					24, //
					"Lorem ipsum dolor sit amet", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.MOBILE, //
					1358898479));

			batch.add(createArticleInsertOpe(
					//
					25, //
					"consectetur adipisicing elit", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.MICROSOFT, //
					1358897479));

			batch.add(createArticleInsertOpe(
					//
					26, //
					"sed do eiusmod tempor incididunt", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.MAC, //
					1358896479));

			batch.add(createArticleInsertOpe(
					//
					27, //
					"ut labore et dolore magna aliqua", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.MAC, //
					1358895479));

			batch.add(createArticleInsertOpe(
					//
					28, //
					"Ut enim ad minim veniam", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.HARDWARE, //
					1358894479));

			batch.add(createArticleInsertOpe(
					//
					29, //
					"quis nostrud exercitation ullamco", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.GOOGLE, //
					1358893479));

			batch.add(createArticleInsertOpe(
					//
					30, //
					"laboris nisi ut aliquip ex ea commodo consequat", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.GOOGLE, //
					1358892479));

			batch.add(createArticleInsertOpe(
					//
					31, //
					"Duis aute irure dolor in reprehenderit", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.GOOGLE, //
					1358891479));

			batch.add(createArticleInsertOpe(
					//
					32, //
					"in voluptate velit esse cillum dolore", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.BUSINESS, //
					1358890479));

			batch.add(createArticleInsertOpe(
					//
					33, //
					"eu fugiat nulla pariatur", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.BUSINESS, //
					1358889479));

			batch.add(createArticleInsertOpe(
					//
					34, //
					"Excepteur sint occaecat cupidatat", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.APPLE, //
					1358888479));

			batch.add(createArticleInsertOpe(
					//
					35, //
					"non proident, sunt in culpa", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.APPLE, //
					1358887479));

			batch.add(createArticleInsertOpe(
					//
					36, //
					"qui officia deserunt mollit anim", //
					"", //
					"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", //
					getString(R.string.html_content), //
					"YANA", //
					FeedsData.FEED_IDS.NEWS, //
					FeedsData.CATEGORY_IDS.APPLE, //
					1358886479));

			final ContentResolver cr = getContentResolver();
			try {
				cr.applyBatch(YANAContract.CONTENT_AUTHORITY, batch);

				final SharedPreferences.Editor edit = pref.edit();
				edit.putBoolean(PREF_DATA_IS_INIT_KEY, true);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
					edit.apply();
				} else {
					edit.commit();
				}
			} catch (final RemoteException e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "Impossible to add batch", e);
				}
			} catch (final OperationApplicationException e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "Impossible to add batch", e);
				}
			}
		}
	}

	private ContentProviderOperation createArticleInsertOpe(int id, String title, String imageUrl, String header, String content, String author, int feedId,
			int categoryId,
			long timestamp) {
		final ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(YANAContract.ArticleTable.CONTENT_URI);
		builder.withValue(YANAContract.ArticleTable.ID, id);
		builder.withValue(YANAContract.ArticleTable.TITLE, title);
		builder.withValue(YANAContract.ArticleTable.IMAGE_URL, imageUrl);
		builder.withValue(YANAContract.ArticleTable.HEADER, header);
		builder.withValue(YANAContract.ArticleTable.CONTENT, content);
		builder.withValue(YANAContract.ArticleTable.AUTHOR, author);
		builder.withValue(YANAContract.ArticleTable.FEED_ID, feedId);
		builder.withValue(YANAContract.ArticleTable.TIMESTAMP, timestamp);
		return builder.build();
	}

}
