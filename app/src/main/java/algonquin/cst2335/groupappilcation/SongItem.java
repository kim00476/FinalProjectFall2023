package algonquin.cst2335.groupappilcation;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SongItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;
    @ColumnInfo(name = "artistName")
    protected String name;
    @ColumnInfo(name = "songTitle")
    protected String songTitle;
    @ColumnInfo(name = "albumImage")
    protected String albumImage;
    @ColumnInfo(name = "CheckBox")
    protected boolean isCheckBox;
    @ColumnInfo(name = "Save")
    protected boolean isSaveButton;
    @ColumnInfo(name = "Delete")
    protected boolean isDeleteButton;

    public SongItem(String songTitle, String albumImage, boolean isCheckBox,
                    boolean isSaveButton, boolean isDeleteButton) {
        this.songTitle = songTitle;
        this.albumImage = albumImage;
        this.isCheckBox = isCheckBox;
        this.isSaveButton = isSaveButton;
        this.isDeleteButton = isDeleteButton;
        }
        public String getSongTitle() {
            return songTitle;
        }
        public String getAlbumImage(){
            return albumImage;
        }
        public boolean isCheckBox(){
            return isCheckBox;
        }
        public boolean isSaveButton(){
            return isSaveButton;
        }
        public boolean isDeleteButton() {return isDeleteButton;}
    }