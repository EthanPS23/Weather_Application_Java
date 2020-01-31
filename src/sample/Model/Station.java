package sample.Model;

public class Station {

    private Integer StationID;
    private String StationName;

    public Station(Integer stationID, String stationName) {
        StationID = stationID;
        StationName = stationName;
    }

    public Integer getStationID() {
        return StationID;
    }

    public void setStationID(Integer stationID) {
        StationID = stationID;
    }

    public String getStationName() {
        return StationName;
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }
}
