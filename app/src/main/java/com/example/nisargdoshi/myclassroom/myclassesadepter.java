package com.example.nisargdoshi.myclassroom;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Nisarg Doshi on 1/26/2017.
 */

public class myclassesadepter extends ArrayAdapter<myclasses> {

    Context context;
    int layoutResourceId;
    myclasses data[] = null;

    public myclassesadepter(Context context, int layoutResourceId, myclasses[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        myclassesHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new myclassesHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imageView2);
            holder.txtTitle = (TextView)row.findViewById(R.id.textView6);
            holder.subtxtTitle = (TextView)row.findViewById(R.id.textView7);
            row.setTag (holder);
        }
        else
        {
            holder = (myclassesHolder)row.getTag();
        }

        myclasses weather = data[position];
        holder.txtTitle.setText(weather.title);
        holder.subtxtTitle.setText(weather.subttile);
        holder.imgIcon.setImageResource(weather.icon);

        return row;
    }

    static class myclassesHolder
    {
        ImageView imgIcon;
        TextView txtTitle,subtxtTitle;

    }
}
