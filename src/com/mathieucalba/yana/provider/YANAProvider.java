package com.mathieucalba.yana.provider;

import java.util.Arrays;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.mathieucalba.yana.BuildConfig;


public class YANAProvider extends ContentProvider {

	private static final String TAG = YANAProvider.class.getSimpleName();

	protected static UriMatcher mUriMatcher;
	private static final String SEPARATOR = "/";
	private static final String MANY = "/*";

	private static final int ARTICLE = 100;
	private static final int ARTICLE_ID = 101;
	private static final int ARTICLE_BY_FEED_ID = 102;
	private static final int ARTICLE_BY_CATEGORY_ID = 103;
	private static final int ARTICLE_BY_FEED_ID_AND_CATEGORY_ID = 104;

	private static final int FEED = 200;
	private static final int FEED_ID = 201;

	private static final int CATEGORY = 300;
	private static final int CATEGORY_ID = 301;

	protected static YANADatabase mDatabase;

	protected static UriMatcher buildUriMatcher(final String authority) {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "authority=[" + authority + "]");
		}

		/* ARTICLES */
		matcher.addURI(authority, YANAContract.PATH_ARTICLE, ARTICLE);
		matcher.addURI(authority, YANAContract.PATH_ARTICLE + MANY, ARTICLE_ID);
		matcher.addURI(authority, YANAContract.PATH_ARTICLE + SEPARATOR + YANAContract.PATH_FEED + MANY, ARTICLE_BY_FEED_ID);
		matcher.addURI(authority, YANAContract.PATH_ARTICLE + SEPARATOR + YANAContract.PATH_CATEGORY + MANY, ARTICLE_BY_CATEGORY_ID);
		matcher.addURI(authority, YANAContract.PATH_ARTICLE + SEPARATOR + YANAContract.PATH_FEED + MANY + YANAContract.PATH_CATEGORY + MANY,
				ARTICLE_BY_FEED_ID_AND_CATEGORY_ID);

		/* FEEDS */
		matcher.addURI(authority, YANAContract.PATH_FEED, FEED);
		matcher.addURI(authority, YANAContract.PATH_FEED + MANY, FEED_ID);

		/* CATEGORIES */
		matcher.addURI(authority, YANAContract.PATH_CATEGORY, CATEGORY);
		matcher.addURI(authority, YANAContract.PATH_CATEGORY + MANY, CATEGORY_ID);

		return matcher;
	}

	@Override
	public boolean onCreate() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreate()");
		}

		mDatabase = new YANADatabase(getContext(), YANADatabase.DATABASE_NAME, null, YANADatabase.DATABASE_VERSION);
		mUriMatcher = buildUriMatcher(YANAContract.CONTENT_AUTHORITY);

		return true;
	}

	@Override
	public String getType(Uri uri) {
		final int match = mUriMatcher.match(uri);
		switch (match) {
			case ARTICLE:
			case ARTICLE_BY_FEED_ID:
			case ARTICLE_BY_CATEGORY_ID:
			case ARTICLE_BY_FEED_ID_AND_CATEGORY_ID:
				return YANAContract.ArticleTable.CONTENT_TYPE;
			case ARTICLE_ID:
				return YANAContract.ArticleTable.CONTENT_ITEM_TYPE;

			case FEED:
				return YANAContract.FeedTable.CONTENT_TYPE;
			case FEED_ID:
				return YANAContract.FeedTable.CONTENT_ITEM_TYPE;

			case CATEGORY:
				return YANAContract.CategoryTable.CONTENT_TYPE;
			case CATEGORY_ID:
				return YANAContract.CategoryTable.CONTENT_ITEM_TYPE;
		}

		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "query(uri=" + uri + ", proj=" + Arrays.toString(projection) + ")");
		}

		final SQLiteDatabase db = mDatabase.getReadableDatabase();
		if (db == null || !db.isOpen()) {
			return null;
		}

		final int match = mUriMatcher.match(uri);
		switch (match) {

			case ARTICLE: {
				final Cursor c = db.query(YANAContract.Tables.ARTICLE, projection, selection, selectionArgs, null, null, sortOrder);
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;
			}

			case ARTICLE_BY_FEED_ID: {
				final StringBuilder select = new StringBuilder();
				if (!TextUtils.isEmpty(selection)) {
					select.append(selection);
					select.append(" AND ");
				}
				select.append(YANAContract.Tables.ARTICLE);
				select.append('.');
				select.append(YANAContract.ArticleTable.FEED_ID);
				select.append(" = ");
				select.append(YANAContract.ArticleTable.getFeedId(uri));
				selection = select.toString();
				final Cursor c = db.query(YANAContract.Tables.ARTICLE, projection, selection, selectionArgs, null, null, sortOrder);
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;
			}

			case ARTICLE_BY_CATEGORY_ID: {
				final StringBuilder select = new StringBuilder();
				if (!TextUtils.isEmpty(selection)) {
					select.append(selection);
					select.append(" AND ");
				}
				select.append(YANAContract.Tables.ARTICLE);
				select.append('.');
				select.append(YANAContract.ArticleTable.CATEGORY_ID);
				select.append(" = ");
				select.append(YANAContract.ArticleTable.getCategoryId(uri));
				selection = select.toString();
				final Cursor c = db.query(YANAContract.Tables.ARTICLE, projection, selection, selectionArgs, null, null, sortOrder);
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;
			}

			case ARTICLE_BY_FEED_ID_AND_CATEGORY_ID: {
				final StringBuilder select = new StringBuilder();
				if (!TextUtils.isEmpty(selection)) {
					select.append(selection);
					select.append(" AND ");
				}
				select.append(YANAContract.Tables.ARTICLE);
				select.append('.');
				select.append(YANAContract.ArticleTable.FEED_ID);
				select.append(" = ");
				select.append(YANAContract.ArticleTable.getFeedId(uri));
				select.append(" AND ");
				select.append(YANAContract.Tables.ARTICLE);
				select.append('.');
				select.append(YANAContract.ArticleTable.CATEGORY_ID);
				select.append(" = ");
				select.append(YANAContract.ArticleTable.getCategoryIdWithFeed(uri));
				selection = select.toString();
				final Cursor c = db.query(YANAContract.Tables.ARTICLE, projection, selection, selectionArgs, null, null, sortOrder);
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;
			}

			case ARTICLE_ID: {
				final StringBuilder select = new StringBuilder();
				if (!TextUtils.isEmpty(selection)) {
					select.append(selection);
					select.append(" AND ");
				}
				select.append(YANAContract.Tables.ARTICLE);
				select.append('.');
				select.append(YANAContract.ArticleTable.ID);
				select.append(" = ");
				select.append(YANAContract.ArticleTable.getId(uri));
				selection = select.toString();
				final Cursor c = db.query(YANAContract.Tables.ARTICLE, projection, selection, selectionArgs, null, null, sortOrder);
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;
			}

			case FEED: {
				final Cursor c = db.query(YANAContract.Tables.FEED, projection, selection, selectionArgs, null, null, sortOrder);
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;
			}

			case FEED_ID: {
				final StringBuilder select = new StringBuilder();
				if (!TextUtils.isEmpty(selection)) {
					select.append(selection);
					select.append(" AND ");
				}
				select.append(YANAContract.Tables.FEED);
				select.append('.');
				select.append(YANAContract.FeedTable.ID);
				select.append(" = ");
				select.append(YANAContract.FeedTable.getId(uri));
				selection = select.toString();
				final Cursor c = db.query(YANAContract.Tables.FEED, projection, selection, selectionArgs, null, null, sortOrder);
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;
			}

			case CATEGORY: {
				final Cursor c = db.query(YANAContract.Tables.CATEGORY, projection, selection, selectionArgs, null, null, sortOrder);
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;
			}

			case CATEGORY_ID: {
				final StringBuilder select = new StringBuilder();
				if (!TextUtils.isEmpty(selection)) {
					select.append(selection);
					select.append(" AND ");
				}
				select.append(YANAContract.Tables.CATEGORY);
				select.append('.');
				select.append(YANAContract.CategoryTable.ID);
				select.append(" = ");
				select.append(YANAContract.CategoryTable.getId(uri));
				selection = select.toString();
				final Cursor c = db.query(YANAContract.Tables.CATEGORY, projection, selection, selectionArgs, null, null, sortOrder);
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;
			}

			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "match (" + match + ") is not handled in query");
				}
				return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "insert(uri=" + uri + ", val=" + values + ")");
		}

		final SQLiteDatabase db = mDatabase.getReadableDatabase();
		if (db == null || !db.isOpen()) {
			return null;
		}

		final int match = mUriMatcher.match(uri);
		switch (match) {

			case ARTICLE:
			case ARTICLE_BY_FEED_ID:
			case ARTICLE_BY_CATEGORY_ID:
			case ARTICLE_BY_FEED_ID_AND_CATEGORY_ID:
			case ARTICLE_ID: {
				db.insertOrThrow(YANAContract.Tables.ARTICLE, null, values);
				getContext().getContentResolver().notifyChange(uri, null);
				return YANAContract.ArticleTable.buildUriWithArticleId(values.getAsInteger(YANAContract.ArticleColumns.ID));
			}

			case FEED:
			case FEED_ID: {
				db.insertOrThrow(YANAContract.Tables.FEED, null, values);
				getContext().getContentResolver().notifyChange(uri, null);
				return YANAContract.FeedTable.buildUriWithId(values.getAsInteger(YANAContract.FeedColumns.ID));
			}

			case CATEGORY:
			case CATEGORY_ID: {
				db.insertOrThrow(YANAContract.Tables.CATEGORY, null, values);
				getContext().getContentResolver().notifyChange(uri, null);
				return YANAContract.CategoryTable.buildUriWithId(values.getAsInteger(YANAContract.CategoryColumns.ID));
			}

			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "match (" + match + ") is not handled in insert");
				}
				return null;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "update(uri=" + uri + ", val=" + values + ", sel=" + selection + ", selArg=" + Arrays.toString(selectionArgs) + ")");
		}

		final SQLiteDatabase db = mDatabase.getWritableDatabase();
		if (db == null || !db.isOpen()) {
			return 0;
		}

		final int match = mUriMatcher.match(uri);
		switch (match) {

			case ARTICLE:
			case ARTICLE_BY_FEED_ID:
			case ARTICLE_BY_CATEGORY_ID:
			case ARTICLE_BY_FEED_ID_AND_CATEGORY_ID: {
				final int retVal = db.update(YANAContract.Tables.ARTICLE, values, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}
			case ARTICLE_ID: {
				final StringBuilder select = new StringBuilder();
				if (!TextUtils.isEmpty(selection)) {
					select.append(selection);
					select.append(" AND ");
				}
				select.append(YANAContract.Tables.ARTICLE);
				select.append('.');
				select.append(YANAContract.ArticleTable.ID);
				select.append(" = ");
				select.append(YANAContract.ArticleTable.getId(uri));
				selection = select.toString();
				final int retVal = db.update(YANAContract.Tables.ARTICLE, values, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}

			case FEED: {
				final int retVal = db.update(YANAContract.Tables.FEED, values, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}
			case FEED_ID: {
				final StringBuilder select = new StringBuilder();
				if (!TextUtils.isEmpty(selection)) {
					select.append(selection);
					select.append(" AND ");
				}
				select.append(YANAContract.Tables.FEED);
				select.append('.');
				select.append(YANAContract.FeedTable.ID);
				select.append(" = ");
				select.append(YANAContract.FeedTable.getId(uri));
				selection = select.toString();
				final int retVal = db.update(YANAContract.Tables.FEED, values, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}

			case CATEGORY: {
				final int retVal = db.update(YANAContract.Tables.CATEGORY, values, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}
			case CATEGORY_ID: {
				final StringBuilder select = new StringBuilder();
				if (!TextUtils.isEmpty(selection)) {
					select.append(selection);
					select.append(" AND ");
				}
				select.append(YANAContract.Tables.CATEGORY);
				select.append('.');
				select.append(YANAContract.CategoryTable.ID);
				select.append(" = ");
				select.append(YANAContract.CategoryTable.getId(uri));
				selection = select.toString();
				final int retVal = db.update(YANAContract.Tables.CATEGORY, values, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}

			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "match (" + match + ") is not handled in update");
				}
				return 0;
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "delete(uri=" + uri + ", sel=" + selection + ", selArg=" + Arrays.toString(selectionArgs) + ")");
		}

		final SQLiteDatabase db = mDatabase.getWritableDatabase();
		if (db == null || !db.isOpen()) {
			return 0;
		}

		final int match = mUriMatcher.match(uri);
		switch (match) {

			case ARTICLE: {
				final int retVal = db.delete(YANAContract.Tables.ARTICLE, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}

			case ARTICLE_BY_FEED_ID: {
				final StringBuilder select = new StringBuilder();
				if (!TextUtils.isEmpty(selection)) {
					select.append(selection);
					select.append(" AND ");
				}
				select.append(YANAContract.Tables.ARTICLE);
				select.append('.');
				select.append(YANAContract.ArticleTable.FEED_ID);
				select.append(" = ");
				select.append(YANAContract.ArticleTable.getFeedId(uri));
				selection = select.toString();
				final int retVal = db.delete(YANAContract.Tables.ARTICLE, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}

			case ARTICLE_BY_CATEGORY_ID: {
				final StringBuilder select = new StringBuilder();
				if (!TextUtils.isEmpty(selection)) {
					select.append(selection);
					select.append(" AND ");
				}
				select.append(YANAContract.Tables.ARTICLE);
				select.append('.');
				select.append(YANAContract.ArticleTable.CATEGORY_ID);
				select.append(" = ");
				select.append(YANAContract.ArticleTable.getCategoryId(uri));
				selection = select.toString();
				final int retVal = db.delete(YANAContract.Tables.ARTICLE, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}

			case ARTICLE_BY_FEED_ID_AND_CATEGORY_ID: {
				final StringBuilder select = new StringBuilder();
				if (!TextUtils.isEmpty(selection)) {
					select.append(selection);
					select.append(" AND ");
				}
				select.append(YANAContract.Tables.ARTICLE);
				select.append('.');
				select.append(YANAContract.ArticleTable.FEED_ID);
				select.append(" = ");
				select.append(YANAContract.ArticleTable.getFeedId(uri));
				select.append(" AND ");
				select.append(YANAContract.Tables.ARTICLE);
				select.append('.');
				select.append(YANAContract.ArticleTable.CATEGORY_ID);
				select.append(" = ");
				select.append(YANAContract.ArticleTable.getCategoryIdWithFeed(uri));
				selection = select.toString();
				final int retVal = db.delete(YANAContract.Tables.ARTICLE, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}

			case ARTICLE_ID: {
				final StringBuilder select = new StringBuilder();
				if (!TextUtils.isEmpty(selection)) {
					select.append(selection);
					select.append(" AND ");
				}
				select.append(YANAContract.Tables.ARTICLE);
				select.append('.');
				select.append(YANAContract.ArticleTable.ID);
				select.append(" = ");
				select.append(YANAContract.ArticleTable.getId(uri));
				selection = select.toString();
				final int retVal = db.delete(YANAContract.Tables.ARTICLE, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}

			case FEED: {
				final int retVal = db.delete(YANAContract.Tables.FEED, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}

			case FEED_ID: {
				final StringBuilder select = new StringBuilder();
				if (!TextUtils.isEmpty(selection)) {
					select.append(selection);
					select.append(" AND ");
				}
				select.append(YANAContract.Tables.FEED);
				select.append('.');
				select.append(YANAContract.FeedTable.ID);
				select.append(" = ");
				select.append(YANAContract.FeedTable.getId(uri));
				selection = select.toString();
				final int retVal = db.delete(YANAContract.Tables.FEED, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}

			case CATEGORY: {
				final int retVal = db.delete(YANAContract.Tables.CATEGORY, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}

			case CATEGORY_ID: {
				final StringBuilder select = new StringBuilder();
				if (!TextUtils.isEmpty(selection)) {
					select.append(selection);
					select.append(" AND ");
				}
				select.append(YANAContract.Tables.CATEGORY);
				select.append('.');
				select.append(YANAContract.CategoryTable.ID);
				select.append(" = ");
				select.append(YANAContract.CategoryTable.getId(uri));
				selection = select.toString();
				final int retVal = db.delete(YANAContract.Tables.CATEGORY, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}

			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "match (" + match + ") is not handled in delete");
				}
				return 0;
		}
	}

}
