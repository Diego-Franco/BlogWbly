package com.defch.blogwbly.model;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.defch.blogwbly.util.DBMethods;

import java.io.ByteArrayOutputStream;

/**
 * Created by DiegoFranco on 4/17/15.
 */
public class BlogPost implements Parcelable {

    private int id;
    private Bitmap thumbnail;
    private double latitude;
    private double longitude;
    private String title;
    private String subtitle;
    private int layoutId;

    private byte[] bitmapArray;

    public BlogPost() { }

    public BlogPost(Cursor cursor) {
        setId(cursor.getInt(DBMethods.PublishC.COLUMN_INDEX_ID));
        setTitle(cursor.getString(DBMethods.PublishC.COLUMN_INDEX_TITLE));
        setSubtitle(cursor.getString(DBMethods.PublishC.COLUMN_INDEX_DESCRIPTION));
        setLatitude(cursor.getDouble(DBMethods.PublishC.COLUMN_INDEX_LATITUDE));
        setLongitude(cursor.getDouble(DBMethods.PublishC.COLUMN_INDEX_LONGITUDE));
        setLayoutId(cursor.getInt(DBMethods.PublishC.COLUMN_INDEX_LAYOUT_ID));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
        convertBitmapToArray();
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public boolean isMapView() {
        if(latitude > Integer.MIN_VALUE || longitude > Integer.MIN_VALUE) {
            return true;
        } else {
            return false;
        }
    }

    public byte[] getBitmapArray() {
        return bitmapArray;
    }

    private void convertBitmapToArray() {
        if(thumbnail != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
            bitmapArray = stream.toByteArray();
        }
    }

    private Bitmap convertByteArrayToBitmap(byte[] bitmapdata) {
       return BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
    }

    protected BlogPost(Parcel in) {
        thumbnail = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        latitude = in.readInt();
        longitude = in.readInt();
        title = in.readString();
        subtitle = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(thumbnail);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(title);
        dest.writeString(subtitle);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BlogPost> CREATOR = new Parcelable.Creator<BlogPost>() {
        @Override
        public BlogPost createFromParcel(Parcel in) {
            return new BlogPost(in);
        }

        @Override
        public BlogPost[] newArray(int size) {
            return new BlogPost[size];
        }
    };
}
