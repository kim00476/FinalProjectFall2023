package algonquin.cst2335.groupappilcation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

import algonquin.cst2335.groupappilcation.databinding.ActivityDictionaryMainBinding;


public class DictionaryMain extends AppCompatActivity {

ActivityDictionaryMainBinding binding;
SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDictionaryMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.dictionaryToolbar);

        SharedPreferences prefs = getSharedPreferences("MyDictionaryData", Context.MODE_PRIVATE);
        String wordFormSearch = prefs.getString("WordSearched", "");
        binding.dictionarySearch.setText(wordFormSearch);

        binding.button.setOnClickListener(click -> {
            SharedPreferences.Editor editor = prefs.edit();
            EditText word = findViewById(R.id.dictionarySearch);
            String wordSearched = word.getText().toString();
            editor.putString("WordSearched", wordSearched);
            editor.apply();
        });

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);

            getMenuInflater().inflate(R.menu.dictionary_menu, menu);

            return true;
    }


}