package mac.acatlan.com.macfeed.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.List;

import mac.acatlan.com.macfeed.Contracts.EntriesContract;
import mac.acatlan.com.macfeed.DAO.Entry;
import mac.acatlan.com.macfeed.FeedActivity;
import mac.acatlan.com.macfeed.FeedDbHelper;
import mac.acatlan.com.macfeed.R;
import mac.acatlan.com.macfeed.StoreEntriesTask;

/**
 * Created by jhoan on 2/7/16.
 */
public class FeedPullService extends IntentService {
    public static final String TAG = "FeedPullService";
    public static final int DEFAULT_NOTIFICATION_ID = -1;
    public static final String BROADCAST_ACTION = FeedPullService.class.getPackage() + ".NEW_ENTRIES_RECEIVED";
    public static final String EXTRA_ENTRIES = FeedPullService.class.getPackage() + "EXTRA_ENTRIES";

    public FeedPullService() {
        super(TAG);
    }

    public FeedPullService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "ALARMAAAAAAAAA");
        final SQLiteOpenHelper dbHelper = new FeedDbHelper(this);
        JsonArrayRequest request = new JsonArrayRequest(FeedActivity.REMOTE_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, "From backgroudn" + response.toString());
                new StoreNewEntriesTask().execute(dbHelper, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private class StoreNewEntriesTask extends StoreEntriesTask<Object, Void, List<Entry>> {
        @Override
        protected void onPostExecute(List<Entry> entries) {
            new ReadStoredEntriesTask().execute();
        }
    }

    private class ReadStoredEntriesTask extends AsyncTask<Object, Void, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            final SQLiteOpenHelper dbHelper = new FeedDbHelper(FeedPullService.this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Intent intent = new Intent(FeedPullService.this, FeedActivity.class);
            intent.putExtra(FeedActivity.EXTRA_READ_FROM_DATABASE_ON_CREATE, true);

            PendingIntent resultIntent = PendingIntent.getActivity(
                    FeedPullService.this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            long rowCount = DatabaseUtils.queryNumEntries(db, EntriesContract.Entry.TABLE_NAME);
            String title = FeedPullService.this.getResources().getQuantityString(R.plurals.new_entries_notification_title, (int) rowCount);
            Notification notification = new Notification.Builder(FeedPullService.this).
                    setContentTitle(title).
                    setContentText(title).
                    setPriority(Notification.PRIORITY_DEFAULT).
                    setDefaults(Notification.DEFAULT_ALL).
                    setAutoCancel(true).
                    setSmallIcon(R.drawable.ic_notification_sync).
                    setContentIntent(resultIntent).
                    build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.cancelAll();
//            if (!MyApplication.isRunning) {
            Log.i(TAG, "NOTIFICAONDOOOOO");
                notificationManager.notify(DEFAULT_NOTIFICATION_ID, notification);
//            }
            return null;
        }
    }
}
