package algonquin.cst2335.groupappilcation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.groupappilcation.databinding.ActivityRecipeSearchMainBinding;

public class RecipeSearchMain extends AppCompatActivity {
    ActivityRecipeSearchMainBinding binding;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecipeSearchMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.Toolbar);

        sharedPreferences = getPreferences(Activity.MODE_PRIVATE);
        String savedText = sharedPreferences.getString("search_prompt", "");
        binding.EditText.setText(savedText);

        binding.SearchButton.setOnClickListener(click -> {
            saveData();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_toast) {
            showToast();
            return true;
        } else if (itemId == R.id.action_snackbar) {
            showSnackbar();
            return true;
        } else if (itemId == R.id.action_alert_dialog) {
            showAlertDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showToast() {
        Toast.makeText(this, "This is a Toast notification", Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar() {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, "This is a Snackbar", Snackbar.LENGTH_SHORT).show();
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Alert Dialog")
                .setMessage("This is an Alert Dialog")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void saveData() {
        // Save the current text from the EditText to SharedPreferences
        String textToSave = binding.EditText.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("search_prompt", textToSave);
        editor.apply();
    }
}