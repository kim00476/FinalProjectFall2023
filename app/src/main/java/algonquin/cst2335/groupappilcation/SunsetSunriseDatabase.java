package algonquin.cst2335.groupappilcation;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SunsetSunriseItem.class}, version = 1, exportSchema = false)
public abstract class SunsetSunriseDatabase extends RoomDatabase {

    public abstract SunsetSunriseItemDAO isDAO();
}
