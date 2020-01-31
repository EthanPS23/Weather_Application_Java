package sample.Model;

public class StationInfo {
    private int StationID;
    private int RegionID;
    private int Elevation;
    private double Latitude;
    private double Longitude;
    private String Description;

    public StationInfo(int stationID, int regionID, int elevation, double latitude, double longitude, String description) {
        StationID = stationID;
        RegionID = regionID;
        Elevation = elevation;
        Latitude = latitude;
        Longitude = longitude;
        Description = description;
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

    public int getElevation() {
        return Elevation;
    }

    public void setElevation(int elevation) {
        Elevation = elevation;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
