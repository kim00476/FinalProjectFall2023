package algonquin.cst2335.groupappilcation.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.groupappilcation.SunsetSunriseItem;

public class SunsetSunriseModel extends ViewModel {
    public ArrayList<SunsetSunriseItem> items = new ArrayList<>();
    public static MutableLiveData<SunsetSunriseItem> selected = new MutableLiveData<>();
}
