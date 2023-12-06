package algonquin.cst2335.groupappilcation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SunsetSunriseFragment extends Fragment {

    private SunsetSunriseItem result;
    private SunsetSunriseItemDAO iDao;

    public SunsetSunriseFragment(SunsetSunriseItem toDisplay, SunsetSunriseItemDAO iDao) {
        result = toDisplay;
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

        latitudeTextView.setText("Latitude: " + result.getLatitude());
        longitudeTextView.setText("Longitude: " + result.getLongitude());
        sunriseResultTextView.setText("Sunrise Time: " + result.getSunrise());
        sunsetResultTextView.setText("Sunset Time: " + result.getSunset());
        dateTextView.setText("Date: " + result.getDate());

        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> onSaveButtonClick());

        return view;
    }

    private void onSaveButtonClick() {
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            iDao.insertItem(result);

        });



        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
