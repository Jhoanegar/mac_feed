package mac.acatlan.com.macfeed;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;

import org.json.JSONArray;

import java.util.List;

import mac.acatlan.com.macfeed.Contracts.EntriesContract;
import mac.acatlan.com.macfeed.DAO.Entry;

/**
 * Created by jhoan on 2/7/16.
 */
public class StoreEntriesTask extends AsyncTask<Object, Void, List<Entry>> {
    @Override
    protected void onPreExecute() {

    }

    @Override
    protected List<Entry> doInBackground(Object... params) {
        SQLiteOpenHelper dbHelper = (SQLiteOpenHelper) params[0];
        JSONArray responseArray = (JSONArray) params[1];
        List<Entry> entriesArray = Entry.fromJSONArray(responseArray);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        Entry currentEntry;
        for (int i = 0; i < entriesArray.size(); i++) {
            currentEntry = entriesArray.get(i);
            values.put(EntriesContract.Entry.COLUMN_NAME_TITLE, currentEntry.getTitle());
            values.put(EntriesContract.Entry.COLUMN_NAME_SUMMARY, currentEntry.getSummary());
            values.put(EntriesContract.Entry.COLUMN_NAME_DATE, currentEntry.getDate());
            values.put(EntriesContract.Entry.COLUMN_NAME_CATEGORY, String.valueOf(currentEntry.getCategory()));
            db.insertWithOnConflict(EntriesContract.Entry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
        db.close();
        return entriesArray;
    }

    @Override
    protected void onPostExecute(final List<Entry> entries) {

    }
}
