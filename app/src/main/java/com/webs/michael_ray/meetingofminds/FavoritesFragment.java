package com.webs.michael_ray.meetingofminds;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.webs.michael_ray.meetingofminds.adapters.PointNearAdapter;
import com.webs.michael_ray.meetingofminds.logic.DatabaseManager;
import com.webs.michael_ray.meetingofminds.logic.Point;

import java.util.ArrayList;

public class FavoritesFragment extends ListFragment {
    private int userID;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setCacheColorHint(Color.TRANSPARENT);
    }

    public void updateList(int userID) {
        this.userID = userID;
        GetFav getFav = new GetFav();
        //getFav.execute();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }

    private class GetFav extends AsyncTask<Void, Integer, Void> {
        private PointNearAdapter pAdapter;
        private ArrayList<Point> points;
        @Override
        protected Void doInBackground(Void...params) {
            try {
                points = DatabaseManager.dm.findFavorites(userID);
            } catch (Exception e) {
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