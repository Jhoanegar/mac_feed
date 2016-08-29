package mac.acatlan.com.macfeed.DAO;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import mac.acatlan.com.macfeed.R;

/**
 * Created by jhoan on 2/6/16.
 */
public class Adjunto implements Serializable {
    private static final long serialVersionUID = 0L;

    private int id;
    private String titulo;
    private String link;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public static ArrayList<Adjunto> fromJSONArray(JSONArray arrayEntries) {
        ArrayList<Adjunto> outputArray = new ArrayList<>();
        JSONObject currentObject;
        /**
         * Se recorre el array del final al principio esperando que los registros más nuevos
         * se encuentren al principio
         */
        for (int i = arrayEntries.length() - 1; i >= 0 ; i--) {
            try {
                currentObject = arrayEntries.getJSONObject(i);
                Adjunto newAdjunto = new Adjunto();
                newAdjunto.setId(currentObject.getInt("id"));
                newAdjunto.setTitulo(currentObject.getString("titulo"));
                newAdjunto.setLink(currentObject.getString("link"));
                outputArray.add(newAdjunto);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return outputArray;
    }


}
