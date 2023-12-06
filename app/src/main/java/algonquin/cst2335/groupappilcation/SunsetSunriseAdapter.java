package algonquin.cst2335.groupappilcation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SunsetSunriseAdapter extends RecyclerView.Adapter<SunsetSunriseAdapter.ViewHolder> {

    private List<SunsetSunriseItem> items;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    // Constructor to initialize the adapter with data and click listeners
    public SunsetSunriseAdapter(List<SunsetSunriseItem> items, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
        this.items = items;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    // Method to update the data set when needed
    public void setItems(List<SunsetSunriseItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coordinate_item, parent, false);
        return new ViewHolder(view, clickListener, longClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SunsetSunriseItem item = items.get(position);
        holder.latitudeText.setText(item.getLatitude());
        holder.longitudeText.setText(item.getLongitude());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder class with click listeners
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView latitudeText;
        TextView longitudeText;

        public ViewHolder(@NonNull View itemView, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
            super(itemView);
            latitudeText = itemView.findViewById(R.id.latitudeTextView);
            longitudeText = itemView.findViewById(R.id.longitudeTextView);

            // Set the short click listener on the itemView
            itemView.setOnClickListener(view -> {
                if (clickListener != null) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });

            // Set the long click listener on the itemView
            itemView.setOnLongClickListener(view -> {
                if (longClickListener != null) {
                    longClickListener.onItemLongClick(getAdapterPosition());
                }
                return true;
            });
        }
    }

    // Interface for short click listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Interface for long click listener
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
}
