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

    private Context movieContext;
    private ArrayList<Movie> listMovies;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        listMovies = movies;
        movieContext = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        FrameLayout itemView = (FrameLayout) LayoutInflater.from(movieContext)
                .inflate(R.layout.grid_view_item, parent, false);
        WindowManager moviedisplay = (WindowManager) movieContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = moviedisplay.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        itemView.setLayoutParams(new FrameLayout.LayoutParams(width / 3, (width * 41 / 27 / 3)));
        itemView.setPadding(2, 2, 2, 2);
        itemView.setBackgroundColor(Color.parseColor("#000000"));

        MovieViewHolder filmViewHolder = new MovieViewHolder(itemView);
        return filmViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        Picasso.with(movieContext).load(createImageUrlString(position)).into(holder.imgMovieImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent filmDetailIntent = new Intent(movieContext, MovieDetailActivity.class);
                filmDetailIntent.putExtra("title", listMovies.get(position).getMovieTitle());
                filmDetailIntent.putExtra("overview", listMovies.get(position).getMovieOverview());
                filmDetailIntent.putExtra("rating", listMovies.get(position).getMovieRating());
                filmDetailIntent.putExtra("date", listMovies.get(position).getMovieReleaseDate());
                filmDetailIntent.putExtra("image", createImageUrlString(position));
                filmDetailIntent.putExtra("id", listMovies.get(position).getMovieId());
                movieContext.startActivity(filmDetailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listMovies != null) {
            return listMovies.size();
        } else {
            return 0;
        }
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMovieImage;

        public MovieViewHolder(FrameLayout itemView) {
            super(itemView);
            imgMovieImage = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public String createImageUrlString(int ImagePosition) {
        String imagePath = listMovies.get(ImagePosition).getMovieImage();
        return BuildConfig.BASE_URL + BuildConfig.IMAGE_SIZE + imagePath;
    }
}
