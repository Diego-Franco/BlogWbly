package com.defch.blogwbly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.VideoView;

import com.defch.blogwbly.R;
import com.defch.blogwbly.ui.BlogPictureView;

import java.util.ArrayList;

/**
 * Created by DiegoFranco on 4/18/15.
 */
public class AdapterPostPictures extends ArrayAdapter<BlogPictureView> {

    private Context context;
    private ArrayList<BlogPictureView> pictures;

    public AdapterPostPictures(Context context, ArrayList<BlogPictureView> objects) {
        super(context, 0, objects);
        this.context = context;
        this.pictures = objects;
    }

    @Override
    public View getView(int position, View v, final ViewGroup parent) {
        ViewHolder holder;
        BlogPictureView pictureView = pictures.get(position);
        if(v == null) {
            v = LayoutInflater.from(this.getContext()).inflate(R.layout.widget_post_blog, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView)v.findViewById(R.id.widget_img);
            holder.videoView = (VideoView)v.findViewById(R.id.widget_video);
            pictureView.setViews(v, parent);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        if(pictureView != null) {
            if(pictureView.getPicture() != null) {
                holder.imageView.setImageBitmap(pictureView.getPicture());
            } else if(pictureView.getVideo() != null) {
                holder.videoView.setVideoURI(pictureView.getVideo());
            }

        }

        return v;
    }

    private static class ViewHolder {
        ImageView imageView;
        VideoView videoView;
    }
}
