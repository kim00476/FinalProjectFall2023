package algonquin.cst2335.groupappilcation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SunsetSunriseFragment extends Fragment {

    private SunsetSunriseItem result;
    private SunsetSunriseItemDAO iDao;

    public SunsetSunriseFragment(SunsetSunriseItem result, SunsetSunriseItemDAO iDao) {
        this.result = result;
        this.iDao = iDao;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sunset_sunrise_fragment, container, false);

        // Set your views using the view.findViewById method
        TextView latitudeTextView = view.findViewById(R.id.latitude);
        TextView longitudeTextView = view.findViewById(R.id.longitude);
        TextView sunriseResultTextView = view.findViewById(R.id.sunriseResult);
        TextView sunsetResultTextView = view.findViewById(R.id.sunsetResult);
        TextView dateTextView = view.findViewById(R.id.date);

        latitudeTextView.setText(String.format("%s%s", getString(R.string.latitude), result.getLatitude()));
        longitudeTextView.setText(String.format("%s%s", getString(R.string.longitude), result.getLongitude()));
        sunriseResultTextView.setText(String.format("%s%s",getString(R.string.sunrise), result.getSunrise()));
        sunsetResultTextView.setText(String.format("%s%s", getString(R.string.sunset), result.getSunset()));
        dateTextView.setText(String.format("%s%s", getString(R.string.date), result.getDate()));

        Button addButton = view.findViewById(R.id.saveButton);
        addButton.setOnClickListener(v -> {
            // Save the result and refresh the result
            saveResultAndRefresh();
        });
        return view;
    }

    private void saveResultAndRefresh() {
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            // Check if the item already exists in the database
            List<SunsetSunriseItem> existingItems = iDao.getAllCoordinates();
            boolean itemExists = false;

            for (SunsetSunriseItem existingItem : existingItems) {
                if (existingItem.getLatitude().equals(result.getLatitude()) &&
                        existingItem.getLongitude().equals(result.getLongitude())) {
                    itemExists = true;
                    break;
                }
            }

            // Insert the item only if it does not already exist
            if (!itemExists) {
                iDao.insertItem(result);
            }
        });

        // Set the result code to indicate success
        requireActivity().setResult(Activity.RESULT_OK);

        // Close the current activity (fragment) and return to the previous one
        requireActivity().finish();

        Intent intent = new Intent(requireContext(),SunsetSunriseMain.class);
        startActivity(intent);
    }
}
