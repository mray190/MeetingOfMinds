package com.webs.michael_ray.meetingofminds;

import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.webs.michael_ray.meetingofminds.adapters.PointCatAdapter;
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
    private PointCatAdapter pcAdapter;
    private ArrayList<Point> points;
    private String category;
    private int advanced;
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
        advanced = 0;
        pcAdapter = new PointCatAdapter(getActivity(), R.layout.row_catmain, points);
        setListAdapter(pcAdapter);
    }

    public ArrayList<Point> getPoints() {
        if (advanced==0) {
            return new ArrayList<Point>();
        } else {
            return points;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        advanced = 1;
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