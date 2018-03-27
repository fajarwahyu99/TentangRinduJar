package com.example.infolabsolution.databasemoviejar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.infolabsolution.databasemoviejar.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moviefilm.db";
    private static final int DATABASE_VERSION = 5;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " ("
                + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieEntry.COLUMN_MOVIE_NAME + " TEXT, "
                + MovieEntry.COLUMN_MOVIE_POSTER + " TEXT, "
                + MovieEntry.COLUMN_MOVIE_RATING + " TEXT, "
                + MovieEntry.COLUMN_MOVIE_DATE + " TEXT, "
                + MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT, "
                + MovieEntry.COLUMN_MOVIE_ID + " INTEGER);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
