package algonquin.cst2335.groupappilcation;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeItemDAO {
    @Insert
    void insertRecipe(RecipeItem recipeItem);

    @Query("Select * from RecipeItem")
    List<RecipeItem> getAllRecipes();

    @Query("SELECT * FROM RecipeItem where id = :id")
    RecipeItem getRecipe(long id);

    @Query("SELECT * FROM RecipeItem where id = :id")
    List<RecipeItem> getRecipeById(long id);

    @Query("DELETE FROM RecipeItem WHERE id = :id")
    void deleteRecipeByID(long id);

    @Query("DELETE FROM RecipeItem")
    void deleteAllRecipes();

    @Delete
    void deleteRecipe(RecipeItem recipeItem);
}
