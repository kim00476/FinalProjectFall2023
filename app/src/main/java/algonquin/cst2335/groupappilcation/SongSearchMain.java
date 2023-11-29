package algonquin.cst2335.groupappilcation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.groupappilcation.databinding.ActivitySongSearchMainBinding;


public class SongSearchMain extends AppCompatActivity {
    ActivitySongSearchMainBinding binding;
    EditText search;
    SharedPreferences sharedPreferences;

    SongSearchItemDAO songDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SongDatabase database = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "Song_db")
                .fallbackToDestructiveMigration() // Schema DB version
                .allowMainThreadQueries()         // Main Thread DB : IO
                .build();

        songDao = database.songDAO(); // interface



        binding = ActivitySongSearchMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.SongToolbar);

        sharedPreferences = getSharedPreferences("SongData", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("song", "");
        binding.editText.setText(value);

        search = findViewById(R.id.editText); // main and should out of button

        binding.searchButton.setOnClickListener(click -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String searched = search.getText().toString();
            editor.putString("SongSearched", searched);
            editor.apply();

            String stringUrl = null;
            try {
                stringUrl = "https://api.deezer.com/search/artist/?q="
                        + URLEncoder.encode(searched, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }


        });

    }//onCreate last

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
}//last