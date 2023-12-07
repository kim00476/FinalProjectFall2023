package algonquin.cst2335.groupappilcation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.groupappilcation.Data.SongViewModel;
import algonquin.cst2335.groupappilcation.databinding.ActivitySongRoomBinding;
import algonquin.cst2335.groupappilcation.databinding.ItemSongBinding;

public class SongRoom extends AppCompatActivity {
    private RecyclerView.Adapter myAdapter;
    ArrayList<SongItem> favoriteSong = new ArrayList<>();
    Executor thread = Executors.newSingleThreadExecutor();
    SongItemDAO songItemDAO;
    SongViewModel songViewModel;
    ActivitySongRoomBinding songRoomBinding;
    RecyclerView favRecyclerView;
    ItemSongBinding itemSongBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent fromPrevious = getIntent();

        SongDatabase db = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "songs").build();
        songItemDAO = db.songItemDAO();

        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);
        favoriteSong = songViewModel.listSong.getValue();

        if (favoriteSong == null) {
            songViewModel.favoriteSongsArray.postValue(favoriteSong = new ArrayList<>());
            thread.execute(() -> {
                favoriteSong.addAll(songItemDAO.getAllSongs());
                runOnUiThread(() -> songRoomBinding.recycleView.setAdapter(myAdapter));
            });
        }

        songRoomBinding = ActivitySongRoomBinding.inflate(getLayoutInflater());
        View favoritesView = songRoomBinding.getRoot();

        favRecyclerView = songRoomBinding.recycleView;
        //
        favRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Adapter for Recycle view
        favRecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<SongRoom.FavRowHolder>() {
            @NonNull
            @Override
            public SongRoom.FavRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                itemSongBinding = ItemSongBinding.inflate(getLayoutInflater());
                return new SongRoom.FavRowHolder(itemSongBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull FavRowHolder holder, int position) {
                SongItem favoriteSong = favoriteSong.get(position);
                holder.favoriteSong.setText(favoriteSong.getName());
                holder.favoriteAlbum.setText(favoriteSong.getSongTitle());
                String pathname = favoriteSong.getAlbumImage();
                Picasso.get().load(new File(pathname)).into(holder.favoriteImage);
            }

            @Override
            public int getItemCount() {
                return favoriteSong.size();
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }
        });
        setContentView(favoritesView);
    }

    class FavRowHolder extends RecyclerView.ViewHolder {
        // Sets views from the favorite_songs.xml, to maintain variables.
        TextView favoriteSong;
        TextView favoriteAlbum;
        ImageView favoriteImage;
        ImageButton favoriteDeleteBtn;

        public FavRowHolder(@NonNull View itemView) {
            super(itemView);
            favoriteSong = itemView.findViewById(R.id.favoriteSong);
            favoriteAlbum = itemView.findViewById(R.id.favoriteAlbum);
            favoriteImage = itemView.findViewById(R.id.favoriteImage);
            favoriteDeleteBtn = itemView.findViewById(R.id.favoriteDeleteBtn);

            favoriteDeleteBtn.setOnClickListener(clk -> {
                int position = getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(SongRoom.this);
                String deleteQuestion = getString(R.string.deleteQuestion);
                String deleteTitle = getString(R.string.deleteTitle);
                String confirm = getString(R.string.confirm);
                builder.setMessage(deleteQuestion)
                        .setTitle(deleteTitle).
                        setNegativeButton("No", (dialog, cl) -> {

                        })
                        .setPositiveButton(confirm, (dialog, cl) -> {
                            SongItem song = favoriteSong.get(position);

                            thread.execute(() -> {
                                SongItemDAO.deleteSong(song);
                                runOnUiThread(() -> songRoomBinding.favRecyclerView.setAdapter(myAdapter));
                            });
                            favoriteSong.remove(position);
                            myAdapter.notifyItemRemoved(position);

                            String deletedSong = getString(R.string.deletedSong);
                            String undo = getString(R.string.undo);

                            Snackbar.make(favoriteSong, deletedSong + " " + favoriteSong.getText().toString(),
                                            Snackbar.LENGTH_LONG)
                                    .setAction(undo, click -> {
                                        favoriteSong.add(position, song);
                                        myAdapter.notifyItemInserted(position);
                                        thread.execute(() ->
                                        {
                                            SongItemDAO.insertSong(song);
                                            runOnUiThread(() -> songRoomBinding.favRecyclerView.setAdapter(myAdapter));
                                        });
                                    })
                                    .show();
                        })
                        .create().show();
            });
        } //SongRoom end
    }
}
