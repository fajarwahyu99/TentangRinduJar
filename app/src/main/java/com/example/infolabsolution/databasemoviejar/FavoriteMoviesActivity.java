package com.example.infolabsolution.databasemoviejar;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.infolabsolution.databasemoviejar.databinding.ActivityFavoriteMoviesBinding;
import com.squareup.picasso.Picasso;

public class FavoriteMoviesActivity extends AppCompatActivity{

    public Intent mIntentFromFavoriteMovieGridView;
    public ActivityFavoriteMoviesBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);
        mIntentFromFavoriteMovieGridView = getIntent();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_favorite_movies);

        if (mIntentFromFavoriteMovieGridView.hasExtra("image")) {
            Picasso.with(this).load(mIntentFromFavoriteMovieGridView.getStringExtra("image")).into(mBinding.favoriteImageView);
        }
        if (mIntentFromFavoriteMovieGridView.hasExtra("overview")) {
            mBinding.favoriteOverviewTextView.setText(mIntentFromFavoriteMovieGridView.getStringExtra("overview"));
        }
        if (mIntentFromFavoriteMovieGridView.hasExtra("title")) {
            mBinding.favoriteTitleTextView.setText(mIntentFromFavoriteMovieGridView.getStringExtra("title"));
        }
        if (mIntentFromFavoriteMovieGridView.hasExtra("date")) {
            mBinding.favoriteReleaseDateTextView.setText(mIntentFromFavoriteMovieGridView.getStringExtra("date"));
        }
        if (mIntentFromFavoriteMovieGridView.hasExtra("rating")) {
            mBinding.favoriteRatingTextView.setText(mIntentFromFavoriteMovieGridView.getStringExtra("rating"));
        }
    }
}
