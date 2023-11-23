package algonquin.cst2335.groupappilcation.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.groupappilcation.DictionaryItem;

public class SongSearchModel extends ViewModel {
    public MutableLiveData<ArrayList<DictionaryItem>> def = new MutableLiveData<>();
}
