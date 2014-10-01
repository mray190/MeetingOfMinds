package com.webs.michael_ray.meetingofminds.logic;

import android.location.Location;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    //GPS constants
    //----------------------------------------------------------------------------------------------
    private final double long_range = 0.05;
    private final double lat_range  = 0.05;

    private final double long_range_min = 0.001/3;
    private final double lat_range_min  = 0.001/3;
    //----------------------------------------------------------------------------------------------


    //Helper Functions
    //----------------------------------------------------------------------------------------------
    //pls dont touch
    private static final String md5(final String s) {
        try {
            // Create MD5 Hash
            s.concat("%poo#()@FKJWF(IRGOIUGOWI$I8");
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            //Well, s***.
        }
        return "";
    }

    /**
     *
     * @param data ResultSet from a query
     * @return ArrayList of points
     * @throws SQLException
     */
    private ArrayList<Point> convertToPoints(ResultSet data) throws SQLException{
        ArrayList<Point> points = new ArrayList<Point>();

        while (data.next()){
            String category = data.getString("type");
            points.add(
                    new Point(
                            data.getInt("uid"),
                            data.getInt("sid"),
                            false, //TODO is favorite
                            category,
                            CategoryManager.resource(category),
                            data.getString("description"),
                            data.getDouble("lat"),
                            data.getDouble("long"),
                            data.getInt("reports"),
                            data.getInt("votes"),
                            data.getInt("rating"),
                            data.getTime("time")
                    )
            );
        }

        return points;
    }
    //----------------------------------------------------------------------------------------------


    //Access
    //----------------------------------------------------------------------------------------------
    public static DatabaseManager dm = new DatabaseManager();
    //----------------------------------------------------------------------------------------------


    //Constructor
    //----------------------------------------------------------------------------------------------
    private DatabaseManager(){

    }
    //----------------------------------------------------------------------------------------------


    //User functions
    //----------------------------------------------------------------------------------------------

    /**
     *
     * @param username
     * @param password
     * @return User id.  Returns -1 on failure (username already taken)
     */
    public int createUser(String username, String password) throws IOException {
        String hash = md5(password);
        String urlParameters = "username=" + username + "&hash=" + hash;

        //Requests
        String request = "http://shaneschulte.com/motm/createUser.php";
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
        connection.setUseCaches (false);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        connection.disconnect();

        return -1;
    }

    /**
     *
     * @param username
     * @param password
     * @return User id. Returns -1 on failure
     */
    public int authUser(String username, String password) throws IOException {
        String hash = md5(password);
        String urlParameters = "username=" + username + "&hash=" + hash;

        //Requests
        String request = "http://shaneschulte.com/motm/authUser.php";
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
        connection.setUseCaches (false);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        connection.disconnect();

        return 0;
    }
    //----------------------------------------------------------------------------------------------


    //Find Points
    //----------------------------------------------------------------------------------------------
    private ArrayList<Point> findNearHelper(Location loc, String append) throws SQLException {
        double longitude = loc.getLongitude();
        double latitude  = loc.getLatitude();
        ResultSet points = query("SELECT * FROM submissions WHERE " +
                " and longitude > " + (longitude - long_range) +
                " and latitude  > " + (latitude  -  lat_range) +
                " and longitude < " + (longitude + long_range) +
                " and latitude  < " + (latitude  +  lat_range) +
                append);
        return convertToPoints(points);
    }

    /**
     *
     * @param loc Location to find points near
     * @return all the points
     */
    public ArrayList<Point> findNear(Location loc) throws SQLException {
        return findNearHelper(loc, "");
    }

    /**
     *
     * @param loc Location to find points near
     * @param category Category to filter with
     * @return all the points
     */
    public ArrayList<Point> findNear(Location loc, int category) throws SQLException {
        return findNearHelper(loc, " AND type='"+category+"'");
    }

    /**
     *
     * @param userId the user's ID from authUser return
     * @return ArrayList of all points found
     * @throws SQLException because deal with it Michael
     */
    public ArrayList<Point> findFavorites(int userId) throws SQLException {
        //Get all the point IDs associated with the userID
        ResultSet subIds = query(
                "SELECT sid " +
                        "FROM favorites " +
                        "WHERE uid = '" + userId + "'"
        );

        //Container
        ArrayList<Point> points = new ArrayList<Point>();

        //Converts all favorite point IDs to the Java container
        while (subIds.next()){
            ResultSet pointData = query(
                    "SELECT * " +
                            "FROM submissions " +
                            "WHERE sid = '" + subIds.getInt("sid") + "'"
            );

            points.addAll(convertToPoints(pointData));
        }

        return points;
    }
    //----------------------------------------------------------------------------------------------


    //Point operations
    //----------------------------------------------------------------------------------------------
    public void getComments(Point data){
        //TODO
    }

    /**
     *
     * @param loc Location the point should be placed
     * @param category integer representation of category
     * @param description a short description (think name) [please sanitize, 22 char max]
     * @param userId uid returned from authUser
     * @return true/false based on success of submission
     * @throws SQLException sorry michael
     */
    public boolean insertPoint(Location loc, int category, String description, int userId) throws SQLException {
        double longitude = loc.getLongitude();
        double latitude  = loc.getLatitude();
        ResultSet points = query("SELECT * FROM submissions WHERE " +
                " and longitude > " + (longitude - long_range_min) +
                " and latitude  > " + (latitude  -  lat_range_min) +
                " and longitude < " + (longitude + long_range_min) +
                " and latitude  < " + (latitude  +  lat_range_min));
        if(points.next()) {
            return false;
        }
        query("INSERT INTO submissions (uid, latitude, longitude, type, description) VALUES " +
                "('"+userId+"', '"+latitude+"', '"+longitude+"', '"+category+"', '"+description+"')");
        return true;
    }
    //-------------------------- --------------------------------------------------------------------


    //Sequel
    //----------------------------------------------------------------------------------------------
    private ResultSet query(String sqlStatement) throws SQLException {
        if (true){
            return null;
        }

        String url      = "jdbc:mysql://shaneschulte.com:3306/";
        String db       = "schul030_meeting_of_the_minds";
        String username = "schul030_motm";
        String password = "%LyB}g4Pxlrz";

        //Connect
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = DriverManager.getConnection(url+db, username, password);

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
