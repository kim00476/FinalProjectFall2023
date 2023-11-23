package algonquin.cst2335.groupappilcation;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySongSearchMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.SongToolbar);

        sharedPreferences = getSharedPreferences("SongData", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("song", "");
        binding.editText.setText(value);

        binding.searchButton.setOnClickListener(click -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            search = findViewById(R.id.editText);
            String searched = search.getText().toString();
            editor.putString("SongSearched", searched);
            editor.apply();

            String url = null;
            try {
                url = ""
                        + URLEncoder.encode(searched, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });

    }//override last

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