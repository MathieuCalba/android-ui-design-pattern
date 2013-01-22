package com.mathieucalba.yana.utils;

import android.content.Context;
import android.content.Intent;

import com.mathieucalba.yana.services.YANAService;


public class ServiceUtils {

	public static void startInitData(Context ctx) {
		final Intent i = new Intent(ctx, YANAService.class);
		i.putExtra(YANAService.EXTRA_API_ID, YANAService.API_INIT);
		ctx.startService(i);
	}

}
