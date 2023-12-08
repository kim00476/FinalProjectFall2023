package algonquin.cst2335.groupappilcation;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.groupappilcation.Data.SongViewModel;
import algonquin.cst2335.groupappilcation.databinding.ActivitySongSearchMainBinding;
import algonquin.cst2335.groupappilcation.databinding.SongFragmentBinding;
import algonquin.cst2335.groupappilcation.databinding.SongListBinding;

public class SongSearchMain extends AppCompatActivity {
    Button searchBtn;
    EditText search;
    RecyclerView recyclerView;
    protected RequestQueue queue = null;
    private ActivitySongSearchMainBinding binding;
    private SongListBinding songListBinding;
    SongViewModel songModel;
    ArrayList<SongItem> songItems = new ArrayList<>();
    private RecyclerView.Adapter myAdapter;
    SharedPreferences sharedPreferences;
    Executor thread = Executors.newSingleThreadExecutor();
    String artistName;
    SongItemDAO songItemDAO;
    SongFragmentBinding mBinding;
    SongFragment songFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SongDatabase db = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "songs").build();
        songItemDAO = db.songItemDAO();

        queue = Volley.newRequestQueue(this);

        songModel = new ViewModelProvider(this).get(SongViewModel.class);
        songItems = songModel.listSong.getValue();

        binding = ActivitySongSearchMainBinding.inflate(getLayoutInflater());

        if(songItems == null){
            songModel.listSong.postValue(songItems = new ArrayList<>());
//            Executor thread = Executors.newSingleThreadExecutor();
//            thread.execute(()->
//            {
//                songItems.addAll(songItemDAO.getAllMessages());
//                runOnUiThread(() ->
//                        mainViewModel.recycleView1.setAdapter(myAdapter));
//            });
        }

        songModel.selectedSong.observe(this, (newSongValue) ->
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLocation, songFragment)
                        .addToBackStack(null)
                        .commit());
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction tx = fragmentManager.beginTransaction();
//            SongFragment songFragment = new SongFragment( newSongValue );
//            tx.replace(R.id.fragmentLocation, songFragment);
//            tx.addToBackStack("");
//            tx.commit();

        binding = ActivitySongSearchMainBinding.inflate(getLayoutInflater());
        View songView = binding.getRoot();
        setContentView(songView);
        searchBtn = binding.searchButton;
        search = binding.artistName;
        recyclerView = binding.recyclerView1;
//        favoriteBtn = binnding.favoriteBtn;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyHolder>() {

            @NonNull
            @Override
            public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                songListBinding = songListBinding.inflate(getLayoutInflater());
                return new MyHolder(songListBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyHolder holder, int position) {
                runOnUiThread(()-> {
                    SongItem song = songItems.get(position);
                    holder.songName.setText(song.getName());
                    holder.albumName.setText(song.getSongTitle());
                    String duration = String.valueOf(song.getDuration());
                    holder.songDuration.setText(duration);
                    String pathname = song.getAlbumImage();
                    // Makes it possible to load them with string from API
                    Picasso.get().load(new File(pathname)).into(holder.albumImage);
                });
            }

            @Override
            public int getItemCount() {
                return songItems.size();
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }
        });
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String searchField = prefs.getString("SearchField", "");
        search.setText(searchField);


        searchBtn.setOnClickListener( clk -> {
            String searchAuthor = getString(R.string.searchArtistName);
            Toast.makeText(SongSearchMain.this, searchAuthor, Toast.LENGTH_SHORT).show();
            String userInput = binding.artistName.getText().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("SearchField", userInput);
            editor.apply();

            songItems.clear();
            myAdapter.notifyDataSetChanged();

            setSupportActionBar(binding.SongToolbar);


            thread = Executors.newSingleThreadExecutor();

            artistName = binding.artistName.getText().toString();
            String stringUrl = "https://api.deezer.com/search/artist/?q=" + artistName;
//
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringUrl, null,
                    (response) -> {
                        try {
                            JSONArray results = response.getJSONArray("data");
//                                JSONObject position0 = results.getJSONObject(0);
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject anAlbum = results.getJSONObject(i);
                                String tracklist = anAlbum.getString("tracklist");
                                JsonObjectRequest songLookup = new JsonObjectRequest(Request.Method.GET, tracklist, null,
                                        (songRequest) -> {
                                            try {
                                                JSONArray albumData = songRequest.getJSONArray("data");
                                                for (int j = 0; j < albumData.length(); j++) {
                                                    JSONObject thisAlbum = albumData.getJSONObject(j);
                                                    String title = thisAlbum.getString("title");
                                                    int duration = thisAlbum.getInt("duration");
                                                    JSONArray contributors = thisAlbum.getJSONArray("contributors");
                                                    for (int k = 0; k < contributors.length(); k++) {
                                                        JSONObject contributeObj = contributors.getJSONObject(k);
                                                        String album = contributeObj.getString("picture_small");
                                                        String name = contributeObj.getString("name");
//                                          String albumImage = albumData.getJSONObject(j).getString("picture_small");

                                                        // String imageUrl = "https://e-cdns-images.dzcdn.net/images/artist/" + album + ".jpg";
                                                        String pathname = getFilesDir() + "/" + album + ".jpg";
                                                        File file = new File(pathname);
                                                      /* if (file.exists()) {
//                                                            mBinding.albumImage.setImageBitmap(BitmapFactory.decodeFile(pathname));
                                                        } else {*/
                                                        ImageRequest imgReq = new ImageRequest(album,
                                                                (Response.Listener<Bitmap>) bitmap -> {
                                                                   /*     FileOutputStream fOut = null;
                                                                        try {
                                                                            fOut = openFileOutput(album + ".jpg", Context.MODE_PRIVATE);
                                                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                                                            fOut.flush();
                                                                            fOut.close();
                                                                        } catch (
                                                                                FileNotFoundException e) {
                                                                            e.printStackTrace();
                                                                        } catch (
                                                                                IOException e) {
                                                                            throw new RuntimeException(e);
                                                                        }
                                                                        mBinding.albumImage.setImageBitmap(bitmap);*/
                                                                }, 200, 200, ImageView.ScaleType.CENTER, null,
                                                                (error) -> {
                                                                });

                                                        queue.add(imgReq);
                                                        songItems.add(new SongItem(name, title,duration, album));
                                                    }
                                                }


                                                runOnUiThread(() -> {
                                                    myAdapter.notifyDataSetChanged();
                                                });
                                            } catch (JSONException e) {
                                                int k = 0;
                                                throw new RuntimeException(e);
                                            }

                                        },
                                        (songError) -> {
                                        });
                                queue.add(songLookup);

                            }

                        } catch (JSONException e) {
                            String m  = e.getMessage();
                            Log.d("Hi", m);
                            int i = 0;
                            throw new RuntimeException(e);
                        }
                    },
                    (error -> {
                        error.printStackTrace();
                    }));
            queue.add(request);
        });//click search button listener end
    }

