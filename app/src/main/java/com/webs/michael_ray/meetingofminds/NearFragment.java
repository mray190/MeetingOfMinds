package com.webs.michael_ray.meetingofminds;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.webs.michael_ray.meetingofminds.adapters.PointNearAdapter;
import com.webs.michael_ray.meetingofminds.logic.Point;

import java.util.ArrayList;


public class NearFragment extends ListFragment {
    private PointNearAdapter pAdapter;
    private ArrayList<Point> points;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setCacheColorHint(Color.TRANSPARENT);
        points = new ArrayList<Point>();
        points.add(addPoint());
        pAdapter = new PointNearAdapter(getActivity(), R.layout.row_near, points);
        setListAdapter(pAdapter);
    }

    private Point addPoint() {
        Point point = new Point();
        point.setCat("Food Truck");
        point.setName("Mikes Hotdog stand");
        point.setRate(4);
        point.setIcon(R.drawable.ic_launcher);
        point.setDir(R.drawable.ic_launcher);
        return point;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }
}