package com.mathieucalba.yana.provider;

import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.mathieucalba.yana.BuildConfig;
import com.mathieucalba.yana.model.FeedsData;


public class YANADatabase extends SQLiteOpenHelper {

	private static final String TAG = YANADatabase.class.getSimpleName();

	public static String DATABASE_NAME = "yana_database";
	public static int DATABASE_VERSION = 1;

	public YANADatabase(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "YANADatabase(context)");
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/* ARTICLE TABLE */
		final String articleTable = new StringBuilder() //
				.append("CREATE TABLE ").append(YANAContract.Tables.ARTICLE) //
				.append(" ( '").append(BaseColumns._ID).append("' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '") //
				.append(YANAContract.ArticleColumns.ID).append("' INTEGER NOT NULL, '") //
				.append(YANAContract.ArticleColumns.TITLE).append("' TEXT, '") //
				.append(YANAContract.ArticleColumns.IMAGE_URL).append("' TEXT, '") //
				.append(YANAContract.ArticleColumns.HEADER).append("' TEXT, '") //
				.append(YANAContract.ArticleColumns.CONTENT).append("' TEXT, '") //
				.append(YANAContract.ArticleColumns.AUTHOR).append("' TEXT, '") //
				.append(YANAContract.ArticleColumns.FEED_ID).append("' INTEGER NOT NULL DEFAULT 0, '") //
				.append(YANAContract.ArticleColumns.CATEGORY_ID).append("' INTEGER NOT NULL DEFAULT 0, '") //
				.append(YANAContract.ArticleColumns.TIMESTAMP).append("' TIMESTAMP NOT NULL );") //
				.toString();

		db.execSQL(articleTable);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, articleTable);
		}

		/* FEED TABLE */
		final String feedTable = new StringBuilder() //
				.append("CREATE TABLE ").append(YANAContract.Tables.FEED) //
				.append(" ( '").append(BaseColumns._ID).append("' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '") //
				.append(YANAContract.FeedColumns.ID).append("' INTEGER NOT NULL, '") //
				.append(YANAContract.FeedColumns.NAME).append("' TEXT );") //
				.toString();

		db.execSQL(feedTable);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, feedTable);
		}

		final Set<FeedsData.Feed> feeds = FeedsData.getFeeds();
		for (final Iterator<FeedsData.Feed> iterator = feeds.iterator(); iterator.hasNext();) {
			final FeedsData.Feed feed = iterator.next();
			final String feedInsert = createFeedInsert(feed);

			db.execSQL(feedInsert);

			if (BuildConfig.DEBUG) {
				Log.d(TAG, feedInsert);
			}
		}

		/* CATEGORY TABLE */
		final String categoryTable = new StringBuilder() //
				.append("CREATE TABLE ").append(YANAContract.Tables.CATEGORY) //
				.append(" ( '").append(BaseColumns._ID).append("' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '") //
				.append(YANAContract.CategoryColumns.ID).append("' INTEGER NOT NULL, '") //
				.append(YANAContract.CategoryColumns.NAME).append("' TEXT );") //
				.toString();

		db.execSQL(categoryTable);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, categoryTable);
		}

		final Set<FeedsData.Category> categories = FeedsData.getCategories();
		for (final Iterator<FeedsData.Category> iterator = categories.iterator(); iterator.hasNext();) {
			final FeedsData.Category category = iterator.next();
			final String categoryInsert = createCategoryInsert(category);

			db.execSQL(categoryInsert);

			if (BuildConfig.DEBUG) {
				Log.d(TAG, categoryInsert);
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 0) {
			final String articleTable = new StringBuilder() //
					.append("DROP TABLE ").append(YANAContract.Tables.ARTICLE) //
					.toString();

			db.execSQL(articleTable);

			if (BuildConfig.DEBUG) {
				Log.d(TAG, articleTable);
			}

			final String feedTable = new StringBuilder() //
					.append("DROP TABLE ").append(YANAContract.Tables.FEED) //
					.toString();

			db.execSQL(feedTable);

			if (BuildConfig.DEBUG) {
				Log.d(TAG, feedTable);
			}

			final String categoryTable = new StringBuilder() //
					.append("DROP TABLE ").append(YANAContract.Tables.CATEGORY) //
					.toString();

			db.execSQL(categoryTable);

			if (BuildConfig.DEBUG) {
				Log.d(TAG, categoryTable);
			}

			onCreate(db);
		}
	}

	private String createFeedInsert(FeedsData.Feed feed) {
		return new StringBuilder() //
				.append("INSERT INTO ").append(YANAContract.Tables.FEED) //
				.append(" ( ").append(YANAContract.FeedColumns.ID).append(", ").append(YANAContract.FeedColumns.NAME).append(" )") //
				.append(" VALUES ( ").append(feed.id).append(", '").append(feed.name).append("' )") //
				.toString();
	}

	private String createCategoryInsert(FeedsData.Category category) {
		return new StringBuilder() //
				.append("INSERT INTO ").append(YANAContract.Tables.CATEGORY) //
				.append(" ( ").append(YANAContract.CategoryColumns.ID).append(", ").append(YANAContract.CategoryColumns.NAME).append(" )") //
				.append(" VALUES ( ").append(category.id).append(", '").append(category.name).append("' )") //
				.toString();
	}
}
