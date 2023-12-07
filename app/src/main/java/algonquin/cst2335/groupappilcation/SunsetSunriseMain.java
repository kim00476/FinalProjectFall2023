package algonquin.cst2335.groupappilcation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
                searchCoordinates(inputLatitude, inputLongitude);
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
        getItem();
    }

    private void getItem() {

        Executor thread1 = Executors.newSingleThreadExecutor();
        thread1.execute(() -> {
            List<SunsetSunriseItem> fromDatabase = iDao.getAllCoordinates();
            items.clear(); // Clear existing items
            items.addAll(fromDatabase);

            // Notify the adapter on the main thread
            runOnUiThread(() -> adapter.notifyDataSetChanged());
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
            case R.id.home_item:
                homeAlert();
                break;

            case R.id.about_item:
                Toast.makeText(this,"Version 1.0, created by Yuyao He", Toast.LENGTH_LONG).show();

            case R.id.howToUse_item:
                instructionAlert();
        }
        return true;
    }

    private void homeAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // User confirmed, close the main activity
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // User canceled, do nothing
                })
                .show();
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

    public boolean isValidLocation(String inputLatitude, String inputLongitude) {
        double parsedLatitude = Double.parseDouble(inputLatitude);
        double parsedLongitude = Double.parseDouble(inputLongitude);
        return parsedLatitude >= -90.0 && parsedLatitude <= 90.0 &&
                parsedLongitude >= -180.0 && parsedLongitude <= 180.0;
    }

    private void onItemLongClick(int position) {
        SunsetSunriseItem selectedItem = items.get(position);

        // Show a confirmation dialog before deleting the item
        showDeleteConfirmationDialog(selectedItem, position);
    }

    private void showDeleteConfirmationDialog(SunsetSunriseItem selectedItem, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // User confirmed, delete the item
                    deleteItem(selectedItem, position);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // User canceled, do nothing
                })
                .show();
    }
        private void deleteItem(SunsetSunriseItem selectedItem, int position) {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                iDao.deleteItem(selectedItem);
            });

            items.remove(position);
            adapter.notifyDataSetChanged();

            Snackbar.make(this.getCurrentFocus(), "Item deleted", Toast.LENGTH_SHORT).show();
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

        // Initialize the adapter with the items
        adapter = new SunsetSunriseAdapter(items);

        // Set the item click and long click listeners
        adapter.setOnItemClickListener((latitude, longitude) -> {
            // Perform a new search using the coordinates
            searchCoordinates(latitude, longitude);
        });
        adapter.setOnItemLongClickListener(position -> {
            // Perform deletion after confirming with a pop-up warning
            onItemLongClick(position);
        });

        recyclerView.setAdapter(adapter);
    }

    private void searchCoordinates(String latitude, String longitude) {
        try {
            latitude = URLEncoder.encode(latitude, "UTF-8");
            longitude = URLEncoder.encode(longitude, "UTF-8");

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

                            SunsetSunriseFragment fragment = new SunsetSunriseFragment(newItem, iDao);
                            getSupportFragmentManager().beginTransaction()
                                    .addToBackStack(null)  // Add the fragment to the back stack
                                    .replace(R.id.fragmentLocation, fragment)
                                    .commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }, (error) -> {
                Toast.makeText(this, "Network request failed", Toast.LENGTH_SHORT).show();
            });

            queue.add(request);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}

