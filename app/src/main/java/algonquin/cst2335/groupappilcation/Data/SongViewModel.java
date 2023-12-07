package algonquin.cst2335.groupappilcation.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.groupappilcation.SongItem;

public class SongViewModel extends ViewModel {
        public MutableLiveData<String> editString = new MutableLiveData<>();
        public MutableLiveData<Boolean> isSelected = new MutableLiveData<>(false);
        public MutableLiveData<ArrayList<SongItem>> listSong = new MutableLiveData<>(null);
        public MutableLiveData<SongItem> selectedSong = new MutableLiveData<>();
}
