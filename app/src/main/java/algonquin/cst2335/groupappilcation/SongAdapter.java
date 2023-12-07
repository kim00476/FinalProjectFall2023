package algonquin.cst2335.groupappilcation;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyRowHolder> {
    @NonNull
    @Override
    public SongAdapter.MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.MyRowHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyRowHolder extends RecyclerView.ViewHolder {
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
