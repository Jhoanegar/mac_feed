package mac.acatlan.com.macfeed.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mac.acatlan.com.macfeed.DAO.Aviso;
import mac.acatlan.com.macfeed.R;

/**
 * Created by jhoan on 2/3/16.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder>{
    private static final String TAG = "FeedAdapter";
    private final Context context;
    private List<Aviso> feedItems;

    public FeedAdapter(Context context, List<Aviso> feedItems) {
        this.context = context;
        this.feedItems = feedItems;
    }

    public void setEntries(List<Aviso> feedItems) {
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
        Aviso currentAviso = feedItems.get(position);
        holder.titulo.setText(currentAviso.getTitle());
        holder.summary.setText(currentAviso.getSummary());
        holder.fechaCreacion.setText(currentAviso.getFormattedDate());
        holder.iconoAdjunto.setVisibility(currentAviso.hasAdjuntos() ? View.VISIBLE : View.GONE);
        holder.color.setBackgroundColor(currentAviso.getColor(context));

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "ItemCount: " + feedItems.size());
        return feedItems.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo;
        public TextView summary;
        public TextView fechaCreacion;
        public ViewGroup color;
        public View iconoAdjunto;
        public View share;
        public View details;

        public FeedViewHolder(View itemView) {
            super(itemView);
            titulo = (TextView) itemView.findViewById(R.id.item_feed_title);
            summary = (TextView) itemView.findViewById(R.id.item_feed_description);
            fechaCreacion = (TextView) itemView.findViewById(R.id.item_feed_date);
            iconoAdjunto = itemView.findViewById(R.id.item_feed_attachment_icon);
            color = (ViewGroup) itemView.findViewById(R.id.item_feed_color);
            share = itemView.findViewById(R.id.item_feed_show_share);
            details = itemView.findViewById(R.id.item_feed_show_details);
        }
    }
}
