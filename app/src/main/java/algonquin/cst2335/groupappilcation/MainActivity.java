package algonquin.cst2335.groupappilcation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.groupappilcation.databinding.ActivityMainBinding;

/**
 * MainActivity is the main entry point of the application.
 */
public class MainActivity extends AppCompatActivity {

    /** Binding for the main activity layout */
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Inflating the layout using view binding */
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /** Set click listeners for different buttons in the layout */

        /** Dictionary Button */
        binding.dictionaryButton.setOnClickListener( click -> {

            /** Start DictionaryMain activity */
            startActivity(new Intent(this, DictionaryMain.class));
            /** Display a toast message */
            Toast.makeText(this, getString(R.string.main_toast_dict), Toast.LENGTH_LONG).show();
        });

        /** Recipe button */
        binding.recipeButton.setOnClickListener( click -> {

            /** Start RecipeMain activity */
            startActivity(new Intent(this, RecipeSearchMain.class));
            /** Display a toast message */
            Toast.makeText(this, "Click this button", Toast.LENGTH_LONG).show();
            /** Display a snackbar message */
            Snackbar.make(binding.recipeButton, "Click this button", Snackbar.LENGTH_LONG).show();
        });

        /** Sunruse Button */
        binding.sunriseButton.setOnClickListener( click -> {

            /** Start the SunriseMain activity */
            startActivity(new Intent(this, SunsetSunriseMain.class));
            /** Display a toast message */
            Toast.makeText(this, "Click this button", Toast.LENGTH_LONG).show();
            /** Display a Snackbar */
            Snackbar.make(binding.sunriseButton, "Click this button", Snackbar.LENGTH_LONG).show();
        });

        /** Song button */
        binding.songButton.setOnClickListener( click -> {

            /** Display an alert dialog before moving to the song Main */
            AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
            ad.setMessage("Do you still want to search the song?")
              .setTitle("Question")

              .setNegativeButton("No", (dialog, cl) -> {
                  /** Display a toast with No option */
                  Toast.makeText(this, "You clicked No.", Toast.LENGTH_LONG).show();
                    })

              .setPositiveButton("Yes", (dialog, cl) -> {
                  /** Display a Snackbar message with yes to move to the SongMain */
                  Snackbar.make(binding.songButton, "Go to song search", Snackbar.LENGTH_LONG)
                          .setAction("go", clk -> {
                              /** Log a message and move to the SongMain */
                              Log.d("SnackbarAction", "Snackbar Action Clicked!");
                              startActivity(new Intent(MainActivity.this, SongSearchMain.class));
                          })
                          .show();
                    }).create().show();

        });
    }
}/** end MainActivity */