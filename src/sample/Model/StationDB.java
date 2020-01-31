package sample.Model;

import java.sql.SQLException;
import java.util.List;

public class StationDB {

    // method to get a list of the stations
    public static List<Station> GetStations(){
        List<Station> stations = null;
        try {
            var results = SQL.ExecuteQuery("SELECT * FROM Station;"); // get all of the stations

            while (results.next()){
                var station = new Station(results.getInt("StationID"),
                        results.getString("StationName"));
                stations.add(station);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  stations;
    }


    public static boolean SetStations(List<Station> stations){
        var result = false;
        // sql to insert into the station table. If a duplicate key exists then the values will be updated
        var sql = "INSERT INTO Station " +
                "(StationID,StationName) " +
                "VALUES " +
                "('%1$s','%2$s')" +
                "ON DUPLICATE KEY UPDATE StationName = '%2$s';";
        var sqlStatement = "";
        // combines sql statements if there is more than one station in the list
        for (Station station : stations) {
            sqlStatement += String.format(sql,station.getStationID(),station.getStationName());
        }

        result = SQL.ExecuteUpdate(sqlStatement)>=0; // if the number of rows affected is 0 or more than result is true

        return result;
    }
}
