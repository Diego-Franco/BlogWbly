package com.defch.blogwbly.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.RelativeLayout;

/**
 * Created by DiegoFranco on 4/17/15.
 */
public class BlogPictureView extends RelativeLayout {

    private Bitmap bmp;
    private Uri uri;
    private double latitude, longitude;
    private boolean isMapPicture;

    public BlogPictureView(Context context) {
        super(context);
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isMapPicture() {
        return isMapPicture;
    }

    public void setIsMapPicture(boolean isMapPicture) {
        this.isMapPicture = isMapPicture;
    }
}
