package mac.acatlan.com.macfeed;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import mac.acatlan.com.macfeed.Contracts.EntriesContract;

/**
 * Created by jhoan on 2/6/16.
 */
public class FeedDbHelper extends SQLiteOpenHelper {
    public static final String TAG = "FeedDbHelper";

    public FeedDbHelper(Context context) {
        super(context, EntriesContract.DATABASE_NAME, null, EntriesContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDbSql = "" +
                "CREATE TABLE " + EntriesContract.Entry.TABLE_NAME + "(" +
                EntriesContract.Entry._ID + " INTEGER PRIMARY KEY," +
                EntriesContract.Entry.COLUMN_NAME_TITLE + " TEXT UNIQUE," +
                EntriesContract.Entry.COLUMN_NAME_SUMMARY + " TEXT," +
                EntriesContract.Entry.COLUMN_NAME_CATEGORY + " TEXT," +
                EntriesContract.Entry.COLUMN_NAME_DATE + " TEXT)";

        Log.d(TAG, "OnCreate: " + createDbSql);
        db.execSQL(createDbSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EntriesContract.Entry.TABLE_NAME);
    }
}
