package algonquin.cst2335.groupappilcation;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SongItem.class}, version = 1)
public abstract class SongDatabase extends RoomDatabase {
    public abstract SongItemDAO songItemDAO();
}
