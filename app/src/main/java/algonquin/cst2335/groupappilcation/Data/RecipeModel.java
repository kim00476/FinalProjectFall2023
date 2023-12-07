package algonquin.cst2335.groupappilcation.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.groupappilcation.Recipe;

public class RecipeModel extends ViewModel {
    public MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();

    public MutableLiveData<Recipe> selectedRecipe = new MutableLiveData<>();
}
