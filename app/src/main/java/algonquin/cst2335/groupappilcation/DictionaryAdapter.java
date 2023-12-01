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


/**
 * The DictionaryAdapter class is a RecyclerView adapter for displaying dictionary items in a list.
 */
public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {

    private final List<DictionaryItem> dataList;
    private final Handler handler;

    /** Reference to the parent activity's DictionaryModel */
    DictionaryModel fromParentActivity;


    /**
     * Constructor for DictionaryAdapter.
     *
     * @param dm       The DictionaryModel from the parent activity.
     * @param dataList The list of dictionary items to be displayed.
     */
    public DictionaryAdapter(DictionaryModel dm, List<DictionaryItem> dataList) {
        this.dataList = dataList;
        this.handler = new Handler(Looper.getMainLooper());
        fromParentActivity = dm;
    }


    /**
     * Adds a new dictionary item to the adapter's data list and notifies the UI on the main thread.
     *
     * @param item The dictionary item to be added.
     */
    public void addData(DictionaryItem item) {
        dataList.add(item);
        notifyDataSetChangedOnMainThread();
    }

    /**
     * Clears all data from the adapter and notifies the UI on the main thread.
     */
    public void clearData() {
        dataList.clear();
        notifyDataSetChangedOnMainThread();
    }


    /**
     * Interface definition for a callback to be invoked when a dictionary item is clicked.
     */
    public interface OnItemClickListener {
        void onDefinitionClick(int position, DictionaryItem item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /** Inflates the layout for each dictionary item view in the RecyclerView */
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dictionary_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /** Binds data to the ViewHolder for a given position */
        DictionaryItem item = dataList.get(position);
        holder.wordTextView.setText(item.getWord());
        holder.definitionTextView.setText(item.getDefinition());
    }

    /** Returns the total number of items in the data list */
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * Notifies the UI of data set changes on the main thread.
     */
    private void notifyDataSetChangedOnMainThread() {
        handler.post(this::notifyDataSetChanged);
    }

    /**
     * ViewHolder class for holding references to views within each dictionary item view.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordTextView;
        TextView definitionTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            /** Initializes views within the dictionary item view */
            wordTextView = itemView.findViewById(R.id.wordTextView);
            definitionTextView = itemView.findViewById(R.id.definitionTextView);

            /** Sets an OnClickListener for each dictionary item view */
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION ) {
                    /** Gets the clicked dictionary item and updates the selectedDefinition in the parent activity's DictionaryModel */
                    DictionaryItem location = dataList.get(position);
                    fromParentActivity.selectedDefinition.postValue(  location );
                }
            });
        }
    }
}
