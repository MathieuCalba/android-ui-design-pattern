package com.mathieucalba.yana.ui.widgets;

import android.database.Cursor;


public interface IItemView {

	/**
	 * Display the data from the cursor parameter on the View
	 * @param c
	 */
	public void setData(Cursor c);

}
