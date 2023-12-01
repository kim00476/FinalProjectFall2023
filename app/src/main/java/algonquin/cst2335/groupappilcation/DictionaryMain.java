package algonquin.cst2335.groupappilcation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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


/**
 * Main activity for the Dictionary application.
 */
public class DictionaryMain extends AppCompatActivity {

    /** Binding for the activity */
    private ActivityDictionaryMainBinding binding;

    /** Volley request queue for handling API requests */
    private RequestQueue queue;

    /** Adapter for the RecyclerView */
    private DictionaryAdapter adapter;

    /** List to store dictionary items for the RecyclerView */
    private List<DictionaryItem> dataList;

    /** List to store search history */
    private List<String> searchHistory;

    /** List to store dictionary items from the ViewModel */
    private ArrayList<DictionaryItem> dictionaryItem;

    /** ViewModel for managing data */
    private DictionaryModel dictionaryModel;

    /** Room Database Access Object for dictionary items */
    private DictionaryItemDAO wDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**  Initialize variables and set up UI */
        setupViewModel();
        initializeVariables();
        setupToolbar();
        setupSharedPreferences();


        /** Set up click listener for the search button */
        binding.button.setOnClickListener(click -> {
            handleSearchButtonClick();
        });

        /** Set up RecyclerView  */
        setupRecyclerView();

    }

    /**
     * Initialize variables used in the activity.
     */
    private void initializeVariables() {
        binding = ActivityDictionaryMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /** Initialize Volley request queue */
        queue = Volley.newRequestQueue(this);

        /** Initialize lists and adapter */
        dataList = new ArrayList<>();
        searchHistory = new ArrayList<>();
        adapter = new DictionaryAdapter(dictionaryModel, dataList);

        /** Initialize Room Database and DAO */
        DictionaryDatabase db = Room.databaseBuilder(getApplicationContext(), DictionaryDatabase.class, "dictionary-db")
                .build();
        wDAO = db.dictionaryItemDAO();

    }


    /**
     * Set up ViewModel for the activity.
     */
    private void setupViewModel() {
        dictionaryModel = new ViewModelProvider(this).get(DictionaryModel.class);
        dictionaryModel.selectedDefinition.observe(this, (newDefinitionValue) ->
                showDefinitionFragment(newDefinitionValue));
    }

    /**
     * Show the definition fragment when a dictionary item is selected.
     *
     * @param item The selected dictionary item.
     */
    private void showDefinitionFragment(DictionaryItem item) {
        DictionaryFragment dictionaryFragment = new DictionaryFragment(item);
        FragmentManager fMgr = getSupportFragmentManager();
        FragmentTransaction tx = fMgr.beginTransaction();
        tx.addToBackStack("");
        tx.add(R.id.fragmentLocation, dictionaryFragment);
        tx.commit();
    }

    /**
     * Set up the toolbar for the activity.
     */
    private void setupToolbar() {
        setSupportActionBar(binding.dictionaryToolbar);
    }

    /**
     * Set up shared preferences for the activity.
     */
    private void setupSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MyDictionaryData", Context.MODE_PRIVATE);
        String wordFormSearch = prefs.getString("WordSearched", "");
        binding.dictionarySearch.setText(wordFormSearch);
    }

    /**
     * Handle the click event for the search button.
     */
    private void handleSearchButtonClick() {
        SharedPreferences.Editor editor = getSharedPreferences("MyDictionaryData", Context.MODE_PRIVATE).edit();
        EditText word = findViewById(R.id.dictionarySearch);
        String wordSearched = word.getText().toString();
        editor.putString("WordSearched", wordSearched);
        editor.apply();

        /** Build API URL for the searched word */
        String stringUrl = buildApiUrl(wordSearched);

        /** Clear dataList before adding new items */
        dataList.clear();

        /** Make API request to get dictionary definitions */
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

        /** Load dictionary items from Room Database */
        dictionaryItem = dictionaryModel.dictionaryItem.getValue();
        if (dictionaryItem == null)
            dictionaryModel.dictionaryItem.postValue(dictionaryItem = new ArrayList<>());

        Executor thread1 = Executors.newSingleThreadExecutor();
        thread1.execute(() -> {
            dictionaryItem.addAll(wDAO.getItemByWord(wordSearched));
            runOnUiThread(() -> binding.recycleView.setAdapter(adapter));
        });

        /** Add searched word to search history if not already present */
        if (!searchHistory.contains(wordSearched)) {
            searchHistory.add(wordSearched);
        }

        /** Update options menu */
        invalidateOptionsMenu();
    }

    /**
     * Build the API URL for the given searched word.
     *
     * @param wordSearched The word to search for.
     * @return The formatted API URL.
     */
    private String buildApiUrl(String wordSearched) {
        try {
            return "https://api.dictionaryapi.dev/api/v2/entries/en/" + URLEncoder.encode(wordSearched, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Process the API response and update the UI with dictionary definitions.
     *
     * @param response      The JSON array response from the API.
     * @param wordSearched  The word that was searched.
     * @throws JSONException If there is an issue parsing the JSON response.
     */
    private void processApiResponse(JSONArray response, String wordSearched) throws JSONException {
        JSONObject results = response.getJSONObject(0);
        JSONArray meanings = results.getJSONArray("meanings");

        /** Clear dataList before adding new items */
        dataList.clear();

        for (int i = 0; i < meanings.length(); i++) {
            JSONObject aMeaning = meanings.getJSONObject(i);
            JSONArray aDefinition = aMeaning.getJSONArray("definitions");

            for (int j = 0; j < aDefinition.length(); j++) {
                String def = aDefinition.getJSONObject(j).getString("definition");
                DictionaryItem thisItem = new DictionaryItem(wordSearched, def);
                dataList.add(thisItem);

                /** Insert the word into Room Database */
                Executors.newSingleThreadExecutor().execute(()-> {
                        thisItem.id =(int) wDAO.insertWord(thisItem);
                });

            }

            /** Notify adapter of data changes */
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

        /** Access the submenu of the "History" item */
        SubMenu historySubMenu = menu.findItem(R.id.history).getSubMenu();

        /** Retrieve searchHistory from SharedPreferences */
        SharedPreferences prefs = getSharedPreferences("MyDictionaryData", Context.MODE_PRIVATE);
        String searchHistoryString = prefs.getString("SearchHistory", "");
        List<String> searchHistory = Arrays.asList(searchHistoryString.split(","));

        /** Add items from searchHistory to the submenu dynamically */
        for (int i = 0; i < searchHistory.size(); i++) {
            historySubMenu.add(Menu.NONE, Menu.NONE, i, searchHistory.get(i));
        }
        return true;
    }
    int position = 0;
    DictionaryFragment dictionaryFragment;
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
            case R.id.delete_item:

                /**
                 * Handle the delete action when the delete icon is clicked.
                 */
                dictionaryFragment = new DictionaryFragment(dataList.get(position));
                DictionaryItem selectedItem = dictionaryModel.selectedDefinition.getValue();
                AlertDialog.Builder builder = new AlertDialog.Builder(DictionaryMain.this);
                if (selectedItem != null && dictionaryFragment != null) {
                    builder.setMessage(getString(R.string.dict_delete_msg) + dictionaryModel.selectedDefinition.getValue().getDefinition())
                            .setTitle(getString(R.string.dict_delete_title))
                            .setNegativeButton(getString(R.string.dict_del_neg), (dialog, cl) -> {
                            })
                            .setPositiveButton(getString(R.string.dict_del_pos), (dialog, cl) -> {
                            dictionaryItem.remove(dataList.get(position));
                            adapter.notifyItemRemoved(position);

                            Executor thread1 = Executors.newSingleThreadExecutor();
                            thread1.execute(() ->{
                            getSupportFragmentManager().popBackStack();
                            wDAO.deleteWord(dictionaryModel.selectedDefinition.getValue());
                            dictionaryFragment = null;});

                            Snackbar.make(binding.getRoot(), getString(R.string.dict_del_sb) + position, Snackbar.LENGTH_LONG)
                                    .setAction(getString(R.string.dict_del_sb_undo), clk -> {
                                        Executor thread2 = Executors.newSingleThreadExecutor();
                                        thread2.execute(() -> {
                                            int id = (int) wDAO.insertWord(dictionaryModel.selectedDefinition.getValue());
                                            dictionaryModel.selectedDefinition.getValue().id = id;
                                        });

                                        dictionaryItem.add(position, dictionaryModel.selectedDefinition.getValue());
                                        adapter.notifyItemInserted(position);
                                    }).show();
                            }).create().show();
                }

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Show instructions in a Snackbar.
     *
     * @param view The view to anchor the Snackbar.
     */
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

    /**
     * Show "About" information in a Toast.
     */
    private void showAboutToast() {
        Toast.makeText(this, getString(R.string.dictionary_about), Toast.LENGTH_SHORT).show();
    }

    /**
     * Show a confirmation dialog for navigating back to the home activity.
     */
    private void showHomeConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dictionary_homebtn_alrt))
                .setTitle(getString(R.string.dictionary_homebtn_alrt_title))
                .setNegativeButton(getString(R.string.dictionary_homebtn_alrt_cancel), (dialog, cl) -> {
                    /** User canceled, do nothing */
                })
                .setPositiveButton(getString(R.string.dictionary_homebtn_alrt_yes), (dialog, cl) -> {
                    /** User confirmed, navigate back to MainActivity */
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
                    /** User canceled, do nothing */
                })
                .setPositiveButton(getString(R.string.dictionary_recpbtn_alrt_yes), (dialog, cl) -> {
                    /** User confirmed, navigate to RecipeSearchMain */
                    startActivity(new Intent(this, RecipeSearchMain.class));
                })
                .create().show();
    }

    private void showSongConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dictionary_songbtn_alrt))
                .setTitle(getString(R.string.dictionary_songbtn_alrt_title))
                .setNegativeButton(getString(R.string.dictionary_songbtn_alrt_cancel), (dialog, cl) -> {
                    /** User canceled, do nothing */
                })
                .setPositiveButton(getString(R.string.dictionary_songbtn_alrt_yes), (dialog, cl) -> {
                    /** User confirmed, navigate to SongSearchMain */
                    startActivity(new Intent(this, SongSearchMain.class));
                })
                .create().show();
    }

    private void showSunsetConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dictionary_sunbtn_alrt))
                .setTitle(getString(R.string.dictionary_sunbtn_alrt_title))
                .setNegativeButton(getString(R.string.dictionary_sunbtn_alrt_cancel), (dialog, cl) -> {
                    /** User canceled, do nothing */
                })
                .setPositiveButton(getString(R.string.dictionary_sunbtn_alrt_yes), (dialog, cl) -> {
                    /** User confirmed, navigate to SunsetSunriseMain */
                    startActivity(new Intent(this, SunsetSunriseMain.class));
                })
                .create().show();
    }
}
