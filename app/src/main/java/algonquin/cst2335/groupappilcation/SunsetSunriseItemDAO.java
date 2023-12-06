package algonquin.cst2335.groupappilcation;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SunsetSunriseItemDAO {
    @Insert
    void insertItem(SunsetSunriseItem i);
    @Delete
    int deleteItem(SunsetSunriseItem i);
    @Update
    void updateItem(SunsetSunriseItem i);
    @Query("SELECT * FROM SunsetSunriseItem")
    List<SunsetSunriseItem> getAllCoordinates();

}
