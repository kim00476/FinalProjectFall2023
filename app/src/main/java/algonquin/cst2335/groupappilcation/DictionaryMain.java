package algonquin.cst2335.groupappilcation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;

import algonquin.cst2335.groupappilcation.databinding.ActivityDictionaryMainBinding;


public class DictionaryMain extends AppCompatActivity {

ActivityDictionaryMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDictionaryMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.dictionaryToolbar);

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);

            getMenuInflater().inflate(R.menu.dictionary_menu, menu);

            return true;
    }
}