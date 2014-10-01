package com.webs.michael_ray.meetingofminds;

import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.webs.michael_ray.meetingofminds.adapters.PointNearAdapter;
import com.webs.michael_ray.meetingofminds.logic.DatabaseManager;
import com.webs.michael_ray.meetingofminds.logic.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CategoryFragment extends ListFragment {
    private Location location;
    private String category;
    private ArrayList<Point> points;
    public static ArrayList<ArrayList<String>> categories;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setCacheColorHint(Color.TRANSPARENT);
    }

    public void updateList(Location location, String category) {
        this.location = location;
        this.category = category;
        GetCategory getCategory = new GetCategory();
        getCategory.execute();
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }

    private void read(){
        if (categories != null){
            return;
        }

        //Gets the data stream
        Resources res = getResources();
        InputStream in = res.openRawResource(R.raw.category);

        //Data to return
        ArrayList<ArrayList<String>> arr = new ArrayList<ArrayList<String>>();

        //Reads the data line by line
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String[] vals = line.split("\\s*,\\s*");

                ArrayList<String> tmp = new ArrayList<String>();

                for (String val: vals){
                    tmp.add(val);
                }

                arr.add(tmp);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        categories = arr;
        return;
    }

    private class GetCategory extends AsyncTask<Void, Integer, Void> {
        private PointNearAdapter pAdapter;
        @Override
        protected Void doInBackground(Void...params) {
            try {
                points = DatabaseManager.dm.findNear(location,category);
            } catch (Exception e) {
                e.printStackTrace();
                points = new ArrayList<Point>();
            }
            return null;
        }
        protected void onProgressUpdate(Integer...progress) {

        }
        @Override
        protected void onPostExecute(Void result) {
            pAdapter = new PointNearAdapter(getActivity(), R.layout.row_near, points);
            setListAdapter(pAdapter);
        }
    }
}