package com.mathieucalba.yana.receivers;

import java.lang.ref.WeakReference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


public final class InitDataReceiver extends BroadcastReceiver {

	public static final String ACTION_INIT_DATA_CHANGE_STATE = "com.mathieucalba.yana.ACTION_INIT_DATA_CHANGE_STATE";
	public static final String EXTRA_STATE = "com.mathieucalba.yana.EXTRA_STATE";

	public interface InitDataReceiverListener {
		void onInitChangeState(boolean isRefresing);
	}

	private final WeakReference<InitDataReceiver.InitDataReceiverListener> mRefListener;

	public InitDataReceiver(InitDataReceiver.InitDataReceiverListener listener) {
		super();

		mRefListener = new WeakReference<InitDataReceiver.InitDataReceiverListener>(listener);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent != null) {
			final InitDataReceiver.InitDataReceiverListener listener = mRefListener.get();
			if (listener != null) {
				final boolean state = intent.getBooleanExtra(EXTRA_STATE, false);
				listener.onInitChangeState(state);
			}
		}
	}

	public static IntentFilter getIntentFilter() {
		return new IntentFilter(ACTION_INIT_DATA_CHANGE_STATE);
	}

}
