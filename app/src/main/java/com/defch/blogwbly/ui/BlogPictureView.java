package com.defch.blogwbly.ui;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DiegoFranco on 4/17/15.
 */
public class BlogPictureView implements Parcelable {

    private Bitmap bmp;
    private Uri uri;
    private double latitude;
    private double longitude;
    private boolean isMapPicture;

    public BlogPictureView(){}

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

    protected BlogPictureView(Parcel in) {
        bmp = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        uri = (Uri) in.readValue(Uri.class.getClassLoader());
        latitude = in.readDouble();
        longitude = in.readDouble();
        isMapPicture = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(bmp);
        dest.writeValue(uri);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeByte((byte) (isMapPicture ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BlogPictureView> CREATOR = new Parcelable.Creator<BlogPictureView>() {
        @Override
        public BlogPictureView createFromParcel(Parcel in) {
            return new BlogPictureView(in);
        }

        @Override
        public BlogPictureView[] newArray(int size) {
            return new BlogPictureView[size];
        }
    };
}
