package com.webs.michael_ray.meetingofminds;

/**
 * Created by Aaron Barber on 30/09/14.
 */
public class PointImpl implements Point {


    //Data
    //----------------------------------------------------------------------------------------------
    private String cat;
    private String name;
    private int icon;
    private int dir;
    private double dist;
    private double rate;
    private boolean fav;
    //----------------------------------------------------------------------------------------------


    //Constructors
    //----------------------------------------------------------------------------------------------
    public PointImpl(){
        this(null, null, -1, -1, -1, -1, false);
    }

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
    public PointImpl(String cat, String name, int icon, int dir, double dist, double rate, boolean fav){
        this.cat = cat;
        this.name = name;
        this.icon = icon;
        this.dir = dir;
        this.dist = dist;
        this.rate = rate;
        this.fav = fav;
    }
    //----------------------------------------------------------------------------------------------


    //Setters
    //----------------------------------------------------------------------------------------------
    public void setCat(String cat){
        this.cat = cat;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setIcon(int icon){ this.icon = icon; }

    public void setDir(int dir){
        this.dir = dir;
    }

    public void setRate(int rate){
        this.rate = rate;
    }

    public void setFav(boolean fav){
        this.fav = fav;
    }
    //----------------------------------------------------------------------------------------------


    //Getters
    //----------------------------------------------------------------------------------------------
    public String getCat(){
        return cat;
    }

    public String getName(){
        return name;
    }

    public int getIcon(){
        return icon;
    }

    public int getDir(){
        return dir;
    }

    public double getDist(){
        return dist;
    }

    public double getRate(){
        return rate;
    }

    public boolean getFav(){
        return fav;
    }
    //----------------------------------------------------------------------------------------------

}
