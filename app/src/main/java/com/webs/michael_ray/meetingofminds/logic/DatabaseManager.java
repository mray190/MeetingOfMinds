package com.webs.michael_ray.meetingofminds.logic;

import android.location.Location;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    private ArrayList<Point> convertToPoints(String rows, Location loc){
        ArrayList<Point> points = new ArrayList<Point>();

        String[] rowSet = rows.split("\\r?\\n");

        for (String row: rowSet){
            String[] vals = row.split(",");

            points.add(new Point(
               Integer.parseInt(vals[0]),           //userId
               Integer.parseInt(vals[1]),           //subId
               false,                               //favorite
               vals[3],                             //category
               CategoryManager.resource(vals[4]),   //categoryIconCode
               vals[5],                             //names
               Double.parseDouble(vals[6]),         //latitude
               Double.parseDouble(vals[7]),         //longitude
               Integer.parseInt(vals[8]),           //numReports
               Integer.parseInt(vals[9]),           //numVotes
               Double.parseDouble(vals[10]),        //rating
               Integer.parseInt(vals[11])           //time
            ));
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


    private String post(String url, List<NameValuePair> pairs) throws IOException{
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        try {
            // Add your data
            httppost.setEntity(new UrlEncodedFormEntity(pairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            return EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

        return "Exception";
    }

    //User functions
    //----------------------------------------------------------------------------------------------

    public int createUser(String username, String password) throws IOException {
        //Computation
        String hash = md5(password);

        //Request
        String request = "http://shaneschulte.com/motm/createUser.php";

        //Params
        List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
        pairs.add(new BasicNameValuePair("username", username));
        pairs.add(new BasicNameValuePair("hash", hash));

        String tmp = post(request, pairs);
        Log.d("bullshit", tmp);
        Log.d("user", username);
        Log.d("hash", hash);
        Log.d("pass", password);

        return Integer.parseInt(tmp);
    }

    /**
     *
     * @param username
     * @param password
     * @return User id. Returns -1 on failure
     */
    public int authUser(String username, String password) throws IOException {
        //Computation
        String hash = md5(password);

        //Request
        String request = "http://shaneschulte.com/motm/authUser.php";

        //Params
        List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
        pairs.add(new BasicNameValuePair("username", username));
        pairs.add(new BasicNameValuePair("hash", hash));

        return Integer.parseInt(post(request, pairs));
    }
    //----------------------------------------------------------------------------------------------


    //Find Points
    //----------------------------------------------------------------------------------------------
    private ArrayList<Point> findNearHelper(Location loc, String append) throws IOException {
        //Request
        String request = "http://shaneschulte.com/motm/findHelper.php";

        //Params
        List<NameValuePair> pairs = new ArrayList<NameValuePair>(3);
        pairs.add(new BasicNameValuePair("longitude", Double.toString(loc.getLongitude())));
        pairs.add(new BasicNameValuePair("latitude", Double.toString(loc.getLatitude())));
        pairs.add(new BasicNameValuePair("append", append));

        return convertToPoints(post(request, pairs), loc);
    }

    /**
     *
     * @param loc Location to find points near
     * @return all the points
     */
    public ArrayList<Point> findNear(Location loc) throws IOException {
        return findNearHelper(loc, "");
    }

    /**
     *
     * @param loc Location to find points near
     * @param category Category to filter with
     * @return all the points
     */
    public ArrayList<Point> findNear(Location loc, int category) throws IOException {
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

//            points.addAll(convertToPoints(pointData));
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
