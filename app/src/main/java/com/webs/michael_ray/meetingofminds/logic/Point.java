package com.webs.michael_ray.meetingofminds.logic;


import android.location.Location;

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
    private int category;
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
    /**
     * Constructs a Point.
     * @param cat category, such as "Food"
     * @param name name of the point, such as "Tom's Food Truck"
     * @param icon index of the icon, such as "1" for "R.drawable.ic_1"
     * @param dir direction of the point, relative to the current location
     * @param dist distance of the point, relative to the current location
     * @param rate rating of the point, from 0-5
     * @param fav whether the point is a favorite
     */
    public Point(
            String cat,
            String name,
            int icon,
            int dir,
            double dist,
            double rate,
            boolean fav
    ){
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
    private String convertCategory(int category){
//        context.getResources().openRawResource(R.raw.category_json);
        return null;
    }

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
        return convertCategory(category);
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
