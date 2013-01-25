package com.mathieucalba.yana.utils;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class LoaderUtils {

	public static void restartLoader(SherlockFragmentActivity activity, int id, Bundle args, LoaderCallbacks<Cursor> listener) {
		if (!activity.isFinishing()) {
			final LoaderManager loaderManager = activity.getSupportLoaderManager();
			if (loaderManager != null) {
				loaderManager.restartLoader(id, args, listener);
			}
		}
	}

	public static void restartLoader(SherlockFragment fragment, int id, Bundle args, LoaderCallbacks<Cursor> listener) {
		if (fragment.getActivity() != null && !fragment.isDetached()) {
			final LoaderManager loaderManager = fragment.getActivity().getSupportLoaderManager();
			if (loaderManager != null) {
				loaderManager.restartLoader(id, args, listener);
			}
		}
	}

}
