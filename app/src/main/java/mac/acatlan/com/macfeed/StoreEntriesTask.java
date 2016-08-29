package mac.acatlan.com.macfeed;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mac.acatlan.com.macfeed.Contracts.FeedContract;
import mac.acatlan.com.macfeed.DAO.Adjunto;
import mac.acatlan.com.macfeed.DAO.Aviso;

/**
 * Created by jhoan on 2/7/16.
 */
public class StoreEntriesTask<O, V, L> extends AsyncTask<Object, Void, List<mac.acatlan.com.macfeed.DAO.Aviso>> {

    private SQLiteDatabase db;
    private JSONObject response;

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected List<Aviso> doInBackground(Object... params) {
        SQLiteOpenHelper dbHelper = (SQLiteOpenHelper) params[0];
        response = (JSONObject) params[1];

        db = dbHelper.getWritableDatabase();


        return storeAvisos();
    }

    private  List<Aviso> storeAvisos() {
        List<Aviso> entriesArray = new ArrayList<>();
        try {
            entriesArray = Aviso.fromJSONArray(response.getJSONArray("avisos"));
            ContentValues avisoValues = new ContentValues();
            Aviso currentAviso;
            for (int i = 0; i < entriesArray.size(); i++) {
                currentAviso = entriesArray.get(i);
                avisoValues.put(FeedContract.AvisoContract._ID, currentAviso.getId());
                avisoValues.put(FeedContract.AvisoContract.COLUMN_NAME_TITULO, currentAviso.getTitle());
                avisoValues.put(FeedContract.AvisoContract.COLUMN_NAME_DESCRIPCION, currentAviso.getSummary());
                avisoValues.put(FeedContract.AvisoContract.COLUMN_NAME_FECHA_CREACION, currentAviso.getDate());
                avisoValues.put(FeedContract.AvisoContract.COLUMN_NAME_TIPO, String.valueOf(currentAviso.getCategory()));
                db.insertWithOnConflict(FeedContract.AvisoContract.TABLE_NAME, null, avisoValues, SQLiteDatabase.CONFLICT_IGNORE);
                if (currentAviso.hasAdjuntos()) {
                    List<Adjunto> adjuntos = currentAviso.getAdjuntos();
                    Adjunto currentAdjunto;
                    ContentValues adjuntoValues = new ContentValues();
                    for (int j = 0; j < adjuntos.size(); j++) {
                        currentAdjunto = adjuntos.get(j);
                        adjuntoValues.put(FeedContract.AdjuntoContract._ID, currentAdjunto.getId());
                        adjuntoValues.put(FeedContract.AdjuntoContract.COLUMN_NAME_TITULO, currentAdjunto.getTitulo());
                        adjuntoValues.put(FeedContract.AdjuntoContract.COLUMN_NAME_LINK, currentAdjunto.getLink());
                        adjuntoValues.put(FeedContract.AdjuntoContract.COLUMN_NAME_AVISO_ID, currentAviso.getId());
                        db.insertWithOnConflict(FeedContract.AdjuntoContract.TABLE_NAME, null, adjuntoValues, SQLiteDatabase.CONFLICT_IGNORE);
                    }
                }
            }
            db.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return entriesArray;
    }


    @Override
    protected void onPostExecute(final List<Aviso> entries) {

    }
}
