package sample.Model;

import java.sql.SQLException;
import java.util.List;

public class StationInfoDB {

    // method to get a list of the stations
    public static List<StationInfo> GetStationInfo(){
        List<StationInfo> stations = null;
        try {
            var results = SQL.ExecuteQuery("SELECT * FROM StationInfo;"); // get all of the stations info

            while (results.next()){
                var station = new StationInfo(results.getInt("StationID"),
                        results.getInt("RegionID"),
                        results.getInt("Elevation"),
                        results.getDouble("Latitude"),
                        results.getDouble("Longitude"),
                        results.getString("Description")
                        );
                stations.add(station);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  stations;
    }


    public static boolean SetStationInfo(List<StationInfo> stationsInfo){
        var result = false;
        // sql to insert into the StationInfo table. If a duplicate key exists then the values will be updated
        var sql = "INSERT INTO StationInfo " +
                "(StationID,RegionID,Elevation,Latitude,Longitude,Description) " +
                "VALUES " +
                "('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s')" +
                "ON DUPLICATE KEY UPDATE RegionID='%2$s',Elevation='%3$s',Latitude='%4$s',Longitude='%5$s'," +
                "Description='%6$s';";
        /*var sqlStatement = "";
        // combines sql statements if there is more than one station in the list
        for (StationInfo stationInfo : stationsInfo) {
            sqlStatement += String.format(sql,stationInfo.getStationID(),stationInfo.getRegionID(),stationInfo.getElevation(),
                    stationInfo.getLatitude(),stationInfo.getLongitude(),stationInfo.getDescription());
        }

        result = SQL.ExecuteUpdate(sqlStatement)>=0; // if the number of rows affected is 0 or more than result is true*/

        for (StationInfo stationInfo : stationsInfo) {
            var sqlStatement = String.format(sql, stationInfo.getStationID(), stationInfo.getRegionID(), stationInfo.getElevation(),
                    stationInfo.getLatitude(), stationInfo.getLongitude(), stationInfo.getDescription());
            SQL.ExecuteUpdate(sqlStatement);
        }
z
        return result;
    }
}
