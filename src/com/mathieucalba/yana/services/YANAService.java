package com.mathieucalba.yana.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

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

			final Set<FeedsData.Article> articles = FeedsData.getArticles(this);
			for (final Iterator<FeedsData.Article> iterator = articles.iterator(); iterator.hasNext();) {
				final FeedsData.Article article = iterator.next();
				batch.add(createArticleInsertOpe(article));
			}

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

	private ContentProviderOperation createArticleInsertOpe(FeedsData.Article article) {
		final ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(YANAContract.ArticleTable.CONTENT_URI);
		builder.withValue(YANAContract.ArticleTable.ID, article.id);
		builder.withValue(YANAContract.ArticleTable.TITLE, article.title);
		builder.withValue(YANAContract.ArticleTable.IMAGE_URL, article.imageUrl);
		builder.withValue(YANAContract.ArticleTable.HEADER, article.header);
		builder.withValue(YANAContract.ArticleTable.CONTENT, article.htmlContent);
		builder.withValue(YANAContract.ArticleTable.AUTHOR, article.author);
		builder.withValue(YANAContract.ArticleTable.FEED_ID, article.feedId);
		builder.withValue(YANAContract.ArticleTable.CATEGORY_ID, article.categoryId);
		builder.withValue(YANAContract.ArticleTable.TIMESTAMP, article.timestamp);
		return builder.build();
	}

}
