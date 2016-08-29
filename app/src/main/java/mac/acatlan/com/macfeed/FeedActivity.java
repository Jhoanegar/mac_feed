package mac.acatlan.com.macfeed;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.widget.ProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import mac.acatlan.com.macfeed.Adapters.FeedAdapter;
import mac.acatlan.com.macfeed.DAO.Aviso;
import mac.acatlan.com.macfeed.Services.FeedPullService;
import mac.acatlan.com.macfeed.Services.MyApplication;

public class FeedActivity extends AppCompatActivity {
    private static final String TAG = "FeedActivity";
    public static final String REMOTE_URL = "http://192.168.56.1:3000/data";

    public static final String EXTRA_READ_FROM_DATABASE_ON_CREATE = "EXTRA_READ_FROM_DB_ON_CREATE";

    private RequestQueue requestQueue;
    private FeedDbHelper dbHelper;
    private RecyclerView feedRecycler;

    private FloatingActionButton fab;
    private ProgressBar feedProgressBar;

    private PendingIntent alarmIntent;

    private boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        feedRecycler = (RecyclerView) findViewById(R.id.recycler_feed);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        feedRecycler.setHasFixedSize(true);
        feedRecycler.setLayoutManager(layoutManager);

        setSupportActionBar(toolbar);

        feedProgressBar = (ProgressBar) findViewById(R.id.feed_progressbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestEntries();
            }
        });

        if (alarmIntent == null) {
            Intent intent = new Intent(this, FeedPullService.class);
            alarmIntent = PendingIntent.getService(this, 0, intent, 0);
        }

        dbHelper = new FeedDbHelper(this);
        requestQueue = Volley.newRequestQueue(this);
        requestEntries();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.isRunning = true;
        if ( alarmIntent != null ) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.isRunning = false;
        if (alarmIntent != null) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            // Programa la alarma para que se dispare en un minuto cada 15 minutos
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 60 * 1000, 60 * 1000, alarmIntent);
        }
    }

    private void requestEntries() {
        if (isUpdating)
            return;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,REMOTE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                new StoreEntries().execute(dbHelper, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        requestQueue.add(request);
    }

    private void setAdapter(List<Aviso> entries) {
        FeedAdapter currentAdapter = (FeedAdapter) feedRecycler.getAdapter();
        if (currentAdapter == null) {
            feedRecycler.setAdapter(new FeedAdapter(this, entries));
            startIntroAnimation();
        } else {
            int newEntriesCount = entries.size() - currentAdapter.getItemCount();
            if (newEntriesCount > 0) {
                currentAdapter.setEntries(entries);
                currentAdapter.notifyItemRangeInserted(0, newEntriesCount);
                feedRecycler.smoothScrollToPosition(0   );
            } else if (newEntriesCount < 0) {
                currentAdapter.setEntries(entries);
                currentAdapter.notifyDataSetChanged();
            }
        }
    }

    private void startIntroAnimation() {
        feedRecycler.setTranslationY(50);
        feedRecycler.setAlpha(0f);
        feedRecycler.animate()
                .translationY(0)
                .setDuration(400)
                .alpha(1f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class StoreEntries extends StoreEntriesTask<Object, Void, List<Aviso>> {
        @Override
        protected void onPreExecute() {
            isUpdating = true;
            feedProgressBar.setAlpha(0);
            feedProgressBar.setTranslationY(-getResources().getDimensionPixelOffset(R.dimen.top_margin_feed_progressbar) * 2);
            feedProgressBar.animate().
                    translationY(0).
                    alpha(1).
                    setDuration(200);
        }

        @Override
        protected void onPostExecute(final List<Aviso> entries) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isUpdating = false;
                    feedProgressBar.animate().
                            alpha(0).
                            translationY(-getResources().getDimension(R.dimen.top_margin_feed_progressbar) * 2).
                            setDuration(200);
                    setAdapter(entries);
                    Snackbar.make(fab, getString(R.string.feed_updated_successfully), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }, 2000);
        }
    }
}
