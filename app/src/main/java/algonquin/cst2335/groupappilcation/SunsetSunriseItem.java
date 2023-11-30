package algonquin.cst2335.groupappilcation;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SunsetSunriseItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;
    @ColumnInfo(name="latitude")
    public String latitude;
    @ColumnInfo(name="longitude")
    public String longitude;
    @ColumnInfo(name="sunrise")
    public String sunrise;
    @ColumnInfo(name="sunset")
    public String sunset;

    public SunsetSunriseItem(int id, String latitude, String longitude, String sunrise, String sunset) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

}