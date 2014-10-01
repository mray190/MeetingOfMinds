package com.webs.michael_ray.meetingofminds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Aaron Barber on 30/09/14.
 */
public class DatabaseManager {


    public ResultSet query(String sqlStatement) throws SQLException{
        String host = "schul030_meeting_of_the_minds";
        String username = "schul030_motm";
        String password = "%LyB}g4Pxlrz";

        //Connect
        Connection conn = DriverManager.getConnection(host, username, password);

        //Allocate Statement
        Statement statement = conn.createStatement();

        //Execute a SQL SELECT query
        ResultSet resultSet = statement.executeQuery(sqlStatement);

        //Ends connection
        conn.close();

        return resultSet;
    }

}
