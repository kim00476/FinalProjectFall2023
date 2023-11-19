package algonquin.cst2335.groupappilcation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import algonquin.cst2335.groupappilcation.Data.DictionaryModel;
import algonquin.cst2335.groupappilcation.databinding.ActivityDictionaryMainBinding;


public class DictionaryMain extends AppCompatActivity {

ActivityDictionaryMainBinding binding;
RequestQueue queue = null;
private RecyclerView.Adapter myAdapter;
ArrayList<DictionaryItem> rDef;
DictionaryModel dictionaryModel;
DictionaryItemDAO wDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DictionaryDatabase db = Room.databaseBuilder(getApplicationContext(), DictionaryDatabase.class, "dictionary-db")
                .build();
        wDAO = db.dictionaryItemDAO();

        rDef = dictionaryModel.rDef.getValue();
        if (rDef == null)
            dictionaryModel.rDef.postValue(rDef = new ArrayList<DictionaryItem>());



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

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringUrl, null,
                    (response) -> {
                        try {

                            JSONArray meaningsArray = response.getJSONArray("meanings");

                            for (int i = 0; i < meaningsArray.length(); i++) {
                                JSONObject meaningsObject = meaningsArray.getJSONObject(i);

                                JSONArray definitionsArray = meaningsObject.getJSONArray("definitions");

                                for (int j = 0; j < definitionsArray.length(); j++) {
                                    JSONObject definitionObject = definitionsArray.getJSONObject(j);

                                    // Get the definition
                                    String definition = definitionObject.getString("definition");

                                    // Add the word and definition to the adapter
                                    myAdapter.addData(new DictionaryItem(wordSearched, definition));
                                }
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
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);

            getMenuInflater().inflate(R.menu.dictionary_menu, menu);

            return true;
    }


}