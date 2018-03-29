package com.example.infolabsolution.databasemoviejar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.infolabsolution.databasemoviejar.MovieContract;
import com.squareup.picasso.Picasso;

public class FavoriteMovieCursorAdapter extends CursorAdapter {
    public FavoriteMovieCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        FrameLayout itemView = (FrameLayout) LayoutInflater.from(context)
                .inflate(R.layout.grid_view_item, viewGroup, false);
        WindowManager moviedisplay = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = moviedisplay.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        itemView.setLayoutParams(new FrameLayout.LayoutParams(width / 3, (width * 41 / 27 / 3)));
        itemView.setPadding(2, 2, 2, 2);
        itemView.setBackgroundColor(Color.parseColor("#000000"));
        return itemView;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final String movieTitle = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_NAME));
        final String movieRating = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_RATING));
        final String movieReleaseDate = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_DATE));
        final String movieOverview = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW));
        final String moviePoster = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER));

        ImageView ivPoster = (ImageView) view.findViewById(R.id.image);
        Picasso.with(context).load(moviePoster).into(ivPoster);
        ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent defavIntent = new Intent(context, FavoriteMoviesActivity.class);
                defavIntent.putExtra("title", movieTitle);
                defavIntent.putExtra("overview", movieOverview);
                defavIntent.putExtra("rating", movieRating);
                defavIntent.putExtra("date", movieReleaseDate);
                defavIntent.putExtra("image", moviePoster);
                context.startActivity(defavIntent);
            }
        });
    }
}
