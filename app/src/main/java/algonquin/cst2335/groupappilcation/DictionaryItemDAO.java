package algonquin.cst2335.groupappilcation;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * The Data Access Object (DAO) interface for performing database operations on DictionaryItem entities.
 */
@Dao
public interface DictionaryItemDAO {

    /**
     * Inserts a new dictionary item into the database.
     *
     * @param m The dictionary item to be inserted.
     * @return The ID of the inserted dictionary item.
     */
    @Insert
    long insertWord(DictionaryItem m);

    /**
     * Retrieves a list of dictionary items based on a given word.
     *
     * @param selectedWord The word to search for in the database.
     * @return A list of dictionary items matching the specified word.
     */
    @Query("Select * from DictionaryItem WHERE word = :selectedWord")
    List<DictionaryItem> getItemByWord(String selectedWord);

    /**
     * Deletes a dictionary item from the database.
     *
     * @param m The dictionary item to be deleted.
     */
    @Delete
    void deleteWord(DictionaryItem m);

    @Query("Select distinct word from DictionaryItem")
    List<String> getAllWords();


}
