package com.webs.michael_ray.meetingofminds.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.webs.michael_ray.meetingofminds.R;
import com.webs.michael_ray.meetingofminds.logic.Point;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by mray on 30/09/14.
 */
public class PointCatSubAdapter extends ArrayAdapter<Point> {
    Context context;
    int layoutResourceId;
    ArrayList<Point> data = null;
    DecimalFormat df = new DecimalFormat("0.00");

    public PointCatSubAdapter(Context context, int layoutResourceId, ArrayList<Point> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PointCatSubHolder holder = null;

        if (row==null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new PointCatSubHolder();
            holder.img_dir = (ImageView)row.findViewById(R.id.img_dir_catsub);
           // holder.txt_rate = (TextView)row.findViewById(R.id.txt_rate_catsub);
            holder.txt_dist = (TextView)row.findViewById(R.id.txt_dist_catsub);
            holder.txt_name = (TextView)row.findViewById(R.id.txt_name_catsub);

            row.setTag(holder);
        } else {
            holder = (PointCatSubHolder)row.getTag();
        }
        Point point = data.get(position);
        holder.img_dir.setImageResource(point.getDir());
        if (point.getIcon()==R.drawable.energy_icon) {
            holder.img_dir.setBackgroundResource(R.color.ev_charge);
            holder.txt_dist.setBackgroundResource(R.color.ev_charge);
            holder.txt_name.setBackgroundResource(R.color.ev_charge);
        } else if (point.getIcon()==R.drawable.transportation_icon) {
            holder.img_dir.setBackgroundResource(R.color.bike);
            holder.txt_dist.setBackgroundResource(R.color.bike);
            holder.txt_name.setBackgroundResource(R.color.bike);
        } else if (point.getIcon()==R.drawable.community_icon) {
            holder.img_dir.setBackgroundResource(R.color.soup);
            holder.txt_dist.setBackgroundResource(R.color.soup);
            holder.txt_name.setBackgroundResource(R.color.soup);
        } else if (point.getIcon()==R.drawable.food_icon) {
            holder.img_dir.setBackgroundResource(R.color.food);
            holder.txt_dist.setBackgroundResource(R.color.food);
            holder.txt_name.setBackgroundResource(R.color.food);
        }
        holder.txt_rate.setText(point.getCat());
        holder.txt_dist.setText(df.format(point.getDist()));
        holder.txt_name.setText(point.getName());
        return row;
    }

    static class PointCatSubHolder {
        ImageView img_dir;
        TextView txt_rate, txt_name, txt_dist;
    }
}
