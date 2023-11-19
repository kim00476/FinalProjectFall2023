package algonquin.cst2335.groupappilcation;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DictionaryItem.class}, version=1)
public abstract class DictionaryDatabase extends RoomDatabase {
    public abstract DictionaryItemDAO dictionaryItemDAO();
}
