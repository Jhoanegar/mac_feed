package mac.acatlan.com.macfeed.Contracts;

import android.provider.BaseColumns;

/**
 * Created by jhoan on 2/6/16.
 */
public final class EntriesContract {
    public static final String DATABASE_NAME = "feed_entries";
    public static final int DATABASE_VERSION = 1;
    public EntriesContract() {

    }

    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUMMARY = "summary";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_CATEGORY = "category";
    }
}
