package algonquin.cst2335.groupappilcation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.groupappilcation.databinding.DictionaryDetailsLayoutBinding;
import algonquin.cst2335.groupappilcation.databinding.SongFragmentBinding;

public class SongFragment extends Fragment {

//    @Nullable

    SongSearchItem selected;
    public SongFragment(SongSearchItem song){
        selected = song;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        SongFragmentBinding binding = SongFragmentBinding.inflate(inflater);

        binding.titleView.setText(selected.title);
        binding.durationView.setText(selected.duration);
        binding.albumNameView.setText(selected.name);
//        binding.albumImage.setImageBitmap(selected.picture_small);  //Question
        return binding.getRoot();
    }
}
