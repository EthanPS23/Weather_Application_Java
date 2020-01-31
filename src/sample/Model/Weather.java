package sample.Model;

import java.util.Date;

public class Weather {

    private int StationID;
    private Date Date;
    private int Hour;
    private int Temp;
    // the integers below are all nullable
    private Integer Rel_Hum;
    private Integer Snow_Pack;
    private Integer Snow_New;
    private Integer Precip_New;
    private Integer Hr_Snow;
    private Integer Wind_Speed;
    private Integer Max_Wind_Speed;
    private String Wind_Dir;

    public Weather(int stationID, java.util.Date date, int hour, int temp, Integer RH, Integer snow_Pack, Integer snow_New, Integer precip_New, Integer hr_Snow, Integer wind_Speed, Integer max_Wind_Speed, String wind_Dir) {
        StationID = stationID;
        Date = date;
        Hour = hour;
        Temp = temp;
        Rel_Hum = RH;
        Snow_Pack = snow_Pack;
        Snow_New = snow_New;
        Precip_New = precip_New;
        Hr_Snow = hr_Snow;
        Wind_Speed = wind_Speed;
        Max_Wind_Speed = max_Wind_Speed;
        Wind_Dir = wind_Dir;
    }

    public int getStationID() {
        return StationID;
    }

    public void setStationID(int stationID) {
        StationID = stationID;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public int getHour() {
        return Hour;
    }

    public void setHour(int hour) {
        Hour = hour;
    }

    public int getTemp() {
        return Temp;
    }

    public void setTemp(int temp) {
        Temp = temp;
    }

    public Integer getRel_Hum() {
        return Rel_Hum;
    }

    public void setRel_Hum(Integer rel_Hum) {
        Rel_Hum = rel_Hum;
    }

    public Integer getSnow_Pack() {
        return Snow_Pack;
    }

    public void setSnow_Pack(Integer snow_Pack) {
        Snow_Pack = snow_Pack;
    }

    public Integer getSnow_New() {
        return Snow_New;
    }

    public void setSnow_New(Integer snow_New) {
        Snow_New = snow_New;
    }

    public Integer getPrecip_New() {
        return Precip_New;
    }

    public void setPrecip_New(Integer precip_New) {
        Precip_New = precip_New;
    }

    public Integer getHr_Snow() {
        return Hr_Snow;
    }

    public void setHr_Snow(Integer hr_Snow) {
        Hr_Snow = hr_Snow;
    }

    public Integer getWind_Speed() {
        return Wind_Speed;
    }

    public void setWind_Speed(Integer wind_Speed) {
        Wind_Speed = wind_Speed;
    }

    public Integer getMax_Wind_Speed() {
        return Max_Wind_Speed;
    }

    public void setMax_Wind_Speed(Integer max_Wind_Speed) {
        Max_Wind_Speed = max_Wind_Speed;
    }

    public String getWind_Dir() {
        return Wind_Dir;
    }

    public void setWind_Dir(String wind_Dir) {
        Wind_Dir = wind_Dir;
    }
}
