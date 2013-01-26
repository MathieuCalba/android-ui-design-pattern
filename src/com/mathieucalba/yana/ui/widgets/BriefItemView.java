package com.mathieucalba.yana.ui.widgets;

import java.util.Date;

import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mathieucalba.yana.R;
import com.mathieucalba.yana.provider.YANAContract;


public class BriefItemView extends LinearLayout implements IItemView {

	private TextView mTitle;
	private TextView mDate;
	private CharArrayBuffer mTitleBuffer;

	public BriefItemView(Context context) {
		super(context);
		init(context);
	}

	public BriefItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		final int padding = context.getResources().getDimensionPixelSize(R.dimen.margin_small);
		setPadding(padding, padding, padding, padding);

		setOrientation(HORIZONTAL);

		final int minHeight = context.getResources().getDimensionPixelSize(R.dimen.min_height);
		setMinimumHeight(minHeight);

		final LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.item_feed_brief, this, true);

		mTitle = (TextView) findViewById(R.id.title);
		mDate = (TextView) findViewById(R.id.date);
		mTitleBuffer = new CharArrayBuffer(128);
	}

	@Override
	public void setData(Cursor c) {
		long timestamp = c.getLong(YANAContract.ArticleTable.PROJ_LIST.TIMESTAMP);
		timestamp = timestamp * 1000; // convert to milliseconds
		final Date now = new Date();
		mDate.setText(DateUtils.getRelativeTimeSpanString(timestamp, now.getTime(), DateUtils.MINUTE_IN_MILLIS));

		final CharArrayBuffer titleBuffer = mTitleBuffer;
		c.copyStringToBuffer(YANAContract.ArticleTable.PROJ_LIST.TITLE, titleBuffer);
		mTitle.setText(titleBuffer.data, 0, titleBuffer.sizeCopied);
	}

}
