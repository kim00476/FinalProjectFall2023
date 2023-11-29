package algonquin.cst2335.groupappilcation;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SongSearchItemDAO {

    @Insert
    long insertSong(SongSearchItem s);

//    @Update
//    long updateSong(SongSearchItem s);

    @Query("Select * from SongSearchItem WHERE name = :name")
    List<SongSearchItem> getItemByWord(String name);

    @Delete
    void deleteSong(SongSearchItem s);
}
