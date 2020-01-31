package sample.Model;

public class Region {

    private int RegionID;
    private String RegionName;

    public Region(int regionID, String regionName) {
        RegionID = regionID;
        RegionName = regionName;
    }

    public int getRegionID() {
        return RegionID;
    }

    public void setRegionID(int regionID) {
        RegionID = regionID;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
    }
}
