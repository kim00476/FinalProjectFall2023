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

//    @Query("Select * from SongSearchItem WHERE name = :name")
    @Query("Select * from SongSearchItem")
    List<SongSearchItem> getAllSongs();

    @Delete
    void deleteSong(SongSearchItem songSearchItem);
}
