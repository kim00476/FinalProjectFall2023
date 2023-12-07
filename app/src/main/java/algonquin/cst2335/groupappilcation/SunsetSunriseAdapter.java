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
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public SunsetSunriseAdapter(List<SunsetSunriseItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coordinate_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SunsetSunriseItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String latitude, String longitude);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView latitudeTextView;
        private TextView longitudeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            latitudeTextView = itemView.findViewById(R.id.latitudeTextView);
            longitudeTextView = itemView.findViewById(R.id.longitudeTextView);

            // Set click and long click listeners
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    SunsetSunriseItem clickedItem = items.get(position);
                    onItemClickListener.onItemClick(clickedItem.getLatitude(), clickedItem.getLongitude());
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(position);
                    return true;
                }
                return false;
            });
        }

        public void bind(SunsetSunriseItem item) {
            latitudeTextView.setText("Latitude: " + item.getLatitude());
            longitudeTextView.setText("Longitude: " + item.getLongitude());
        }
    }
}
