package com.mathieucalba.yana.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.mathieucalba.yana.BuildConfig;
import com.mathieucalba.yana.provider.YANAContract.ArticleColumns;
import com.mathieucalba.yana.provider.YANAContract.Tables;


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
				.append("CREATE TABLE ").append(Tables.ARTICLE) //
				.append(" ( '").append(BaseColumns._ID).append("' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '") //
				.append(ArticleColumns.ID).append("' INTEGER NOT NULL, '") //
				.append(ArticleColumns.TITLE).append("' TEXT, '") //
				.append(ArticleColumns.IMAGE_URL).append("' TEXT, '") //
				.append(ArticleColumns.HEADER).append("' TEXT, '") //
				.append(ArticleColumns.CONTENT).append("' TEXT, '") //
				.append(ArticleColumns.AUTHOR).append("' TEXT, '") //
				.append(ArticleColumns.TYPE).append("' TIMESTAMP NOT NULL, '") //
				.append(ArticleColumns.TIMESTAMP).append("' INTEGER NOT NULL DEFAULT 0 );") //
				.toString();

		db.execSQL(articleTable);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, articleTable);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1) {
			final String articleTable = new StringBuilder() //
					.append("DROP TABLE ").append(Tables.ARTICLE) //
					.toString();

			db.execSQL(articleTable);

			if (BuildConfig.DEBUG) {
				Log.d(TAG, articleTable);
			}
		}
	}
}
