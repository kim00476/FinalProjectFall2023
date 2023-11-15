package algonquin.cst2335.groupappilcation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.groupappilcation.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.dictionaryButton.setOnClickListener( click -> {

            startActivity(new Intent(this, DictionaryMain.class));
        });

        binding.recipeButton.setOnClickListener( click -> {

            startActivity(new Intent(this, RecipeSearchMain.class));
        });

        binding.sunriseButton.setOnClickListener( click -> {

            startActivity(new Intent(this, SunsetSunriseMain.class));
        });

        binding.songButton.setOnClickListener( click -> {

            startActivity(new Intent(this, SongSearchMain.class));
        });

    }
}