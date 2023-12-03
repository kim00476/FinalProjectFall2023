package algonquin.cst2335.groupappilcation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.groupappilcation.databinding.DictionaryDetailsLayoutBinding;

/**
 * A fragment class responsible for displaying details of a selected dictionary item.
 */
public class DictionaryFragment extends Fragment {

    /** Selected dictionary item */
    DictionaryItem selected;

    /**
     * Constructor for DictionaryFragment.
     *
     * @param m The selected dictionary item to be displayed in the fragment.
     */
    public DictionaryFragment(DictionaryItem m){
        selected = m;
    }


    /**
     * Called to create the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The root view of the inflated layout.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        /** Inflates the layout using view binding */
        DictionaryDetailsLayoutBinding binding = DictionaryDetailsLayoutBinding.inflate(inflater);


        /** Sets the text values in the layout based on the selected dictionary item */
        binding.wordText.setText(selected.word);
        binding.definitionText.setText(selected.definition);
        binding.databaseText.setText("Id = " + selected.id);
        return binding.getRoot();
    }
}
