package mac.acatlan.com.macfeed;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import mac.acatlan.com.macfeed.Contracts.FeedContract;

/**
 * Created by jhoan on 2/6/16.
 */
public class FeedDbHelper extends SQLiteOpenHelper {
    public static final String TAG = "FeedDbHelper";

    public FeedDbHelper(Context context) {
        super(context, FeedContract.DATABASE_NAME, null, FeedContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAvisoDbSql = "" +
                "CREATE TABLE " + FeedContract.AvisoContract.TABLE_NAME + "(" +
                FeedContract.AvisoContract._ID + " INTEGER PRIMARY KEY," +
                FeedContract.AvisoContract.COLUMN_NAME_TITULO + " TEXT," +
                FeedContract.AvisoContract.COLUMN_NAME_DESCRIPCION + " TEXT," +
                FeedContract.AvisoContract.COLUMN_NAME_TIPO + " TEXT," +
                FeedContract.AvisoContract.COLUMN_NAME_FECHA_CREACION + " TEXT UNIQUE);";

        String createAdjuntoDbSql = "" +
                "CREATE TABLE " + FeedContract.AdjuntoContract.TABLE_NAME + "(" +
                FeedContract.AdjuntoContract._ID + " INTEGER PRIMARY KEY," +
                FeedContract.AdjuntoContract.COLUMN_NAME_TITULO + " TEXT," +
                FeedContract.AdjuntoContract.COLUMN_NAME_LINK + " TEXT," +
                FeedContract.AdjuntoContract.COLUMN_NAME_AVISO_ID + " INTEGER);";

        Log.d(TAG, "OnCreate: " + createAvisoDbSql);
        Log.d(TAG, "OnCreate: " + createAdjuntoDbSql);
        db.execSQL(createAvisoDbSql);
        db.execSQL(createAdjuntoDbSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FeedContract.AvisoContract.TABLE_NAME);
    }
}
