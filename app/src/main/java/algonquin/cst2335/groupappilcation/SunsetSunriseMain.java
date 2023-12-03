package algonquin.cst2335.groupappilcation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.groupappilcation.databinding.ActivitySunsetSunriseMainBinding;

public class SunsetSunriseMain extends AppCompatActivity {
    ActivitySunsetSunriseMainBinding binding;

    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySunsetSunriseMainBinding.inflate(getLayoutInflater());
        queue = Volley.newRequestQueue(this);
        setContentView(binding.getRoot());


        //initialize the toolbar
        setSupportActionBar(binding.SunsetSunriseToolbar);

        //private void setupSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MyLocationData", Context.MODE_PRIVATE);
        String searchedLat = prefs.getString("Latitude", "");
        String searchedLong = prefs.getString("Longitude", "");
        binding.latitudeInput.setText(searchedLat);
        binding.longitudeInput.setText(searchedLong);


        binding.searchButton.setOnClickListener(click -> {
            String latitude = binding.latitudeInput.getText().toString();
            String longitude = binding.longitudeInput.getText().toString();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Latitude", latitude);
            editor.putString("Longitude", longitude);
            editor.apply();

            double parsedLatitude = Double.parseDouble(latitude);
            double parsedLongitude = Double.parseDouble(longitude);

            if (isValidLocation(parsedLatitude, parsedLongitude)) {
                try {
                    latitude = URLEncoder.encode(binding.latitudeInput.getText().toString(), "UTF-8");
                    longitude = URLEncoder.encode(binding.longitudeInput.getText().toString(), "UTF-8");

                    String url = "https://api.sunrisesunset.io/json?lat=" + latitude + "&lng=" + longitude + "&timezone=CA&date=today";

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            (response) -> {
                                try {
                                    JSONObject mainObj = response.getJSONObject("results");
                                    String sunrise = mainObj.getString("sunrise");
                                    String sunset = mainObj.getString("sunset");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }, (error) -> {
                    });

                    queue.add(request);//fetches from the server

                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Handle invalid location
                Toast.makeText(this, "Invalid location. Please enter valid latitude and longitude.", Toast.LENGTH_LONG).show();
            }
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


    public boolean isValidLocation(double parsedLatitude, double parsedLongitude) {
        return parsedLatitude >= -90.0 && parsedLatitude <= 90.0 &&
                parsedLongitude >= -180.0 && parsedLongitude <= 180.0;
    }
}
