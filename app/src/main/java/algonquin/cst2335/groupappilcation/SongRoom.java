package algonquin.cst2335.groupappilcation;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.groupappilcation.Data.SongViewModel;
import algonquin.cst2335.groupappilcation.databinding.ActivitySongRoomBinding;
import algonquin.cst2335.groupappilcation.databinding.SongListBinding;

public class SongRoom extends AppCompatActivity {
    ActivitySongRoomBinding songRoomBinding;
    SongViewModel songViewModel;
    ArrayList<SongItem> songItems;
    RecyclerView.Adapter<MyRowHolder> myAdapter;
    SongItemDAO itemDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        songRoomBinding = ActivitySongRoomBinding.inflate(getLayoutInflater());

        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);
        songItems = songViewModel.listSong.getValue();

        SongDatabase db = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "song-title").build();

        itemDAO = db.songItemDAO();

        if(songItems == null){
            songViewModel.listSong.postValue(songItems = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(()->
            {
                songItems.addAll(itemDAO.getAllMessages());

                runOnUiThread(() ->
                        songRoomBinding.recycleView.setAdapter(myAdapter));
            });
        }

        setContentView(songRoomBinding.getRoot());

//        songRoomBinding.deleteBtn.setOnClickListener(cli ->{
//            //delete the data
//        });

        songRoomBinding.recycleView.setAdapter(
                myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
                    @Override
                    public int getItemViewType(int position){
                        SongItem songList = songItems.get(position);
                        return songList.isCheckBox() ? 0: 1;
                    }
                    @NonNull
                    @Override
                    public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        SongListBinding songListBinding = SongListBinding.inflate(getLayoutInflater(), parent, false);
                        return new MyRowHolder(songListBinding.getRoot());

//                        View itemView = LayoutInflater.from(parent.getContext()).inflate(
//                                viewType == 0 ? R.layout.layout_with_checkbox : R.layout.layout_without_checkbox,
//                                parent,
//                                false
//                        );
//                        return new MyRowHolder(itemView);
                    }//onCreateViewHolder

                    @Override
                    public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                        SongItem songItem = songItems.get(position);
                        holder.songTitle.setText(songItem.getSongTitle());
                        holder.deleteBtn.setOnClickListener(v -> {
//                            deleteData(songList.getSongTitle());
                        });
                        holder.deleteCheckBox.setChecked(songItem.isCheckBox());
                        holder.deleteCheckBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
//                            songItem.setChecked(isChecked);
                            if (isChecked){

                            } else {

                            }
                        }));
////                holder.albumImage.setImageBitmap(songList.);
                    }//onBindViewHolder
                    @Override
                    public int getItemCount() {
                        return songItems.size();
                    }//getItemCount
                });//setAdaper end
        songRoomBinding.recycleView.setLayoutManager(new LinearLayoutManager(this));
    } //onCreat end

    class MyRowHolder extends RecyclerView.ViewHolder{
        public TextView songTitle;
        public ImageView albumImage;
        public CheckBox deleteCheckBox;
        public Button deleteBtn;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk ->{
                int position = getAbsoluteAdapterPosition();
                SongItem sr = songItems.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(SongRoom.this);
                builder.setMessage("Do you want to delete this message:" + songTitle.getText());
                builder.setTitle("Question");
                builder.setNegativeButton("No", (dialog, cl) -> {  });
                builder.setPositiveButton("Yes", (dialog, cl) -> {
                    /*is yes is clicked*/
                    Executor thread1 = Executors.newSingleThreadExecutor();
                    // this is on a background thread
                    thread1.execute((  ) -> {
                        //delete form database
                        itemDAO.deleteSong(sr); //which chat message to delete?

                    });
                    songItems.remove(position); //remove form the array list row: 0
                    myAdapter.notifyDataSetChanged();

                    //give feedback : anything on screen
                    Snackbar.make(songTitle, "You deleted song #" + position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", ck -> {

                                Executor thread2 = Executors.newSingleThreadExecutor();
                                // this is on a background thread
                                thread2.execute((  ) -> {

                                    itemDAO.insertSong(sr);
                                });
                                songItems.add(position,sr);
                                myAdapter.notifyDataSetChanged();
                            })
                            .show();
                });

                builder.create().show(); // this has to be last
            });
            songTitle = itemView.findViewById(R.id.songList);
            albumImage = itemView.findViewById(R.id.albumImage);
            deleteCheckBox = itemView.findViewById(R.id.deleteCheckBox);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }//MyRowHolder end
} //SongRoom end
