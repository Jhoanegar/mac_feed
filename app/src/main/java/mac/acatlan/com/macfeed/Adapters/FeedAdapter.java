package mac.acatlan.com.macfeed.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mac.acatlan.com.macfeed.DAO.Entry;
import mac.acatlan.com.macfeed.R;

/**
 * Created by jhoan on 2/3/16.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder>{
    private static final String TAG = "FeedAdapter";
    private final Context context;
    private List<Entry> feedItems;

    public FeedAdapter(Context context, List<Entry> feedItems) {
        this.context = context;
        this.feedItems = feedItems;
    }

    public void setEntries(List<Entry> feedItems) {
        this.feedItems = feedItems;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feed, parent, false);

        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        Entry currentEntry = feedItems.get(position);
        holder.title.setText(currentEntry.getTitle());
        holder.summary.setText(currentEntry.getSummary());
        holder.date.setText(currentEntry.getDate());
        holder.color.setBackgroundColor(currentEntry.getColor(context));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "ItemCount: " + feedItems.size());
        return feedItems.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView summary;
        public TextView date;
        public ViewGroup color;
        public View share;
        public View details;

        public FeedViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_feed_title);
            summary = (TextView) itemView.findViewById(R.id.item_feed_description);
            date = (TextView) itemView.findViewById(R.id.item_feed_date);
            color = (ViewGroup) itemView.findViewById(R.id.item_feed_color);
            share = itemView.findViewById(R.id.item_feed_show_share);
            details = itemView.findViewById(R.id.item_feed_show_details);
        }
    }
}
