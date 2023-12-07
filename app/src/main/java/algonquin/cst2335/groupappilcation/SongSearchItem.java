package algonquin.cst2335.groupappilcation;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SongSearchItem {

    @PrimaryKey(autoGenerate = true) //database will increment them for us
    @ColumnInfo(name="id")
    long id;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "duration")
    public int duration;

    @ColumnInfo(name = "picture_small")
    public String picture_small;

    @ColumnInfo(name = "tracklist")
    public String tracklist;

    @ColumnInfo(name = "removeButton")
    public boolean removeButton;
    @ColumnInfo(name = "deleteAllSong")
    public boolean deleteAllSong;

    public SongSearchItem(){  }

    public SongSearchItem(String n, String t, int d,
                          String pic, boolean del, boolean rem) {
        this.name = n;
        this.title = t;
        this.duration = d;
        this.picture_small = pic;
        this.deleteAllSong = del;
        this.removeButton = rem;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public String getPicture_small() {
        return picture_small;
    }

    public boolean isRemoveButton (){ return removeButton;    }

    public boolean isDeleteAllSong (){ return deleteAllSong;    }

}

