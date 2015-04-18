package com.defch.blogwbly.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.defch.blogwbly.R;

/**
 * Created by DiegoFranco on 4/17/15.
 */
public class BlogPictureView extends RelativeLayout {

    private Context context;
    private ImageView imageView, btnClose;
    private VideoView videoView;

    private Bitmap bmp;
    private Uri uri;

    private View v;
    private ViewGroup vParent;

    public BlogPictureView(Context context) {
        super(context);
        this.context = context;

        init();
    }

    public BlogPictureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlogPictureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init() {
        LayoutInflater.from(context).inflate(R.layout.widget_post_blog, this);
        btnClose = (ImageView)findViewById(R.id.widget_close_btn);
        imageView = (ImageView)findViewById(R.id.widget_img);
        videoView = (VideoView)findViewById(R.id.widget_video);

    }

    public Bitmap getPicture() {
        return bmp;
    }

    public void setPicture(Bitmap bmp) {
        this.bmp = bmp;
    }

    public Uri getVideo() {
        return uri;
    }

    public void setVideo(Uri uri) {
        this.uri = uri;
    }

    public void setViews(View vi, ViewGroup viParent) {
        this.v = vi;
        this.vParent = viParent;
        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                vParent.removeView(v);
            }
        });
    }

    private class ClickClose implements OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }

}
