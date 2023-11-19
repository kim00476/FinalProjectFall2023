package algonquin.cst2335.groupappilcation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import algonquin.cst2335.groupappilcation.databinding.ActivityRecipeSearchMainBinding;

public class RecipeSearchMain extends AppCompatActivity {
    ActivityRecipeSearchMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecipeSearchMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}