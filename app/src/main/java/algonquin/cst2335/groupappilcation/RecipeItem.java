package algonquin.cst2335.groupappilcation;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RecipeItem {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "title")
    protected String title;
    @ColumnInfo(name = "summary")
    protected String summary;
    @ColumnInfo(name = "sourceURL")
    protected String sourceURL;
    @ColumnInfo(name = "image")
    protected String image;

    public RecipeItem() {
    }

    public RecipeItem(long id, String title, String summary, String sourceURL, String image) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.sourceURL = sourceURL;
        this.image = image;
    }
}
