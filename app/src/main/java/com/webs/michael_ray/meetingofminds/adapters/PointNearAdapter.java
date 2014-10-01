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
public class PointNearAdapter extends ArrayAdapter<Point> {
    Context context;
    int layoutResourceId;
    ArrayList<Point> data;
    DecimalFormat df = new DecimalFormat("0.00");

    public PointNearAdapter(Context context, int layoutResourceId, ArrayList<Point> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PointNearHolder holder = null;

        if (row==null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new PointNearHolder();
            holder.img_icon = (ImageView)row.findViewById(R.id.img_icon_near);
            holder.img_dir = (ImageView)row.findViewById(R.id.img_dir_near);
            holder.txt_cat = (TextView)row.findViewById(R.id.txt_cat_near);
            holder.txt_dist = (TextView)row.findViewById(R.id.txt_dist_near);
            holder.txt_name = (TextView)row.findViewById(R.id.txt_name_near);

            row.setTag(holder);
        } else {
            holder = (PointNearHolder)row.getTag();
        }
        Point point = data.get(position);
        holder.img_icon.setImageResource(point.getIcon());
        if (point.getIcon()==R.drawable.energy_icon) {
            holder.img_icon.setBackgroundResource(R.color.ev_charge);
            holder.img_dir.setBackgroundResource(R.color.ev_charge);
            holder.txt_cat.setBackgroundResource(R.color.ev_charge);
            holder.txt_dist.setBackgroundResource(R.color.ev_charge);
            holder.txt_name.setBackgroundResource(R.color.ev_charge);
        } else if (point.getIcon()==R.drawable.transportation_icon) {
            holder.img_icon.setBackgroundResource(R.color.bike);
            holder.img_dir.setBackgroundResource(R.color.bike);
            holder.txt_cat.setBackgroundResource(R.color.bike);
            holder.txt_dist.setBackgroundResource(R.color.bike);
            holder.txt_name.setBackgroundResource(R.color.bike);
        } else if (point.getIcon()==R.drawable.community_icon) {
            holder.img_icon.setBackgroundResource(R.color.soup);
            holder.img_dir.setBackgroundResource(R.color.soup);
            holder.txt_cat.setBackgroundResource(R.color.soup);
            holder.txt_dist.setBackgroundResource(R.color.soup);
            holder.txt_name.setBackgroundResource(R.color.soup);
        } else if (point.getIcon()==R.drawable.food_icon) {
            holder.img_icon.setBackgroundResource(R.color.food);
            holder.img_dir.setBackgroundResource(R.color.food);
            holder.txt_cat.setBackgroundResource(R.color.food);
            holder.txt_dist.setBackgroundResource(R.color.food);
            holder.txt_name.setBackgroundResource(R.color.food);
        }

        holder.img_dir.setImageResource(point.getDir());
        holder.txt_cat.setText(point.getCat());
        holder.txt_dist.setText(df.format(point.getDist()));
        holder.txt_name.setText(point.getName());
        return row;
    }

    static class PointNearHolder {
        ImageView img_icon, img_dir;
        TextView txt_cat, txt_name, txt_dist;
    }
}