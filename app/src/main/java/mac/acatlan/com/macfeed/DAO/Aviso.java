package mac.acatlan.com.macfeed.DAO;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mac.acatlan.com.macfeed.R;

/**
 * Created by jhoan on 2/6/16.
 */
public class Aviso implements Serializable {
    private static final long serialVersionUID = 0L;

    public static final char CATEGORY_ANNOUNCEMENT = 'A';
    public static final char CATEGORY_EVENT = 'E';
    public static final char CATEGORY_JOB = 'J';

    private int id;
    private String title;
    private String summary;
    private String date;
    private List<Adjunto> adjuntos;
    private int color;
    private char category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDate() {
        return date;
    }

    public String getFormattedDate() {
        DateTime date = new DateTime(this.date);
        return date.toString("dd/MM/yyyy HH:mm:ss");
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Adjunto> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<Adjunto> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public char getCategory() {
        return category;
    }

    public void setCategory(char category) {
        this.category = category;
    }

    public int getColor(Context context) {
        if (color == 0) {
            color = initializeColor(context);
        }
        return color;
    }

    private int initializeColor(Context context) {
        int colorId = 0;
        switch (category) {
            case CATEGORY_ANNOUNCEMENT:
                colorId = R.color.color_announcement;
                break;
            case CATEGORY_EVENT:
                colorId = R.color.color_event;
                break;
            case CATEGORY_JOB:
                colorId = R.color.color_job;
                break;
        }
        return ContextCompat.getColor(context, colorId);
    }

    public boolean hasAdjuntos() {
        return this.adjuntos.size() > 0;
    }

    public static ArrayList<Aviso> fromJSONArray(JSONArray arrayEntries) {
        ArrayList<Aviso> outputArray = new ArrayList<>();
        JSONObject currentObject;
        JSONObject currentAdjunto;
        /**
         * Se recorre el array del final al principio esperando que los registros más nuevos
         * se encuentren al principio
         */
        for (int i = arrayEntries.length() - 1; i >= 0 ; i--) {
            try {
                currentObject = arrayEntries.getJSONObject(i);
                Aviso newAviso = new Aviso();
                newAviso.setId(currentObject.getInt("id"));
                newAviso.setTitle(currentObject.getString("titulo"));
                newAviso.setSummary(currentObject.getString("descripcion"));
                newAviso.setDate(currentObject.getString("fecha_creacion"));
                newAviso.setCategory('A');
                if (currentObject.has("adjuntos")) {
                    JSONArray adjuntos = currentObject.getJSONArray("adjuntos");
                    newAviso.setAdjuntos(Adjunto.fromJSONArray(adjuntos));
                }
                outputArray.add(newAviso);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return outputArray;
    }


}
