package sample.Model;

import java.time.LocalDate;
import java.util.Date;

public class Weather {

    private int StationID;
    private int RegionID;
    private LocalDate Date;
    private int Hour;
    // the integers below are all nullable
    private Double Temp;
    private Double Rel_Hum;
    private Double Snow_Pack;
    private Double Snow_New;
    private Double Precip_New;
    private Double Hr_Snow;
    private Double Wind_Speed;
    private Double Max_Wind_Speed;
    private String Wind_Dir;

    public  Weather(){
        StationID = -1;
        RegionID = -1;
        Date = null;
        Hour = -1;
        Temp = null;
        Rel_Hum = null;
        Snow_Pack = null;
        Snow_New = null;
        Precip_New = null;
        Hr_Snow = null;
        Wind_Speed = null;
        Max_Wind_Speed = null;
        Wind_Dir = "";
    }

    public Weather(int stationID, int regionID, LocalDate date, int hour, Double temp, Double RH, Double snow_Pack, Double snow_New, Double precip_New, Double hr_Snow, Double wind_Speed, Double max_Wind_Speed, String wind_Dir) {
        StationID = stationID;
        RegionID = regionID;
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

    public int getRegionID() {
        return RegionID;
    }

    public void setRegionID(int regionID) {
        RegionID = regionID;
    }

    public LocalDate getDate() {
        return Date;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

    public int getHour() {
        return Hour;
    }

    public void setHour(int hour) {
        Hour = hour;
    }

    public Double getTemp() {
        return Temp;
    }

    public void setTemp(Double temp) {
        Temp = temp;
    }

    public Double getRel_Hum() {
        return Rel_Hum;
    }

    public void setRel_Hum(Double rel_Hum) {
        Rel_Hum = rel_Hum;
    }

    public Double getSnow_Pack() {
        return Snow_Pack;
    }

    public void setSnow_Pack(Double snow_Pack) {
        Snow_Pack = snow_Pack;
    }

    public Double getSnow_New() {
        return Snow_New;
    }

    public void setSnow_New(Double snow_New) {
        Snow_New = snow_New;
    }

    public Double getPrecip_New() {
        return Precip_New;
    }

    public void setPrecip_New(Double precip_New) {
        Precip_New = precip_New;
    }

    public Double getHr_Snow() {
        return Hr_Snow;
    }

    public void setHr_Snow(Double hr_Snow) {
        Hr_Snow = hr_Snow;
    }

    public Double getWind_Speed() {
        return Wind_Speed;
    }

    public void setWind_Speed(Double wind_Speed) {
        Wind_Speed = wind_Speed;
    }

    public Double getMax_Wind_Speed() {
        return Max_Wind_Speed;
    }

    public void setMax_Wind_Speed(Double max_Wind_Speed) {
        Max_Wind_Speed = max_Wind_Speed;
    }

    public String getWind_Dir() {
        return Wind_Dir;
    }

    public void setWind_Dir(String wind_Dir) {
        Wind_Dir = wind_Dir;
    }
}
