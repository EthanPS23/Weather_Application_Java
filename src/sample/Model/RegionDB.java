package sample.Model;

import java.sql.SQLException;
import java.util.List;

public class RegionDB {
    // method to get a list of the regions
    public static List<Region> GetRegions(){
        List<Region> regions = null;
        try {
            var results = SQL.ExecuteQuery("SELECT * FROM Region;"); // get all of the region

            while (results.next()){
                var region = new Region(results.getInt("RegionID"),
                        results.getString("RegionName"));
                regions.add(region);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  regions;
    }

    // method to set the regions in the database
    public static boolean SetRegions(List<Region> regions){
        var result = false;
        // sql to insert into the region table. If a duplicate key exists then the values will be updated
        var sql = "INSERT INTO Region " +
                "(RegionID,RegionName) " +
                "VALUES " +
                "('%1$s','%2$s')" +
                "ON DUPLICATE KEY UPDATE RegionName = '%2$s';";
        var sqlStatement = "";
        // combines sql statements if there is more than one region in the list
        for (Region region : regions) {
            sqlStatement += String.format(sql,region.getRegionID(),region.getRegionName());
        }

        result = SQL.ExecuteUpdate(sqlStatement)>=0; // if the number of rows affected is 0 or more than result is true

        return result;
    }
}