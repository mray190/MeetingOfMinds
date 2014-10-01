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
import java.sql.SQLException;
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

        if(rows.equals("")) return points;

        String[] rowSet = rows.split("\\r?\\n");


        for (String row: rowSet){
            if(row.equals("")) break;
            String[] vals = row.split(",");



            points.add(new Point(
               Integer.parseInt(vals[0]),           //userId
               Integer.parseInt(vals[1]),           //subId
               false,                               //favorite
               vals[2],                             //category
               CategoryManager.resource(vals[2]),   //categoryIconCode
               vals[3],                             //names
               Double.parseDouble(vals[4]),         //latitude
               Double.parseDouble(vals[5]),         //longitude
               Integer.parseInt(vals[6]),           //numReports
               Integer.parseInt(vals[7]),           //numVotes
               Double.parseDouble(vals[8]),         //rating
               Integer.parseInt(vals[9]),          //time
               loc                                  //current location
            ));
        }

        return points;
    }

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
    public ArrayList<Point> findNear(Location loc, String category) throws IOException {
        return findNearHelper(loc, " AND type='"+category+"'");
    }

    /**
     *
     * @param userId the user's ID from authUser return
     * @return ArrayList of all points found
     * @throws SQLException because deal with it Michael
     */
    public ArrayList<Point> findFavorites(int userId) throws IOException {
        //Request
        String request = "http://shaneschulte.com/motm/findFavorites.php";

        //Params
        List<NameValuePair> pairs = new ArrayList<NameValuePair>(1);
        pairs.add(new BasicNameValuePair("uid", Integer.toString(userId)));

        return convertToPoints(post(request, pairs), null); //TODO need location
    }
    //----------------------------------------------------------------------------------------------


    //Point operations
    //----------------------------------------------------------------------------------------------
    public void getComments(Point data){
        //TODO
    }

    public boolean insertPoint(Point point) throws IOException {
        //Request
        String request = "http://shaneschulte.com/motm/insertPoint.php";

        //Params
        List<NameValuePair> pairs = new ArrayList<NameValuePair>(5);
        pairs.add(new BasicNameValuePair("longitude", Double.toString(point.getLongitude())));
        pairs.add(new BasicNameValuePair("latitude", Double.toString(point.getLatitude())));
        pairs.add(new BasicNameValuePair("category", point.getCat()));
        pairs.add(new BasicNameValuePair("description", point.getName()));
        pairs.add(new BasicNameValuePair("uid", Integer.toString(point.getUserId())));

        return Boolean.parseBoolean(post(request, pairs));
    }
    //-------------------------- --------------------------------------------------------------------

}
