package algonquin.cst2335.groupappilcation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


import algonquin.cst2335.groupappilcation.Data.DictionaryModel;
import algonquin.cst2335.groupappilcation.databinding.ActivityDictionaryMainBinding;


public class DictionaryMain extends AppCompatActivity {

    ActivityDictionaryMainBinding binding;
    RequestQueue queue = null;
    private DictionaryAdapter adapter;

    private List<DictionaryItem> dataList;
    ArrayList<DictionaryItem> rDef;
    DictionaryModel dictionaryModel;
    DictionaryItemDAO wDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        DictionaryDatabase db = Room.databaseBuilder(getApplicationContext(), DictionaryDatabase.class, "dictionary-db")
//                .build();
//        wDAO = db.dictionaryItemDAO();

        dataList = new ArrayList<>();
        adapter = new DictionaryAdapter(dataList);

        queue = Volley.newRequestQueue(this);

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

            String stringUrl = null;
            try {
                stringUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/"
                        + URLEncoder.encode(wordSearched, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, stringUrl, null,
                    (response) -> {
                        try {
                            JSONObject results = response.getJSONObject(0);

                            JSONArray meanings = results.getJSONArray("meanings");

                            for (int i =0; i < meanings.length(); i++)
                            {
                            JSONObject aMeaning = meanings.getJSONObject(i);
                                JSONArray aDefinition = aMeaning.getJSONArray("definitions");

                            Log.d ("Received Definition", aDefinition);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    (error) -> {
                        error.printStackTrace();
                    });

            // Add the request to the RequestQueue
            queue.add(request);
        });

        RecyclerView recyclerView = binding.recycleView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.dictionary_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.Instructions:
                View toolbarView = findViewById(R.id.dictionaryToolbar);
                showInstructionsSnackbar(toolbarView);
                return true;

            case R.id.About:
                Toast.makeText(this, "Dictionary App made by Christopher St.Aubin", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.backToHome:
                showHomeConfirmationDialog();
                return true;

            case R.id.btnRecipe:
                showRecipeConfirmationDialog();
                return true;

            case R.id.btnSong:
                showSongConfirmationDialog();
                return true;

            case R.id.btnSunset:
                showSunsetConfirmationDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showInstructionsSnackbar(View view) {
        String instruction1 = getString(R.string.first_instruction_text);
        String instruction2 = getString(R.string.second_instruction_text);
        String instruction3 = getString(R.string.third_instruction_text);
        String instruction4 = getString(R.string.fourth_instruction_text);

        // Combine all instructions into one string
        String allInstructions = instruction1 + "\n" + instruction2 + "\n" + instruction3 + "\n" + instruction4;

        // Create a custom view for the Snackbar
        View customView = getLayoutInflater().inflate(R.layout.custom_snackbar, null);
        TextView textView = customView.findViewById(R.id.dictionary_instructions);
        textView.setText(allInstructions);

        // Create the Snackbar with a custom view
        Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("CLOSE", v -> snackbar.dismiss());

        // Set the custom view
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.addView(customView, 0);

        // Show the Snackbar
        snackbar.show();
    }

    private void showHomeConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to leave this page?")
                .setTitle("Confirmation")
                .setNegativeButton("Cancel", (dialog, cl) -> {
                    // User canceled, do nothing
                })
                .setPositiveButton("Yes", (dialog, cl) -> {
                    // User confirmed, navigate back to MainActivity
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                })
                .create().show();
    }

    private void showRecipeConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to move to the Recipe Search page?")
                .setTitle("Confirmation")
                .setNegativeButton("Cancel", (dialog, cl) -> {
                    // User canceled, do nothing
                })
                .setPositiveButton("Yes", (dialog, cl) -> {
                    // User confirmed, navigate back to MainActivity
                    Intent intent = new Intent(this, RecipeSearchMain.class);
                    startActivity(intent);
                })
                .create().show();
    }

    private void showSongConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to move to the Song Search page?")
                .setTitle("Confirmation")
                .setNegativeButton("Cancel", (dialog, cl) -> {
                    // User canceled, do nothing
                })
                .setPositiveButton("Yes", (dialog, cl) -> {
                    // User confirmed, navigate back to MainActivity
                    Intent intent = new Intent(this, SongSearchMain.class);
                    startActivity(intent);
                })
                .create().show();
    }

    private void showSunsetConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to move to the Sunset & Sunrise lookup page?")
                .setTitle("Confirmation")
                .setNegativeButton("Cancel", (dialog, cl) -> {
                    // User canceled, do nothing
                })
                .setPositiveButton("Yes", (dialog, cl) -> {
                    // User confirmed, navigate back to MainActivity
                    Intent intent = new Intent(this, SunsetSunriseMain.class);
                    startActivity(intent);
                })
                .create().show();
    }
}