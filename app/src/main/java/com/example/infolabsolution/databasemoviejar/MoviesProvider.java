package com.example.infolabsolution.databasemoviejar;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.example.infolabsolution.databasemoviejar.MovieContract.*;

public class MoviesProvider extends ContentProvider{
    private MovieDbHelper movieDBHelper;
    private static final int MOVIES = 200;
    private static final int MOVIE_ID = 201;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(BuildConfig.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        sUriMatcher.addURI(BuildConfig.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_ID);
    }



    @Override
    public boolean onCreate() {
        movieDBHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = movieDBHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                cursor = database.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case MOVIE_ID:
                selection = MovieEntry._ID + "?=";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return insertMovie(uri, contentValues);
            default:
                throw new IllegalArgumentException("\"Insertion is not supported for \" + uri");
        }
    }

    private Uri insertMovie(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = movieDBHelper.getWritableDatabase();
        long id = database.insert(MovieEntry.TABLE_NAME, null, contentValues);

        if (id == -1) {
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowGone;
        SQLiteDatabase database = movieDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                rowGone = database.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                if (rowGone != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowGone;
            case MOVIE_ID:
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowGone = database.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                if (rowGone != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowGone;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
