package com.mathieucalba.yana.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mathieucalba.yana.R;
import com.mathieucalba.yana.provider.YANAContract;


public class CategoriesMenuAdapter extends CursorAdapter {

	private final LayoutInflater mInflater;
	private final Context mContext;

	public CategoriesMenuAdapter(Context context, Cursor cursor) {
		super(context, cursor, 0);

		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position > 0) {
			return super.getView(position - 1, convertView, parent);
		} else {
			View v;
			if (convertView == null) {
				v = newView(mContext, null, parent);
			} else {
				v = convertView;
			}
			bindView(v, mContext, null);
			return v;
		}
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (position > 0) {
			return super.getDropDownView(position - 1, convertView, parent);
		} else {
			View v;
			if (convertView == null) {
				v = newDropDownView(mContext, null, parent);
			} else {
				v = convertView;
			}
			bindView(v, mContext, null);
			return v;
		}
	}

	@Override
	public int getCount() {
		return super.getCount() > 0 ? super.getCount() + 1 : 0;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final TextView v = (TextView) mInflater.inflate(R.layout.item_menu_action_bar, parent, false);
		return v;
	}

	@Override
	public View newDropDownView(Context context, Cursor cursor, ViewGroup parent) {
		final TextView v = (TextView) mInflater.inflate(R.layout.item_menu_action_bar, parent, false);
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final TextView txt = (TextView) view;
		txt.setText(cursor != null ? cursor.getString(YANAContract.CategoryTable.PROJ.NAME) : mContext.getString(R.string.categories_all));
	}

	public void bindDropDownView(View view, Context context, Cursor cursor) {
		final TextView txt = (TextView) view;
		txt.setText(cursor != null ? cursor.getString(YANAContract.CategoryTable.PROJ.NAME) : mContext.getString(R.string.categories_all));
	}

}
