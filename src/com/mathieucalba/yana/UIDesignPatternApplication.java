package com.mathieucalba.yana;

import java.io.File;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.URLConnectionImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;


public class UIDesignPatternApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		final DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder().cacheInMemory() //
				.cacheOnDisc() //
				.displayer(new FadeInBitmapDisplayer(250)) //
				.showImageForEmptyUri(R.drawable.ic_launcher) //
				.showStubImage(R.drawable.ic_launcher) //
				.build();
		final File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "YANA/ImageCache");
		final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()) //
				.defaultDisplayImageOptions(defaultDisplayImageOptions) //
				.discCache(new TotalSizeLimitedDiscCache(cacheDir, new Md5FileNameGenerator(), 10 * 1024 * 1024)) //
				.imageDownloader(new URLConnectionImageDownloader()) //
				.memoryCacheSize(2 * 1024 * 1024) // 2 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO) //
				.threadPoolSize(3) //
				.threadPriority(Thread.NORM_PRIORITY - 2) //
				.build();
		ImageLoader.getInstance().init(config);
	}

}
