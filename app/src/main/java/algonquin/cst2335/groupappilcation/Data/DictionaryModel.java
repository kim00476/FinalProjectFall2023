package algonquin.cst2335.groupappilcation.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.groupappilcation.DictionaryItem;

/**
 * The DictionaryModel class is a ViewModel responsible for managing data related to dictionary items.
 * It extends the Android Architecture Components ViewModel class.
 */
public class DictionaryModel extends ViewModel {

    /**
     * MutableLiveData representing a list of dictionary items.
     * The data is observed for changes by UI components.
     */
    public MutableLiveData<ArrayList<DictionaryItem>> dictionaryItem = new MutableLiveData<>(null);

    /**
     * MutableLiveData representing a selected dictionary item.
     * The data is observed for changes by UI components.
     */
    public MutableLiveData<DictionaryItem> selectedDefinition = new MutableLiveData<>();
}
