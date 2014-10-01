package com.webs.michael_ray.meetingofminds.logic;

import android.location.Location;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Aaron Barber on 30/09/14.
 */
public class DatabaseManager {

    //Access
    //----------------------------------------------------------------------------------------------
    public static DatabaseManager manager = new DatabaseManager();
    //----------------------------------------------------------------------------------------------


    //Constructor
    //----------------------------------------------------------------------------------------------
    private DatabaseManager(){

    }
    //----------------------------------------------------------------------------------------------


    //User functions
    //----------------------------------------------------------------------------------------------
    public boolean createUser(){
        return false;
    }

    public boolean authUser(){
        return false;
    }
    //----------------------------------------------------------------------------------------------


    //Find Points
    //----------------------------------------------------------------------------------------------
    public ArrayList<Point> findNear(Location loc){
        return null;
    }

    public ArrayList<Point> findNear(Location loc, int category){
        return null;
    }

    public ArrayList<Point> findFavorites(int userId){
        return null;
    }
    //----------------------------------------------------------------------------------------------


    //Point operations
    //----------------------------------------------------------------------------------------------
    public void findDetails(Point data){

    }

    public boolean insertPoint(Location loc, int category, String description, int userId){
        return false;
    }
    //----------------------------------------------------------------------------------------------


    //Sequel
    //----------------------------------------------------------------------------------------------
    private ResultSet query(String sqlStatement) throws SQLException{
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
    //----------------------------------------------------------------------------------------------

}
