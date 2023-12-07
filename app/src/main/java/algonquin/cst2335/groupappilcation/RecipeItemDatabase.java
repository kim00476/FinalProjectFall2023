package algonquin.cst2335.groupappilcation;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RecipeItem.class}, version = 1)
public abstract class RecipeItemDatabase extends RoomDatabase {
    public abstract RecipeItemDAO riDAO();
}
