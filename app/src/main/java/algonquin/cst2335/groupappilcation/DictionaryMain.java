package algonquin.cst2335.groupappilcation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.groupappilcation.Data.DictionaryModel;
import algonquin.cst2335.groupappilcation.databinding.ActivityDictionaryMainBinding;

public class DictionaryMain extends AppCompatActivity {

    private ActivityDictionaryMainBinding binding;
    private RequestQueue queue;
    private DictionaryAdapter adapter;
    private List<DictionaryItem> dataList;
    private List<String> searchHistory;
    private ArrayList<DictionaryItem> dictionaryItem;
    private DictionaryModel dictionaryModel;
    private DictionaryItemDAO wDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
        initializeVariables();
        setupToolbar();
        setupSharedPreferences();

        binding.button.setOnClickListener(click -> {
            handleSearchButtonClick();
        });

        setupRecyclerView();

    }

    private void initializeVariables() {
        binding = ActivityDictionaryMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = Volley.newRequestQueue(this);
        dataList = new ArrayList<>();
        searchHistory = new ArrayList<>();
        adapter = new DictionaryAdapter(dictionaryModel, dataList);


        DictionaryDatabase db = Room.databaseBuilder(getApplicationContext(), DictionaryDatabase.class, "dictionary-db")
                .build();
        wDAO = db.dictionaryItemDAO();

    }



    private void setupViewModel() {
        dictionaryModel = new ViewModelProvider(this).get(DictionaryModel.class);
        dictionaryModel.selectedDefinition.observe(this, (newDefinitionValue) ->
                showDefinitionFragment(newDefinitionValue));
    }

    private void showDefinitionFragment(DictionaryItem item) {
        DictionaryFragment dictionaryFragment = new DictionaryFragment(item);
        FragmentManager fMgr = getSupportFragmentManager();
        FragmentTransaction tx = fMgr.beginTransaction();
        tx.addToBackStack("");
        tx.add(R.id.fragmentLocation, dictionaryFragment);
        tx.commit();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.dictionaryToolbar);
    }

    private void setupSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MyDictionaryData", Context.MODE_PRIVATE);
        String wordFormSearch = prefs.getString("WordSearched", "");
        binding.dictionarySearch.setText(wordFormSearch);
    }

    private void handleSearchButtonClick() {
        SharedPreferences.Editor editor = getSharedPreferences("MyDictionaryData", Context.MODE_PRIVATE).edit();
        EditText word = findViewById(R.id.dictionarySearch);
        String wordSearched = word.getText().toString();
        editor.putString("WordSearched", wordSearched);
        editor.apply();

        String stringUrl = buildApiUrl(wordSearched);

        // Clear dataList before adding new items
        dataList.clear();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, stringUrl, null,
                (response) -> {
                    try {
                        processApiResponse(response, wordSearched);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                (error) -> {
                    error.printStackTrace();
                });

        queue.add(request);

        dictionaryItem = dictionaryModel.dictionaryItem.getValue();
        if (dictionaryItem == null)
            dictionaryModel.dictionaryItem.postValue(dictionaryItem = new ArrayList<>());

        Executor thread1 = Executors.newSingleThreadExecutor();
        thread1.execute(() -> {
            dictionaryItem.addAll(wDAO.getItemByWord(wordSearched));
            runOnUiThread(() -> binding.recycleView.setAdapter(adapter));
        });

        if (!searchHistory.contains(wordSearched)) {
            searchHistory.add(wordSearched);
        }
        invalidateOptionsMenu();
    }


    private String buildApiUrl(String wordSearched) {
        try {
            return "https://api.dictionaryapi.dev/api/v2/entries/en/" + URLEncoder.encode(wordSearched, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void processApiResponse(JSONArray response, String wordSearched) throws JSONException {
        JSONObject results = response.getJSONObject(0);
        JSONArray meanings = results.getJSONArray("meanings");

        // Clear dataList before adding new items
        dataList.clear();

        for (int i = 0; i < meanings.length(); i++) {
            JSONObject aMeaning = meanings.getJSONObject(i);
            JSONArray aDefinition = aMeaning.getJSONArray("definitions");

            for (int j = 0; j < aDefinition.length(); j++) {
                String def = aDefinition.getJSONObject(j).getString("definition");
                DictionaryItem thisItem = new DictionaryItem(wordSearched, def);
                dataList.add(thisItem);

                Executors.newSingleThreadExecutor().execute(()-> {
                        thisItem.id =(int) wDAO.insertWord(thisItem);
                });

            }

            adapter.notifyDataSetChanged();
        }
    }


    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.recycleView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.dictionary_menu, menu);

        // Access the submenu of the "History" item
        SubMenu historySubMenu = menu.findItem(R.id.history).getSubMenu();

        // Retrieve searchHistory from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyDictionaryData", Context.MODE_PRIVATE);
        String searchHistoryString = prefs.getString("SearchHistory", "");
        List<String> searchHistory = Arrays.asList(searchHistoryString.split(","));

        // Add items from searchHistory to the submenu dynamically
        for (int i = 0; i < searchHistory.size(); i++) {
            historySubMenu.add(Menu.NONE, Menu.NONE, i, searchHistory.get(i));
        }
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
                showAboutToast();
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
        String allInstructions = instruction1 + "\n" + instruction2 + "\n" + instruction3 + "\n" + instruction4;

        View customView = getLayoutInflater().inflate(R.layout.custom_snackbar, null);
        TextView textView = customView.findViewById(R.id.dictionary_instructions);
        textView.setText(allInstructions);

        Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.dictionary_instruction_sb), v -> snackbar.dismiss());

        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.addView(customView, 0);

        snackbar.show();
    }

    private void showAboutToast() {
        Toast.makeText(this, getString(R.string.dictionary_about), Toast.LENGTH_SHORT).show();
    }

    private void showHomeConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dictionary_homebtn_alrt))
                .setTitle(getString(R.string.dictionary_homebtn_alrt_title))
                .setNegativeButton(getString(R.string.dictionary_homebtn_alrt_cancel), (dialog, cl) -> {
                    // User canceled, do nothing
                })
                .setPositiveButton(getString(R.string.dictionary_homebtn_alrt_yes), (dialog, cl) -> {
                    // User confirmed, navigate back to MainActivity
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                })
                .create().show();
    }

    private void showRecipeConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dictionary_recpbtn_alrt))
                .setTitle(getString(R.string.dictionary_recpbtn_alrt_title))
                .setNegativeButton(getString(R.string.dictionary_recpbtn_alrt_cancel), (dialog, cl) -> {
                    // User canceled, do nothing
                })
                .setPositiveButton(getString(R.string.dictionary_recpbtn_alrt_yes), (dialog, cl) -> {
                    // User confirmed, navigate to RecipeSearchMain
                    startActivity(new Intent(this, RecipeSearchMain.class));
                })
                .create().show();
    }

    private void showSongConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dictionary_songbtn_alrt))
                .setTitle(getString(R.string.dictionary_songbtn_alrt_title))
                .setNegativeButton(getString(R.string.dictionary_songbtn_alrt_cancel), (dialog, cl) -> {
                    // User canceled, do nothing
                })
                .setPositiveButton(getString(R.string.dictionary_songbtn_alrt_yes), (dialog, cl) -> {
                    // User confirmed, navigate to SongSearchMain
                    startActivity(new Intent(this, SongSearchMain.class));
                })
                .create().show();
    }

    private void showSunsetConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dictionary_sunbtn_alrt))
                .setTitle(getString(R.string.dictionary_sunbtn_alrt_title))
                .setNegativeButton(getString(R.string.dictionary_sunbtn_alrt_cancel), (dialog, cl) -> {
                    // User canceled, do nothing
                })
                .setPositiveButton(getString(R.string.dictionary_sunbtn_alrt_yes), (dialog, cl) -> {
                    // User confirmed, navigate to SunsetSunriseMain
                    startActivity(new Intent(this, SunsetSunriseMain.class));
                })
                .create().show();
    }
}
