package algonquin.cst2335.groupappilcation;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DictionaryItemDAO {

    @Insert
    long insertWord(DictionaryItem m);

    @Query("Select * from DictionaryItem WHERE word = :word")
    List<DictionaryItem> getItemByWord(String word);

    @Delete
    void deleteWord(DictionaryItem m);


}
