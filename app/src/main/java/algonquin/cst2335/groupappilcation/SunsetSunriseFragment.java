package algonquin.cst2335.groupappilcation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.groupappilcation.databinding.SunsetSunriseFragmentBinding;

public class SunsetSunriseFragment extends Fragment {
    SunsetSunriseItem result;
    private Button saveButton;
    SunsetSunriseFragment(SunsetSunriseItem toDisplay){
        result = toDisplay;
    }
    private SunsetSunriseItemDAO dao;
    SunsetSunriseFragmentBinding binding = SunsetSunriseFragmentBinding.inflate(getLayoutInflater());
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //fragment layout:
        binding.sunriseResult.setText(result.sunrise);
        binding.sunsetResult.setText(result.sunset);
        binding.latitude.setText(result.latitude);
        binding.longitude.setText(result.longitude);
        binding.date.setText(result.date);
        // Set click listener for the save button
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SunsetSunriseItem existingItem = dao.getItemByCoordinates(result.latitude, result.longitude);
//                if (existingItem != null) {
//                    // Update existing record
//                    //existingItem.setSunrise(result.sunrise);
//                    //existingItem.setSunset(result.sunset);
//                    dao.updateItem(existingItem);
//                } else {
//                    // Insert new record
//                    dao.insertItem(result);
//                }
//            }
//        });
        return binding.getRoot();
    }
    // Method to update UI with search results
    public void updateSearchResults(String latitude, String longitude, String sunrise, String sunset) {
        binding.sunriseResult.setText(result.sunrise);
        binding.sunsetResult.setText(result.sunset);
        binding.latitude.setText(result.latitude);
        binding.longitude.setText(result.longitude);
        binding.date.setText(result.date);
    }
}


