package com.defch.blogwbly.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.defch.blogwbly.R;

import java.util.ArrayList;

/**
 * Created by DiegoFranco on 4/18/15.
 */
public class LayoutsViewAdapter extends ArrayAdapter<Bitmap> {

    private Context context;
    private ArrayList<Bitmap> views;

    public LayoutsViewAdapter(Context context, ArrayList<Bitmap> objects) {
        super(context, 0, objects);
        this.context = context;
        this.views = objects;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder;
        Bitmap bmp = views.get(position);
        if(v == null) {
            v = LayoutInflater.from(this.getContext()).inflate(R.layout.item_list_dialog, parent, false);
            holder = new ViewHolder();
            holder.view = (ImageView)v.findViewById(R.id.itm_dialog_img);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        if(bmp != null) {
            holder.view.setImageBitmap(bmp);
        }
        return v;
    }

    private static class ViewHolder {
        ImageView view;
    }
}
