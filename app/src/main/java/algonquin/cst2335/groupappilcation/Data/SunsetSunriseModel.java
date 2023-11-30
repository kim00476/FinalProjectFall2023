package algonquin.cst2335.groupappilcation.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.groupappilcation.SunsetSunriseItem;

public class SunsetSunriseModel extends ViewModel {
    public MutableLiveData<ArrayList<SunsetSunriseItem>> sunsetSunriseItem = new MutableLiveData<>(null);
    public MutableLiveData<SunsetSunriseItem> selected = new MutableLiveData<>();
}
