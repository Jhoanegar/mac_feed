package mac.acatlan.com.macfeed.DAO;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import mac.acatlan.com.macfeed.R;

/**
 * Created by jhoan on 2/6/16.
 */
public class Entry {
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
                colorId = R.color.color_event;
                break;
        }
        return ContextCompat.getColor(context, colorId);
    }
}
