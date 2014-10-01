package com.webs.michael_ray.meetingofminds;

import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.webs.michael_ray.meetingofminds.adapters.PointNearAdapter;
import com.webs.michael_ray.meetingofminds.logic.CategoryManager;
import com.webs.michael_ray.meetingofminds.logic.DatabaseManager;
import com.webs.michael_ray.meetingofminds.logic.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CategoryFragment extends ListFragment {
    private Location location;
    private ArrayList<Point> points;
    private PointNearAdapter pAdapter;
    private String category;
    public static ArrayList<ArrayList<String>> categories;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setCacheColorHint(Color.TRANSPARENT);
        read();
    }

    public void updateList(Location location) {
        this.location = location;
        points = new ArrayList<Point>();
        for (int i = 0; i < CategoryFragment.categories.size(); i++){
            for (int j = 1; j < CategoryFragment.categories.get(i).size(); j++){
                String category = CategoryFragment.categories.get(i).get(j);
                int icon = CategoryManager.resource(category);
                Point point = new Point(category, icon);
                points.add(point);
            }
        }
        pAdapter = new PointNearAdapter(getActivity(), R.layout.row_near, points);
        setListAdapter(pAdapter);
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        category = points.get(position).getCat();
        GetCategory getCategory = new GetCategory();
        getCategory.execute();
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
        @Override
        protected Void doInBackground(Void...params) {
            try {
                points = DatabaseManager.dm.findNear(location,category);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("MyApp","Not working");
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