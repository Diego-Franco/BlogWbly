package com.defch.blogwbly.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.defch.blogwbly.model.BlogPost;

import java.util.ArrayList;

/**
 * Created by DiegoFranco on 4/17/15.
 */
public class SqlHelper extends SQLiteOpenHelper {
    private static final String TAG = "SqlHelper";

    private static final int DB_VERSION = 5;

    private static final String DB_NAME = "weebly.db";

    private static SQLiteDatabase mReadableDatabase;

    private static SQLiteDatabase mWritableDatabase;

    public SqlHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBMethods.PublishC.CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        onCreate(db);
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        if (mReadableDatabase == null || !mReadableDatabase.isOpen()) {
            mReadableDatabase = super.getReadableDatabase();
        }

        return mReadableDatabase;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        if (mWritableDatabase == null || !mWritableDatabase.isOpen()) {
            mWritableDatabase = super.getWritableDatabase();
        }

        return mWritableDatabase;
    }

    /**
     * Inserts the user to the database
     *
     * @param bPost
     */
    public void insertPost(@NonNull BlogPost bPost) {
        LogUtil.v(TAG, "Inserting user " + bPost.toString());
        // Wipe any users before we add the new one in
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DBMethods.PublishC.TABLE_NAME, null, null);

        ContentValues values = new ContentValues();
        values.put(DBMethods.PublishC._ID, bPost.getId());
        values.put(DBMethods.PublishC.COLUMN_TITLE, bPost.getTitle());
        values.put(DBMethods.PublishC.COLUMN_DESCRIPTION, bPost.getSubtitle());
        values.put(DBMethods.PublishC.COLUMN_PICTURE, bPost.getBitmapArray());
        values.put(DBMethods.PublishC.COLUMN_LATITUDE, bPost.getLatitude());
        values.put(DBMethods.PublishC.COLUMN_LONGITUDE, bPost.getLongitude());
        db.insert(DBMethods.PublishC.TABLE_NAME, null, values);
    }

    /**
     * Updates the user's information
     *
     * @param bPost
     */
    public void updatePost(@NonNull BlogPost bPost) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBMethods.PublishC._ID, bPost.getId());
        values.put(DBMethods.PublishC.COLUMN_TITLE, bPost.getTitle());
        values.put(DBMethods.PublishC.COLUMN_DESCRIPTION, bPost.getSubtitle());
        values.put(DBMethods.PublishC.COLUMN_PICTURE, bPost.getBitmapArray());
        values.put(DBMethods.PublishC.COLUMN_LATITUDE, bPost.getLatitude());
        values.put(DBMethods.PublishC.COLUMN_LONGITUDE, bPost.getLongitude());
        db.update(DBMethods.PublishC.TABLE_NAME, values, null, null);
    }
    

    /**
     * Returns a list of all the cached topics
     *
     * @return
     */
    public ArrayList<BlogPost> getPosts() {
        ArrayList<BlogPost> topics = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(DBMethods.PublishC.GET_POST_SQL, null);

        while (cursor.moveToNext()) {
            topics.add(new BlogPost(cursor));
        }

        cursor.close();
        return topics;
    }

    /**
     * Deletes a topic from the databse given its id
     *
     * @param id
     */
    public void deletePost(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format(DBMethods.PublishC.DELETE_POST_SQL, id));
    }
    

    @Override
    public synchronized void close() {
        if (mReadableDatabase != null) {
            mReadableDatabase.close();
            mReadableDatabase = null;
        }

        if (mWritableDatabase != null) {
            mWritableDatabase.close();
            mWritableDatabase = null;
        }

        super.close();
    }
}
