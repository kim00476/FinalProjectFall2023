package algonquin.cst2335.groupappilcation;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface SongItemDAO {
    @Insert
    long insertSong(SongItem m);

//    @Query(" Select * from SongItem where songTitle = :selectedWord ")
    @Query(" Select * from SongItem ")
//    List<SongItem> getItemByWord(String selectedWord);
    List<SongItem> getAllMessages();
    @Delete
    void deleteSong(SongItem m);
}
