package com.example.infolabsolution.databasemoviejar;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.infolabsolution.databasemoviejar.databinding.ActivityFavoriteMoviesBinding;
import com.squareup.picasso.Picasso;

public class FavoriteMoviesActivity extends AppCompatActivity{

    public ActivityFavoriteMoviesBinding mBinding;
    public Intent favoritesGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);
        favoritesGridView = getIntent();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_favorite_movies);

        if (favoritesGridView.hasExtra("image")) {
            Picasso.with(this).load(favoritesGridView.getStringExtra("image")).into(mBinding.ivFavorites);
        }
        if (favoritesGridView.hasExtra("overview")) {
            mBinding.tvFavoritesOverview.setText(favoritesGridView.getStringExtra("overview"));
        }
        if (favoritesGridView.hasExtra("title")) {
            mBinding.tvFavoritesTitle.setText(favoritesGridView.getStringExtra("title"));
        }
        if (favoritesGridView.hasExtra("date")) {
            mBinding.tvFavoritesRelease.setText(favoritesGridView.getStringExtra("date"));
        }
        if (favoritesGridView.hasExtra("rating")) {
            mBinding.tvFavoritesRating.setText(favoritesGridView.getStringExtra("rating"));
        }
    }
}
