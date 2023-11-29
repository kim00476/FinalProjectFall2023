package algonquin.cst2335.groupappilcation;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SongSearchItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name = "tracklist")
    public String tracklist;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "duration")
    public String duration;

    @ColumnInfo(name = "picture_small")
    public String picture_small;

    public SongSearchItem(String name, String tracklist, String title, String duration, String picture_small) {
        this.id = id;
        this.name = name;
        this.tracklist = tracklist;
        this.title = title;
        this.duration = duration;
        this.picture_small = picture_small;
    }

    public String getName() {
        return name;
    }

    public String getTracklist(){
        return tracklist;
    }

    public String getTitle(){ return title; }

    public String getDuration(){ return duration;}

    public String getPicture_small(){return picture_small;}

}
