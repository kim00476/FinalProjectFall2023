package algonquin.cst2335.groupappilcation;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DictionaryItem {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name="id")
    public int id;

    @ColumnInfo(name="word")
    private String word;

    @ColumnInfo(name="defintion")
    private String definition;

    public DictionaryItem(String word, String definition) {
        this.id = id;
        this.word = word;
        this.definition = definition;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition(){
        return definition;
    }
}
