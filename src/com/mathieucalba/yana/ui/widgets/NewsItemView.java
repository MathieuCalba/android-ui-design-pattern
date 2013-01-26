package com.mathieucalba.yana.ui.widgets;

import java.util.Date;

import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mathieucalba.yana.R;
import com.mathieucalba.yana.provider.YANAContract;
import com.nostra13.universalimageloader.core.ImageLoader;


public class NewsItemView extends RelativeLayout implements IItemView {

	private ImageView mImage;
	private TextView mTitle;
	private TextView mDate;
	private TextView mPreview;
	private CharArrayBuffer mTitleBuffer;
	private CharArrayBuffer mPreviewBuffer;
	private ImageLoader mImageLoader;

	public NewsItemView(Context context) {
		super(context);
		init(context);
	}

	public NewsItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public NewsItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		final int padding = context.getResources().getDimensionPixelSize(R.dimen.margin_small);
		setPadding(padding, padding, padding, padding);

		final LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.item_feed_news, this, true);

		mImage = (ImageView) findViewById(R.id.image);
		mTitle = (TextView) findViewById(R.id.title);
		mDate = (TextView) findViewById(R.id.date);
		mPreview = (TextView) findViewById(R.id.preview);
		mTitleBuffer = new CharArrayBuffer(128);
		mPreviewBuffer = new CharArrayBuffer(256);

		mImageLoader = ImageLoader.getInstance();
	}

	@Override
	public void setData(Cursor c) {
		long timestamp = c.getLong(YANAContract.ArticleTable.PROJ_LIST.TIMESTAMP);
		timestamp = timestamp * 1000; // convert to milliseconds
		// mDate.setText(DateUtils.getRelativeDateTimeString(getContext(), timestamp, DateUtils.MINUTE_IN_MILLIS, DateUtils.DAY_IN_MILLIS, 0));
		final Date now = new Date();
		mDate.setText(DateUtils.getRelativeTimeSpanString(timestamp, now.getTime(), DateUtils.MINUTE_IN_MILLIS));

		final String imageUrl = c.getString(YANAContract.ArticleTable.PROJ_LIST.IMAGE_URL);
		if (!TextUtils.isEmpty(imageUrl)) {
			mImageLoader.displayImage(imageUrl, mImage);
		}

		final CharArrayBuffer titleBuffer = mTitleBuffer;
		c.copyStringToBuffer(YANAContract.ArticleTable.PROJ_LIST.TITLE, titleBuffer);
		mTitle.setText(titleBuffer.data, 0, titleBuffer.sizeCopied);

		final CharArrayBuffer previewBuffer = mPreviewBuffer;
		c.copyStringToBuffer(YANAContract.ArticleTable.PROJ_LIST.HEADER, previewBuffer);
		mPreview.setText(previewBuffer.data, 0, previewBuffer.sizeCopied);
	}

}
