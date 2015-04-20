package com.defch.blogwbly.util;

import android.provider.BaseColumns;

/**
 * Created by DiegoFranco on 4/17/15.
 */
public class DBMethods {
    public static class PublishC implements BaseColumns {
        public static final String TABLE_NAME = "post";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_PICTURE = "picture";

        public static final String COLUMN_LATITUDE = "latitude";

        public static final String COLUMN_LONGITUDE = "longitude";

        public static final String COLUMN_LAYOUT_ID = "layoutid";

        public static final String CREATE_TABLE_SQL =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY ASC AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT NOT NULL," +
                COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                COLUMN_PICTURE + " BLOB," +
                COLUMN_LATITUDE + " INTEGER," +
                COLUMN_LONGITUDE + " INTEGER," +
                COLUMN_LAYOUT_ID + " INTEGER);";

        public static int COLUMN_INDEX_ID = 0;

        public static int COLUMN_INDEX_TITLE = 1;

        public static int COLUMN_INDEX_DESCRIPTION = 2;

        public static int COLUMN_INDEX_PICTURE = 3;

        public static int COLUMN_INDEX_LATITUDE = 4;

        public static int COLUMN_INDEX_LONGITUDE = 5;

        public static int COLUMN_INDEX_LAYOUT_ID= 6;

        public static final String GET_POST_SQL = "SELECT * FROM " + TABLE_NAME
                + " ORDER BY " + _ID + " COLLATE NOCASE ASC";
        
        public static final String DELETE_POST_SQL = "DELETE FROM " + TABLE_NAME + " WHERE " +
                _ID + " =%d";
    }

}
