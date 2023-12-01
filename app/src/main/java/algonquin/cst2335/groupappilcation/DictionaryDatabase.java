package algonquin.cst2335.groupappilcation;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * The Database class for managing the persistence of DictionaryItem entities using RoomDatabase.
 */
@Database(entities = {DictionaryItem.class}, version=1)
public abstract class DictionaryDatabase extends RoomDatabase {

    /**
     * Returns an instance of the associated DAO (Data Access Object) for DictionaryItem.
     *
     * @return The DictionaryItemDAO for performing database operations on DictionaryItem entities.
     */
    public abstract DictionaryItemDAO dictionaryItemDAO();
}
