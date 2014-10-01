package com.webs.michael_ray.meetingofminds;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.webs.michael_ray.meetingofminds.adapters.PointCatAdapter;
import com.webs.michael_ray.meetingofminds.logic.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CategoryFragment extends ListFragment {
    private PointCatAdapter pAdapter;
    private ArrayList<Point> points;

    public static ArrayList<ArrayList<String>> categories;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setCacheColorHint(Color.TRANSPARENT);
        points = new ArrayList<Point>();
        pAdapter = new PointCatAdapter(getActivity(), R.layout.row_catmain, points);
        setListAdapter(pAdapter);
        read();
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
}