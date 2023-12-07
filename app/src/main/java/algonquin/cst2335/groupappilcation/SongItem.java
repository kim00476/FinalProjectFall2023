package algonquin.cst2335.groupappilcation;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SongItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    long id;

    @ColumnInfo(name = "artistName")
    String name;
    @ColumnInfo(name = "songTitle")
    String songTitle;
    @ColumnInfo(name = "duration")
    int duration;
    @ColumnInfo(name = "albumImage")
    String albumImage;

    SongItem(String name, String songTitle, int duration, String albumImage){
        this.name = name;
        this.songTitle = songTitle;
        this.duration = duration;
        this.albumImage = albumImage;
    }

    public String getName() {
        return name;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public int getDuration() {
        return duration;
    }

    public String getAlbumImage() {
        return albumImage;
    }


}