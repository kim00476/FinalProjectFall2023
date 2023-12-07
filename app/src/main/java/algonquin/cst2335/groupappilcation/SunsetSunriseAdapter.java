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

    public SunsetSunriseAdapter(List<SunsetSunriseItem> items, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
        this.items = items;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
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

    public interface OnItemClickListener {
        void onItemClick(int position);
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

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    longClickListener.onItemLongClick(getAdapterPosition());
                }
                return true;
            });
        }

        public void bind(SunsetSunriseItem item) {
            latitudeTextView.setText("Latitude: " + item.getLatitude());
            longitudeTextView.setText("Longitude: " + item.getLongitude());
        }
    }
}
