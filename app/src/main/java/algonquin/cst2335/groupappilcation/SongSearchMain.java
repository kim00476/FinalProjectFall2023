package algonquin.cst2335.groupappilcation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.ReferenceQueue;
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

    ArrayList<SongSearchItem> favList = new ArrayList<SongSearchItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySongSearchMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences();
        buildToolbar();
        showToast();
        showSnackBar();
        showAlertDialog();
        connectionDatabase();

         // main and should out of button
        binding.searchButton.setOnClickListener(click -> {
            searchButtonClick();
        });

    }//onCreate last

    private void connectionDatabase() {

        queue = Volley.newRequestQueue(this);

        SongDatabase database = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "Song_db")
                .fallbackToDestructiveMigration() // Schema DB version
                .allowMainThreadQueries()         // Main Thread DB : IO
                .build();

        songDao = database.songDAO(); // interface
    }

    private void buildToolbar() {

        setSupportActionBar(binding.SongToolbar);
    }

    private void sharedPreferences() {
        sharedPreferences = getSharedPreferences("SongData", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("song", "");
        binding.editText.setText(value);
    }

    private void searchButtonClick() {
        SharedPreferences.Editor editor = getSharedPreferences("SongData", Context.MODE_PRIVATE).edit();
        search = findViewById(R.id.editText);
        String searched = search.getText().toString();
        editor.putString("SongSearched", searched);
        editor.apply();
    }

    private void showToast() {

    }

    private void showSnackBar() {

    }

    private void showAlertDialog() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sharedPreferences = getSharedPreferences("SongData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value = search.getText().toString();
        editor.putString("song", value);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.song_menu, menu);
        return true;
    }

    public void addToFavorite(View view) {

        String songID = view.getContentDescription().toString();
//        Song song = songCollection.searchById(songID)
        Toast.makeText(this, "button is clicked", Toast.LENGTH_LONG).show();
    }

    public void goToFavoriteSong(View view) {

        Intent intent = new Intent(this, FavoriteSong.class);
        startActivity(intent);

//        for (int i = 0; i < SongSearchMain.favList.size(); i++)
//        for (int i = 0; i < favList.size(); i++){
//            Log.d("Test", favList.get(i).getTitle());
//        }
    }
}//main last