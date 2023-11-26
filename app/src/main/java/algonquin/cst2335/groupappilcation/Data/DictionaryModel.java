package algonquin.cst2335.groupappilcation.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.groupappilcation.DictionaryItem;

public class DictionaryModel extends ViewModel {
    public MutableLiveData<ArrayList<DictionaryItem>> dictionaryItem = new MutableLiveData<>(null);
    public MutableLiveData<DictionaryItem> selectedDefinition = new MutableLiveData<>();
}
