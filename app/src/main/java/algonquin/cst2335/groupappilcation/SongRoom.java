package algonquin.cst2335.groupappilcation;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.groupappilcation.Data.SongSearchModel;
import algonquin.cst2335.groupappilcation.databinding.ActivitySongRoomBinding;
import algonquin.cst2335.groupappilcation.databinding.ItemSongBinding;

public class SongRoom extends AppCompatActivity {
    ArrayList<SongSearchItem> songLists; // in the beginning, there are no messages
    ActivitySongRoomBinding binding;
    RecyclerView.Adapter<MyRowHolder> myAdapter;
    SongSearchModel songModel;
    SongSearchItemDAO mDao; //Declare the dao here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySongRoomBinding.inflate(getLayoutInflater());

        songModel = new ViewModelProvider(this).get(SongSearchModel.class);
        songLists = songModel.songLists.getValue();

        SongDatabase db = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "database-name").build();

        mDao = db.songDAO();

        if(songLists == null){
            songModel.songLists.postValue(songLists = new ArrayList<>());

            Executor thread2 = Executors.newSingleThreadExecutor();
            thread2.execute(() ->
            {
                songLists.addAll( mDao.getAllSongs() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

        setContentView(binding.getRoot());

        binding.deleteBtn.setOnClickListener(click ->{

            myAdapter.notifyDataSetChanged();
        });

        binding.recycleView.setAdapter(
                myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
                    @Override
                    public int getItemViewType(int position){
                        //determine which layout to load at row position

                        SongSearchItem songList = songLists.get(position);
                        return songList.isRemoveButton() ? 0 :1;
                    }
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ItemSongBinding binding = ItemSongBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                SongSearchItem songList = songLists.get(position);

                holder.titleView.setText(songList.getTitle());
                holder.durationView.setText(songList.getDuration());
                holder.albumNameView.setText(songList.getPicture_small());

            }

            @Override
            public int getItemCount() {
                return songLists.size();
            }
        });

    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        ImageView albumImage;
        TextView titleView, durationView, albumNameView;
        Button removeButton;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            albumImage = itemView.findViewById(R.id.albumImage);
            titleView = itemView.findViewById(R.id.titleView);
            durationView = itemView.findViewById(R.id.durationView);
            albumNameView = itemView.findViewById(R.id.albumNameView);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}//class
