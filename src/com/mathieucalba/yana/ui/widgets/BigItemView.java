package com.mathieucalba.yana.ui.widgets;

import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mathieucalba.yana.R;
import com.mathieucalba.yana.provider.YANAContract;


public class BigItemView extends FrameLayout implements IItemView {

	private ImageView mImage;
	private TextView mTitle;
	private CharArrayBuffer mTitleBuffer;

	public BigItemView(Context context) {
		super(context);
		init(context);
	}

	public BigItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public BigItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		final int padding = context.getResources().getDimensionPixelSize(R.dimen.margin_small);
		setPadding(padding, padding, padding, padding);

		final LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.item_feed_big, this, true);

		mImage = (ImageView) findViewById(R.id.image);
		mTitle = (TextView) findViewById(R.id.title);
		mTitleBuffer = new CharArrayBuffer(128);
	}

	@Override
	public void setData(Cursor c) {
		final CharArrayBuffer titleBuffer = mTitleBuffer;
		c.copyStringToBuffer(YANAContract.ArticleTable.PROJ_LIST.TITLE, titleBuffer);
		mTitle.setText(titleBuffer.data, 0, titleBuffer.sizeCopied);
	}

}
