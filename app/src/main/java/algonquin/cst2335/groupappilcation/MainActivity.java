package algonquin.cst2335.groupappilcation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

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
            Toast.makeText(this, "Click this button", Toast.LENGTH_LONG).show();

            Snackbar.make(binding.dictionaryButton, "Dictonary Time!", Snackbar.LENGTH_LONG).show();
        });

        binding.recipeButton.setOnClickListener( click -> {

            startActivity(new Intent(this, RecipeSearchMain.class));
            Toast.makeText(this, "Click this button", Toast.LENGTH_LONG).show();

            Snackbar.make(binding.recipeButton, "Click this button", Snackbar.LENGTH_LONG).show();
        });

        binding.sunriseButton.setOnClickListener( click -> {

            startActivity(new Intent(this, SunsetSunriseMain.class));
            Toast.makeText(this, "Click this button", Toast.LENGTH_LONG).show();

            Snackbar.make(binding.sunriseButton, "Click this button", Snackbar.LENGTH_LONG).show();
        });

        binding.songButton.setOnClickListener( click -> {

            startActivity(new Intent(this, SongSearchMain.class));
            Toast.makeText(this, "Click this button", Toast.LENGTH_LONG).show();

            Snackbar.make(binding.songButton, "Click this button", Snackbar.LENGTH_LONG).show();
        });

    }
}