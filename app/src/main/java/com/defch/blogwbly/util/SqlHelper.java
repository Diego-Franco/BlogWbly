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
    private static final String TAG = SqlHelper.class.getSimpleName();

    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "weebly.db";

    private static SQLiteDatabase mReadableDatabase;

    private static SQLiteDatabase mWritableDatabase;

    private long idPost;

    public SqlHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBMethods.PublishC.CREATE_TABLE_SQL);
        LogUtil.v(TAG, "post table created");
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
        LogUtil.v(TAG, "Inserting post " + bPost.toString());
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(DBMethods.PublishC._ID, bPost.getId());
        values.put(DBMethods.PublishC.COLUMN_TITLE, bPost.getTitle());
        values.put(DBMethods.PublishC.COLUMN_DESCRIPTION, bPost.getSubtitle());
        values.put(DBMethods.PublishC.COLUMN_LATITUDE, bPost.getLatitude());
        values.put(DBMethods.PublishC.COLUMN_LONGITUDE, bPost.getLongitude());
        values.put(DBMethods.PublishC.COLUMN_LAYOUT_ID, bPost.getLayoutId());
        this.idPost = db.insertWithOnConflict(DBMethods.PublishC.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
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
        values.put(DBMethods.PublishC.COLUMN_LATITUDE, bPost.getLatitude());
        values.put(DBMethods.PublishC.COLUMN_LONGITUDE, bPost.getLongitude());
        values.put(DBMethods.PublishC.COLUMN_LAYOUT_ID, bPost.getLayoutId());
        db.update(DBMethods.PublishC.TABLE_NAME, values, null, null);
        db.close();
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
        db.close();
    }

    public long getIdPost() {
        return idPost;
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
