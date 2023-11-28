package algonquin.cst2335.groupappilcation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.groupappilcation.databinding.DictionaryDetailsLayoutBinding;

public class DictionaryFragment extends Fragment {

    DictionaryItem selected;

    public DictionaryFragment(DictionaryItem m){
        selected = m;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DictionaryDetailsLayoutBinding binding = DictionaryDetailsLayoutBinding.inflate(inflater);

        binding.wordText.setText(selected.word);
        binding.definitionText.setText(selected.definition);
        binding.databaseText.setText("Id = " + selected.id);
        return binding.getRoot();
    }
}
