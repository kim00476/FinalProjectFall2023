package algonquin.cst2335.groupappilcation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import algonquin.cst2335.groupappilcation.databinding.ActivityDictionaryMainBinding;
import algonquin.cst2335.groupappilcation.databinding.ActivitySongSearchMainBinding;

public class SongSearchMain extends AppCompatActivity {
    ActivitySongSearchMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySongSearchMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}