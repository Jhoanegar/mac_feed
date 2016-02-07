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
public class Entry implements Serializable {
    private static final long serialVersionUID = 0L;

    public static final char CATEGORY_ANNOUNCEMENT = 'A';
    public static final char CATEGORY_EVENT = 'E';
    public static final char CATEGORY_JOB = 'J';

    private int id;
    private String title;
    private String summary;
    private String date;
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

    public void setDate(String date) {
        this.date = date;
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

    public static ArrayList<Entry> fromJSONArray(JSONArray arrayEntries) {
        ArrayList<Entry> outputArray = new ArrayList<>();
        JSONObject currentObject;
        /**
         * Se recorre el array del final al principio esperando que los registros más nuevos
         * se encuentren al principio
         */
        for (int i = arrayEntries.length() - 1; i >= 0 ; i--) {
            try {
                currentObject = arrayEntries.getJSONObject(i);
                Entry newEntry = new Entry();
                newEntry.setTitle(currentObject.getString("title"));
                newEntry.setSummary(currentObject.getString("summary"));
                newEntry.setDate(currentObject.getString("date"));
                newEntry.setCategory(currentObject.getString("category").charAt(0));
                outputArray.add(newEntry);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return outputArray;
    }


}
