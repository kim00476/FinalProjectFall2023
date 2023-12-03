package algonquin.cst2335.groupappilcation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import algonquin.cst2335.groupappilcation.databinding.ActivitySongSearchMainBinding;


public class SongSearchMain extends AppCompatActivity {
    private ActivitySongSearchMainBinding binding;
    private EditText search;
    private SharedPreferences sharedPreferences;
    private SongSearchItemDAO songDao;

    private RequestQueue queue;

    private String artistName;

    private RecyclerView recyclerView;

    private RequestQueue requestQueue;

    ArrayList<SongSearchItem> favList = new ArrayList<SongSearchItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences();
        connectionDatabase();

        queue = Volley.newRequestQueue(this);

        binding = ActivitySongSearchMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.SongToolbar);

        sharedPreferences = getSharedPreferences("SongData", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("song", "");
        binding.editText.setText(value);

//        recyclerView = findViewById(R.id.song_recycleView); //initialize RecyclerView
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

         // main and should out of button
        binding.searchButton.setOnClickListener(click -> {

//            try {
//
//
//                artistName = binding.editText.getText().toString();
//
//                String stringUrl = "https://api.deezer.com/search/artist/?q="
//                        + URLEncoder.encode(artistName, "UTF-8");
//
//                // Clear dataList before adding new items
//
//                Log.d("SongSearchMain", "URL: " + stringUrl);
//                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringUrl, null,
//                        (response) -> {
//                            try {
//                                JSONArray results = response.getJSONArray("data");
//                                JSONObject position0 = results.getJSONObject(0);
//                                for (int i = 0; i < results.length(); i++) {
//                                    JSONObject anAlbum = results.getJSONObject(i);
//                                    String tracklist = anAlbum.getString("tracklist");
//                                    JsonObjectRequest songLookup =
//                                            new JsonObjectRequest(Request.Method.GET, tracklist, null,
//                                            (songRequest) -> {
//                                                try {
//                                                    JSONArray albumData = songRequest.getJSONArray("data");
//
//                                                    for (int j = 0; j < albumData.length(); j++)
//                                                    {
//                                                        JSONObject thisAlbum = albumData.getJSONObject(j);
//                                                        String title = thisAlbum.getString("title");
//                                                        int duration = thisAlbum.getInt("duration");
//                                                        String album = anAlbum.getString("picture_small");
//                                                        String name = anAlbum.getString("name");
//
//                                                        runOnUiThread(()->{
//                                                            binding.textView.setText("Title" +title);
//                                                            binding.textView.setVisibility(View.VISIBLE);
//
//
//
//                                                        });
//                                                    }
//                                                } catch (JSONException e) {
//                                                    throw new RuntimeException(e);
//                                                }
//                                            },
//                                            (songError) -> {     });
//
//                                    queue.add(songLookup);
//                                }
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
//                        },
//                        (error) -> {
//                            error.printStackTrace();
//                        });
//            } catch (UnsupportedEncodingException e) {
//                throw new RuntimeException(e);
//            }
        });//click listener last
    }//onCreate last

    private void connectionDatabase() {

        SongDatabase database = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "Song_db")
                .fallbackToDestructiveMigration() // Schema DB version
                .allowMainThreadQueries()         // Main Thread DB : IO
                .build();

        songDao = database.songDAO(); // interface
    } //connectionDatabase last

    private void buildToolbar() {


    } //toolbar last

    private void sharedPreferences() {

    }

    private void searchButtonClick() throws UnsupportedEncodingException {
        SharedPreferences.Editor editor = getSharedPreferences("SongData", Context.MODE_PRIVATE).edit();
        search = findViewById(R.id.editText);
        String searched = search.getText().toString();
        editor.putString("SongSearched", searched);
        editor.apply();
    }

    private void showToast() {
        Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Test test test")
                .setMessage("This will be instruction")
                .setPositiveButton("OK", (dialog, cl) -> {
                    // close
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (search != null) {
            sharedPreferences = getSharedPreferences("SongData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String value = search.getText().toString();
            editor.putString("song", value);
            editor.commit();
            editor.apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.song_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.About) {
            showToast();
            return true;  // not showing
        } else if (itemId == R.id.help) {
            showAlertDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    public void addToFavorite(View view) {
//
//        String songID = view.getContentDescription().toString();
////        Song song = songCollection.searchById(songID)
//        Toast.makeText(this, "button is clicked", Toast.LENGTH_LONG).show();
//    }

}//main last