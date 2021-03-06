package com.defch.blogwbly.model;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.defch.blogwbly.util.DBMethods;

import java.util.ArrayList;

/**
 * Created by DiegoFranco on 4/17/15.
 */
public class BlogPost implements Parcelable {

    private int id;
    private ArrayList<Bitmap> thumbnails;
    private double latitude;
    private double longitude;
    private String title;
    private String subtitle;
    private int layoutId;

    public BlogPost() { }

    public BlogPost(Cursor cursor) {
        setId(cursor.getInt(DBMethods.PublishC.COLUMN_INDEX_ID));
        setTitle(cursor.getString(DBMethods.PublishC.COLUMN_INDEX_TITLE));
        setSubtitle(cursor.getString(DBMethods.PublishC.COLUMN_INDEX_DESCRIPTION));
        setLatitude(cursor.getDouble(DBMethods.PublishC.COLUMN_INDEX_LATITUDE));
        setLongitude(cursor.getDouble(DBMethods.PublishC.COLUMN_INDEX_LONGITUDE));
        setLayoutId(cursor.getInt(DBMethods.PublishC.COLUMN_INDEX_LAYOUT_ID));
        //setThumbnails(FileUtil.getImagesFromFolder(id));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Bitmap> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(ArrayList<Bitmap> thumbnails) {
        this.thumbnails = thumbnails;
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

    protected BlogPost(Parcel in) {
        id = in.readInt();
        if (in.readByte() == 0x01) {
            thumbnails = new ArrayList<Bitmap>();
            in.readList(thumbnails, Bitmap.class.getClassLoader());
        } else {
            thumbnails = null;
        }
        latitude = in.readDouble();
        longitude = in.readDouble();
        title = in.readString();
        subtitle = in.readString();
        layoutId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        if (thumbnails == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(thumbnails);
        }
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeInt(layoutId);
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
