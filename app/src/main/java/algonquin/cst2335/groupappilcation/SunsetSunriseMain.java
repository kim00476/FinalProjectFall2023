package algonquin.cst2335.groupappilcation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.groupappilcation.databinding.ActivitySunsetSunriseMainBinding;

public class SunsetSunriseMain extends AppCompatActivity {
    ActivitySunsetSunriseMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySunsetSunriseMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize the toolbar
        setSupportActionBar(binding.SunsetSunriseToolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.sunrisesunset_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_item:
                Snackbar.make(binding.SunsetSunriseToolbar, "Confirm deletion?", Snackbar.LENGTH_LONG).show();
                break;

            case R.id.about_item:
                Toast.makeText(this,"Version 1.0, created by Yuyao He", Toast.LENGTH_LONG).show();
                break;

            case R.id.howToUse_item:
                instructionAlert();
        }
        return true;
    }

    private void instructionAlert() {
        String howto1 = getString(R.string.how_to_look_up_location1);
        String howto2 = getString(R.string.how_to_look_up_location2);
        String howto3 = getString(R.string.how_to_look_up_location3);
        String howto4 = getString(R.string.how_to_look_up_location4);

        String appInstruction = howto1 + "\n" + howto2 + "\n" + howto3 + "\n" + howto4;


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(appInstruction)
                .setTitle("How to use this?")
                .setNegativeButton("OK", (dialog, cl) -> {
                })
                .create().show();
    }
}