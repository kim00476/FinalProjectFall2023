package algonquin.cst2335.groupappilcation.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.groupappilcation.DictionaryItem;
import algonquin.cst2335.groupappilcation.SongSearchItem;

public class SongSearchModel extends ViewModel {
    public MutableLiveData<ArrayList<SongSearchItem>> songLists = new MutableLiveData<>(null);
    public MutableLiveData<SongSearchItem> selected = new MutableLiveData< >();
}
