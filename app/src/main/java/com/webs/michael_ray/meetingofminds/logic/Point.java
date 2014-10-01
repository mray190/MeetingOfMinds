package com.webs.michael_ray.meetingofminds.logic;


import android.location.Location;

import com.webs.michael_ray.meetingofminds.R;

import java.sql.Time;

/**
 * Created by asb on 30/09/14.
 */
public class Point {


    //Data
    //----------------------------------------------------------------------------------------------
    //User data
    private int userId;
    private int subId;
    private boolean favorite;

    //Category data
    private String category;
    private int categoryIconCode;
    private String name;

    //Location data
    private double latitude;
    private double longitude;

    //Votes / Reports / Time : Meta data
    private int numReports;
    private int numVotes;
    private double rating;
    private Time time;
    //----------------------------------------------------------------------------------------------


    //Constructors
    //----------------------------------------------------------------------------------------------
    public Point(
            int userId,
            int subId,
            boolean favorite,
            String category,
            int categoryIconCode,
            String name,
            double latitude,
            double longitude,
            int numReports,
            int numVotes,
            double rating,
            Time time
    ){
        this.userId = userId;
        this.subId = subId;
        this.favorite = favorite;
        this.category = category;
        this.categoryIconCode = categoryIconCode;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numReports = numReports;
        this.numVotes = numVotes;
        this.rating = rating;
        this.time = time;
    }

    public Point(
            int userId,
            String category,
            String name,
            double latitude,
            double longitude
    ){
        this.userId = userId;
        this.subId = 0;
        this.favorite = false;
        this.category = category;
        this.categoryIconCode = 0;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numReports = 0;
        this.numVotes = 0;
        this.rating = 0;
    }

    public Point(
            int subId,
            double latitude,
            double longitude,
            int category,
            int reports,
            String description,
            int rating,
            int votes,
            int userId,
            Time time
    ) {
    }
    //----------------------------------------------------------------------------------------------


    //Calculations
    //----------------------------------------------------------------------------------------------
    private Location getLocation(){
        Location loc = new Location("");
        loc.setLatitude(this.latitude);
        loc.setLongitude(this.longitude);
        return loc;
    }
    //----------------------------------------------------------------------------------------------


    //Getters
    //----------------------------------------------------------------------------------------------
    public String getCat(){
        return category;
    }

    public String getName(){
        return name;
    }

    public int getIcon(){
        return categoryIconCode;
    }

    public int getDir(){
        //Calculates the bearing and bearing direction as an index 0-9
        float bearing = CurrentLocation.getCurrentLocation().bearingTo(getLocation());
        int direction = (int)bearing;
        while (direction > 45){ direction = direction / 45; }

        //Switch to find the resource based on the direction
        switch(direction){
            case 0: return R.drawable.compass_north;
            case 1: return R.drawable.compass_northeast;
            case 2: return R.drawable.compass_east;
            case 3: return R.drawable.compass_southeast;
            case 4: return R.drawable.compass_south;
            case 5: return R.drawable.compass_southwest;
            case 6: return R.drawable.compass_west;
            case 7: return R.drawable.compass_northwest;
            case 8: return R.drawable.compass_north;
            default: return R.drawable.compass_north;
        }
    }

    public double getDist(){
        return CurrentLocation.getCurrentLocation().distanceTo(getLocation());
    }

    public double getRate(){
        return rating;
    }

    public boolean getFav(){
        return favorite;
    }
    //----------------------------------------------------------------------------------------------


}
