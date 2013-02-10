package com.mathieucalba.yana.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.mathieucalba.yana.model.FeedsData;
import com.mathieucalba.yana.provider.YANAContract;
import com.mathieucalba.yana.ui.widgets.BigItemView;
import com.mathieucalba.yana.ui.widgets.BriefItemView;
import com.mathieucalba.yana.ui.widgets.IItemView;
import com.mathieucalba.yana.ui.widgets.NewsItemView;


public class FeedListAdapter extends CursorAdapter {

	public FeedListAdapter(Context context) {
		super(context, null, 0);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final int feedId = cursor.getInt(YANAContract.ArticleTable.PROJ_LIST.FEED_ID);

		View v;
		switch (feedId) {
			case FeedsData.FEED_IDS.NEWS:
				v = new NewsItemView(context);
				break;

			case FeedsData.FEED_IDS.BRIEF:
				v = new BriefItemView(context);
				break;

			case FeedsData.FEED_IDS.TEST:
				v = new BigItemView(context);
				break;

			case FeedsData.FEED_IDS.REPORT:
				v = new BigItemView(context);
				break;

			default:
				throw new IllegalStateException("You are trying to display a article type that is not impemented here !");
		}
		return v;
	}

	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		final IItemView view = (IItemView) convertView;
		view.setData(cursor);
	}

	@Override
	public int getItemViewType(int position) {
		if (mDataValid && mCursor != null) {
			if (mCursor.moveToPosition(position)) {
				final int feedId = mCursor.getInt(YANAContract.ArticleTable.PROJ_LIST.FEED_ID);
				return feedId;
			}
		}
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		return 4;
	}

}