//    String artist = binding.artistName.getText().toString();
//    String stringUrl = "";
//
//    // Handles the artist string with spaces
//            try{
//        stringUrl = "https://api.deezer.com/search/artist/?q=" + URLEncoder.encode(artist, "UTF-8");
//    } catch(
//    UnsupportedEncodingException e){
//        throw new RuntimeException(e);
//    }
//    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringUrl,
//            null, (response) -> {
//        try{
//            JSONArray artistArray = response.getJSONArray("data");
//            JSONObject artist0 = artistArray.getJSONObject(0);
//
//            // Accesses the tracklist of the searched artist
//            String tracklist = artist0.getString("tracklist");
//
//            // Json request for the tracklist
//            JsonObjectRequest songRequest = new JsonObjectRequest(Request.Method.GET, tracklist, null,
//                    (songResponse) -> {
//                        try{
//                            // Accesses to main array
//                            JSONArray songArray = songResponse.getJSONArray("data");
//                            // loops in all elements of the array
//                            for(int i = 0; i < songArray.length(); i++){
//                                JSONObject currentSong = songArray.getJSONObject(i);
//                                // Accessed to album inside data array
//                                JSONObject albumObject = currentSong.getJSONObject("album");
//
//                                // Details of the current song
//                                String songTitle = currentSong.getString("title_short");
//                                int songDuration = currentSong.getInt("duration");
//                                String albumTitle = albumObject.getString("title");
//                                String albumCover = albumObject.getString("cover");
//
//                                // Creates a song object for displaying
//                                SongItem song = new SongItem(songTitle, albumTitle, songDuration, albumCover);
//                                songItems.add(song);
//                                myAdapter.notifyItemInserted(songItems.size()-1);
//                                binding.artistName.setText("");
//                            }
//                        } catch(Exception e){
//                            e.printStackTrace();
//                        }
//                    }, (error -> {}));
//            queue.add(songRequest);
//            // END OF SONG REQUEST
//        } // end of ARTIST TRY
//        catch(Exception e){
//            e.printStackTrace();
//        }
//    }, (error -> {}));
//    // ARTISTS queue request add
//            queue.add(request);
//});
//        }


    private class MyHolder extends RecyclerView.ViewHolder {
        TextView songName;
        TextView albumName;
        TextView songDuration;
        ImageView albumImage;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk -> {
                int position = getAdapterPosition();
                SongItem selectedSong = songItems.get(position);
                songModel.selectedSong.postValue(selectedSong);
            });
            // binds using the id.
            songName = itemView.findViewById(R.id.titleView);
            albumName = itemView.findViewById(R.id.albumNameView);
            songDuration = itemView.findViewById(R.id.durationView);
            albumImage = itemView.findViewById(R.id.albumImage);


        }
    }
}