package algonquin.cst2335.groupappilcation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.groupappilcation.Data.SunsetSunriseModel;
import algonquin.cst2335.groupappilcation.databinding.ActivitySunsetSunriseMainBinding;

public class SunsetSunriseMain extends AppCompatActivity {
    ActivitySunsetSunriseMainBinding binding;
    SunsetSunriseAdapter adapter;
    RequestQueue queue = null;
    SunsetSunriseItemDAO iDao;
    ArrayList<SunsetSunriseItem> items = new ArrayList<>();
    SunsetSunriseModel itemModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialization();
        setViewModel();
        toolBar();
        setupSharedPreferences();
        setupRecyclerView();

        binding.searchButton.setOnClickListener(click -> {
            String inputLatitude = binding.latitudeInput.getText().toString();
            String inputLongitude = binding.longitudeInput.getText().toString();

            SharedPreferences.Editor editor = getSharedPreferences("MyLocationData", Context.MODE_PRIVATE).edit();
            editor.putString("Latitude", inputLatitude);
            editor.putString("Longitude", inputLongitude);
            editor.apply();

            if (isValidLocation(inputLatitude, inputLongitude)) {
                try {
                    String latitude = URLEncoder.encode(inputLatitude, "UTF-8");
                    String longitude = URLEncoder.encode(inputLongitude, "UTF-8");

                    String url = "https://api.sunrisesunset.io/json?lat=" + latitude + "&lng=" + longitude + "&timezone=CA&date=today";

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            (response) -> {
                                try {
                                    JSONObject mainObj = response.getJSONObject("results");
                                    String sunrise = mainObj.getString("sunrise");
                                    String sunset = mainObj.getString("sunset");
                                    String date = mainObj.getString("date");

                                    SunsetSunriseItem newItem = new SunsetSunriseItem(latitude, longitude, sunrise, sunset, date);

                                    SunsetSunriseFragment fragment = new SunsetSunriseFragment(newItem, iDao);

                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragmentLocation, fragment)
                                            .addToBackStack(null)
                                            .commit();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }, (error) -> {
                    });

                    queue.add(request);

                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(this, "Invalid location. Please enter valid latitude and longitude.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initialization() {
        binding = ActivitySunsetSunriseMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = Volley.newRequestQueue(this);

        SunsetSunriseDatabase db = Room.databaseBuilder(getApplicationContext(), SunsetSunriseDatabase.class, "sun-db")
                .build();
        iDao = db.isDAO();

        Executor thread1 = Executors.newSingleThreadExecutor();
        thread1.execute(() -> {
            List<SunsetSunriseItem> fromDatabase = iDao.getAllCoordinates();
            items.addAll(fromDatabase);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.sunrisesunset_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_item:
                Snackbar.make(binding.SunsetSunriseToolbar, "Confirm deletion?", Snackbar.LENGTH_LONG).show();
                break;

            case R.id.about_item:
                Toast.makeText(this, "Version 1.0, created by Yuyao He", Toast.LENGTH_LONG).show();
                break;

            case R.id.howToUse_item:
                instructionAlert();
        }
        return true;
    }

    private void instructionAlert() {
        // Needs to be done
    }

    public boolean isValidLocation(String inputLatitude, String inputLongitude) {
        double parsedLatitude = Double.parseDouble(inputLatitude);
        double parsedLongitude = Double.parseDouble(inputLongitude);
        return parsedLatitude >= -90.0 && parsedLatitude <= 90.0 &&
                parsedLongitude >= -180.0 && parsedLongitude <= 180.0;
    }

    private void onItemLongClick(int position) {
        SunsetSunriseItem selectedItem = items.get(position);

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> iDao.deleteItem(selectedItem));

        items.remove(position);
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
    }

    private void setupSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MyLocationData", Context.MODE_PRIVATE);
        String searchedLat = prefs.getString("Latitude", "");
        String searchedLong = prefs.getString("Longitude", "");
        binding.latitudeInput.setText(searchedLat);
        binding.longitudeInput.setText(searchedLong);
    }

    public void toolBar() {
        setSupportActionBar(binding.SunsetSunriseToolbar);
    }

    public void setViewModel() {
        itemModel = new ViewModelProvider(this).get(SunsetSunriseModel.class);
        items = itemModel.items;
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.sunsetSunriseRecycleView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SunsetSunriseAdapter(items,
                position -> {
                    SunsetSunriseItem selectedItem = items.get(position);
                    itemModel.selected.observe(this, newSelected -> {
                        String latitude;
                        String longitude;

                        try {
                            latitude = URLEncoder.encode(selectedItem.getLatitude(), "UTF-8");
                            longitude = URLEncoder.encode(selectedItem.getLongitude(), "UTF-8");

                            String url = "https://api.sunrisesunset.io/json?lat=" + latitude + "&lng=" + longitude + "&timezone=CA&date=today";

                            String finalLatitude = latitude;
                            String finalLongitude = longitude;
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                                    response -> {
                                        try {
                                            JSONObject mainObj = response.getJSONObject("results");
                                            String sunrise = mainObj.getString("sunrise");
                                            String sunset = mainObj.getString("sunset");
                                            String date = mainObj.getString("date");

                                            SunsetSunriseItem newItem = new SunsetSunriseItem(finalLatitude, finalLongitude, sunrise, sunset, date);
                                            Executor thread = Executors.newSingleThreadExecutor();
                                            thread.execute(() -> {
                                                iDao.insertItem(newItem);
                                                items.add(newItem);
                                            });

                                            adapter.notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }, error -> {
                                int i = 0;
                            });

                            queue.add(request);

                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                },
                this::onItemLongClick);

        recyclerView.setAdapter(adapter);
    }
}
