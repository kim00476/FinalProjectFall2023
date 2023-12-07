package algonquin.cst2335.groupappilcation.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.groupappilcation.SongItem;

public class SongViewModel extends ViewModel {
        public MutableLiveData<ArrayList<SongItem>> listSong = new MutableLiveData<>(null);
        public MutableLiveData<SongItem> selectedSong = new MutableLiveData<>();
        public MutableLiveData<ArrayList<SongItem>> favoriteSongsArray = new MutableLiveData<>();
}
