package sample.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQL {

    // method to get data from the weatherstations database and return as a datatable
    public static ResultSet ExecuteQuery(String sql) {
        ResultSet results=null;
        try(var connection = DBConnect.getConnection()) {
            var stmt = connection.createStatement();
            // query the database and receive a result set
            results = stmt.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return the results
        return results;
    }

    public static int ExecuteUpdate(String sql) {
        var rowsAffected = -1;
        try(var connection = DBConnect.getConnection()) {
            var stmt = connection.createStatement();
            // query the database and receive the number of rows affected
            rowsAffected = stmt.executeUpdate(sql);
            connection.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

        // return the number of rows affected
        return rowsAffected;
    }
}
