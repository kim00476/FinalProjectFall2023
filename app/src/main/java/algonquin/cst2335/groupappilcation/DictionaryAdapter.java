package algonquin.cst2335.groupappilcation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.groupappilcation.Data.DictionaryModel;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {

    private final List<DictionaryItem> dataList;
    private final Handler handler;

    DictionaryModel fromParentActivity;

    public DictionaryAdapter(DictionaryModel dm, List<DictionaryItem> dataList) {
        this.dataList = dataList;
        this.handler = new Handler(Looper.getMainLooper());
        fromParentActivity = dm;
    }

    public void addData(DictionaryItem item) {
        dataList.add(item);
        notifyDataSetChangedOnMainThread();
    }

    public void clearData() {
        dataList.clear();
        notifyDataSetChangedOnMainThread();
    }

    public interface OnItemClickListener {
        void onDefinitionClick(int position, DictionaryItem item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dictionary_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DictionaryItem item = dataList.get(position);
        holder.wordTextView.setText(item.getWord());
        holder.definitionTextView.setText(item.getDefinition());
        return null;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void notifyDataSetChangedOnMainThread() {
        handler.post(this::notifyDataSetChanged);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordTextView;
        TextView definitionTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.wordTextView);
            definitionTextView = itemView.findViewById(R.id.definitionTextView);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION ) {

                    fromParentActivity.selectedDefinition.postValue(   dataList.get(position) );
                }
            });
        }
    }
}
