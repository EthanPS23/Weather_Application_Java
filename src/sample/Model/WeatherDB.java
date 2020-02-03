package sample.Model;

import java.sql.SQLException;
import java.util.List;

public class WeatherDB {

    /*// TODO: method to get a list of the regions
    public static List<Weather> GetWeather(){
        List<Weather> weather = null;
        try {
            var results = SQL.ExecuteQuery("SELECT * FROM Weather;"); // get all of the weather info

            while (results.next()){
                var weatherResult = new Weather(results.getInt("StationID"),
                        results.getDate("Date"),
                        results.getInt("Hour"),
                        results.getInt("Temp"),
                        results.getInt("RH"),
                        results.getInt("Snow_Pack"),
                        results.getInt("Snow_New"),
                        results.getInt("Precip_New"),
                        results.getInt("24Hr_Snow"),
                        results.getInt("Wind_Speed"),
                        results.getInt("Max_Wind_Speed"),
                        results.getString("Wind_Dir")
                );
                weather.add(weatherResult);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  weather;
    }*/

    // method to set the weather in the database
    public static boolean InsertWeather(List<Weather> weather){
        var result = false;
        // sql to insert into the weather table
        var sql = "INSERT INTO StationInfo " +
                "(StationID,RegionID,Date,Hour,Temp,RH,Snow_Pack,Snow_New,Precip_New,24Hr_Snow,Wind_Speed,Max_Wind_Speed,Wind_Dir) " +
                "VALUES " +
                "('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s','%8$s','%9$s','%10$s','%11$s','%12$s','%13$s');";
        var sqlStatement = "";

        for (Weather weatherEvent : weather) {
            sqlStatement = String.format(sql,weatherEvent.getStationID(),weatherEvent.getRegionID(),weatherEvent.getDate(),weatherEvent.getHour(),
                    weatherEvent.getTemp(),weatherEvent.getRel_Hum(),weatherEvent.getSnow_Pack(),weatherEvent.getSnow_New(),
                    weatherEvent.getPrecip_New(),weatherEvent.getHr_Snow(),weatherEvent.getWind_Speed(),
                    weatherEvent.getMax_Wind_Speed(),weatherEvent.getWind_Dir());
            result = SQL.ExecuteUpdate(sqlStatement)>=0; // TODO:if the number of rows affected is 0 or more than result is true
        }

        return result;
    }

    // method to set the weather in the database
    public static boolean InsertWeather(Weather weather){
        var result = false;
        // sql to insert into the weather table
        var sql = "INSERT INTO Weather " +
                "(StationID,RegionID,Date,Hour,Temp,RH,Snow_Pack,Snow_New,Precip_New,24Hr_Snow,Wind_Speed,Max_Wind_Speed,Wind_Dir) " +
                "VALUES " +
                "(%1$s,%2$s,'%3$s',%4$s,%5$s,%6$s,%7$s,%8$s,%9$s,%10$s,%11$s,%12$s,%13$s);";
        var sqlStatement = "";

        sqlStatement = String.format(sql,weather.getStationID(),weather.getRegionID(),weather.getDate(),weather.getHour(),
                weather.getTemp(),weather.getRel_Hum(),weather.getSnow_Pack(),weather.getSnow_New(),
                weather.getPrecip_New(),weather.getHr_Snow(),weather.getWind_Speed(),
                weather.getMax_Wind_Speed(),weather.getWind_Dir());
        result = SQL.ExecuteUpdate(sqlStatement)>=0; // TODO:if the number of rows affected is 0 or more than result is true

        return result;
    }
}
