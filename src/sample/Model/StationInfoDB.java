package sample.Model;

import java.sql.SQLException;
import java.util.List;

public class StationInfoDB {

    /*// TODO: method to get a list of the stations
    public static List<StationInfo> GetStationInfo(){
        List<StationInfo> stations = null;
        try {
            var results = SQL.ExecuteQuery("SELECT * FROM StationInfo;"); // get all of the stations info

            while (results.next()){
                var station = new StationInfo(results.getInt("StationID"),
                        results.getInt("RegionID"),
                        results.getString("StationName"),
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
    }*/


    public static boolean SetStationInfo(List<StationInfo> stationsInfo){
        var result = false;
        // sql to insert into the StationInfo table. If a duplicate key exists then the values will be updated
        var sql = "INSERT INTO StationInfo " +
                "(StationID,RegionID,StationName,Elevation,Latitude,Longitude,Description) " +
                "VALUES " +
                "('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s')" +
                "ON DUPLICATE KEY UPDATE StationName='%3$s',Elevation='%4$s',Latitude='%5$s',Longitude='%6$s'," +
                "Description='%7$s';";

        for (StationInfo stationInfo : stationsInfo) {
            var sqlStatement = String.format(sql, stationInfo.getStationID(), stationInfo.getRegionID(),
                    stationInfo.getStationName(), stationInfo.getElevation(), stationInfo.getLatitude(),
                    stationInfo.getLongitude(), stationInfo.getDescription());
            result = SQL.ExecuteUpdate(sqlStatement)>=0; // TODO:if the number of rows affected is 0 or more than result is true
        }

        return result;
    }
}
