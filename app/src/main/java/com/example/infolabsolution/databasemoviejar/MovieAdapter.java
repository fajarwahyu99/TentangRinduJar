package com.example.infolabsolution.databasemoviejar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185";

    private Context mContext;
    private ArrayList<Movie> mMovies;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        mMovies = movies;
        mContext = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        FrameLayout itemView = (FrameLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.grid_view_item, parent, false);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        itemView.setLayoutParams(new FrameLayout.LayoutParams(width / 3, (width * 41 / 27 / 3)));
        itemView.setPadding(2, 2, 2, 2);
        itemView.setBackgroundColor(Color.parseColor("#000000"));

        MovieViewHolder movieViewHolder = new MovieViewHolder(itemView);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        Picasso.with(mContext).load(createImageUrlString(position)).into(holder.mMovieImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movieDetailIntent = new Intent(mContext, MovieDetailActivity.class);
                movieDetailIntent.putExtra("title", mMovies.get(position).getMovieTitle());
                movieDetailIntent.putExtra("overview", mMovies.get(position).getMovieOverview());
                movieDetailIntent.putExtra("rating", mMovies.get(position).getMovieRating());
                movieDetailIntent.putExtra("date", mMovies.get(position).getMovieReleaseDate());
                movieDetailIntent.putExtra("image", createImageUrlString(position));
                movieDetailIntent.putExtra("id", mMovies.get(position).getMovieId());
                mContext.startActivity(movieDetailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mMovies != null) {
            return mMovies.size();
        } else {
            return 0;
        }
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView mMovieImageView;

        public MovieViewHolder(FrameLayout itemView) {
            super(itemView);
            mMovieImageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public String createImageUrlString(int ImagePosition) {
        String imagePath = mMovies.get(ImagePosition).getMovieImage();
        return BASE_URL + IMAGE_SIZE + imagePath;
    }
}
