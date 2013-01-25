package com.mathieucalba.yana.model;

import java.util.HashSet;
import java.util.Set;


public class FeedsData {

	public static final class FEED_IDS {
		public static final int NEWS = 1;
		public static final int BRIEF = 2;
		public static final int TEST = 3;
		public static final int REPORT = 4;
	}

	public static class Feed {
		public final int id;
		public final String name;

		public Feed(int anId, String aName) {
			super();

			id = anId;
			name = aName;
		}
	}

	public static final Set<Feed> getFeeds() {
		final Set<Feed> feeds = new HashSet<FeedsData.Feed>(4);

		feeds.add(new Feed(FEED_IDS.NEWS, "News"));
		feeds.add(new Feed(FEED_IDS.BRIEF, "Briefs"));
		feeds.add(new Feed(FEED_IDS.TEST, "Tests"));
		feeds.add(new Feed(FEED_IDS.REPORT, "Reports"));

		return feeds;
	}

	public static final class CATEGORY_IDS {
		public static final int MOBILE = 1;
		public static final int HARDWARE = 2;
		public static final int MAC = 3;
		public static final int BUSINESS = 4;
		public static final int GOOGLE = 5;
		public static final int APPLE = 6;
		public static final int MICROSOFT = 7;
	}

	public static class Category {
		public final int id;
		public final String name;

		public Category(int anId, String aName) {
			super();

			id = anId;
			name = aName;
		}
	}

	public static final Set<Category> getCategories() {
		final Set<Category> categories = new HashSet<FeedsData.Category>(7);

		categories.add(new Category(CATEGORY_IDS.MOBILE, "News"));
		categories.add(new Category(CATEGORY_IDS.HARDWARE, "Hardware"));
		categories.add(new Category(CATEGORY_IDS.MAC, "Mac"));
		categories.add(new Category(CATEGORY_IDS.BUSINESS, "Business"));
		categories.add(new Category(CATEGORY_IDS.GOOGLE, "Google"));
		categories.add(new Category(CATEGORY_IDS.APPLE, "Apple"));
		categories.add(new Category(CATEGORY_IDS.MICROSOFT, "Microsoft"));

		return categories;
	}

}
