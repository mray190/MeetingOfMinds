package com.webs.michael_ray.meetingofminds;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.webs.michael_ray.meetingofminds.adapters.PointCatAdapter;
import com.webs.michael_ray.meetingofminds.logic.Point;

import java.util.ArrayList;

public class CategoryFragment extends ListFragment {
    private PointCatAdapter pAdapter;
    private ArrayList<Point> points;

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
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }
}