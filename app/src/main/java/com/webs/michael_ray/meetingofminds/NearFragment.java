package com.webs.michael_ray.meetingofminds;

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

import java.util.ArrayList;


public class NearFragment extends ListFragment {
    private Location location;
    private ArrayList<Point> points;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setCacheColorHint(Color.TRANSPARENT);
    }

    public void updateList(Location location) {
        this.location = location;
        GetNear getNear = new GetNear();
        getNear.execute();
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }

    private class GetNear extends AsyncTask<Void, Integer, Void> {
        private PointNearAdapter pAdapter;
        @Override
        protected Void doInBackground(Void...params) {
            try {
                points = DatabaseManager.dm.findNear(location);
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