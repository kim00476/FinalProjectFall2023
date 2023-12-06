package algonquin.cst2335.groupappilcation;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SunsetSunriseItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "latitude")
    public String latitude;

    @ColumnInfo(name = "longitude")
    public String longitude;

    @ColumnInfo(name = "sunrise")
    public String sunrise;

    @ColumnInfo(name = "sunset")
    public String sunset;

    @ColumnInfo(name = "date")
    public String date;

    public SunsetSunriseItem(String latitude, String longitude, String sunrise, String sunset, String date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getDate() {
        return date;
    }
}

