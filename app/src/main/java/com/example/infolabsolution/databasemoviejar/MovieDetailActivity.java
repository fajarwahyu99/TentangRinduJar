package com.example.infolabsolution.databasemoviejar;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.infolabsolution.databasemoviejar.MovieContract.MovieEntry;
import com.example.infolabsolution.databasemoviejar.databinding.ActivityMovieDetailBinding;
import com.squareup.picasso.Picasso;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import static com.example.infolabsolution.databasemoviejar.NetworkUtils.createUrl;
import static com.example.infolabsolution.databasemoviejar.NetworkUtils.extractReviewFromJson;
import static com.example.infolabsolution.databasemoviejar.NetworkUtils.getResponseFromHttpUrl;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MovieDetailActivity extends AppCompatActivity {

    String reviewStringUrl;
    ImageButton imgbuttonFavorite;
    public ShareActionProvider btnShare;
    ActivityMovieDetailBinding mBinding;
    RecyclerView reviewListView;
    int btnFavoriteUnclicked = R.drawable.favorite_white_24px;
    int btnFavoriteClicked = R.drawable.favorite_border_white_24px;
    int mId, querymId;
    Intent detailmovieIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        detailmovieIntent = getIntent();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        imgbuttonFavorite = (ImageButton) findViewById(R.id.favoriteButton);

        if (detailmovieIntent.hasExtra("image")) {
            Picasso.with(this).load(detailmovieIntent.getStringExtra("image")).into(mBinding.ivMoviess);
        }
        if (detailmovieIntent.hasExtra("overview")) {
            mBinding.tvmovieOverview.setText(detailmovieIntent.getStringExtra("overview"));
        }
        if (detailmovieIntent.hasExtra("title")) {
            mBinding.tvmovieTitle.setText(detailmovieIntent.getStringExtra("title"));
        }
        if (detailmovieIntent.hasExtra("date")) {
            mBinding.tvmovieRelease.setText(detailmovieIntent.getStringExtra("date"));
        }
        if (detailmovieIntent.hasExtra("rating")) {
            mBinding.tvmovieRating.setText(String.valueOf(detailmovieIntent.getDoubleExtra("rating", 0)));
        }
        if (detailmovieIntent.hasExtra("id")) {
            mId = detailmovieIntent.getIntExtra("id", 0);
            reviewStringUrl = "http://api.themoviedb.org/3/movie/" + mId + "/reviews?api_key=92fc8095e11194d676367347621d94c0";
            ReviewAsyncTask taskForReview = new ReviewAsyncTask();
            taskForReview.execute();
        }

        Cursor favoriteMovieCursor = getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null, null);
        imgbuttonFavorite.setImageResource(btnFavoriteUnclicked);
        imgbuttonFavorite.setTag(btnFavoriteUnclicked);
        if (favoriteMovieCursor != null) {
            favoriteMovieCursor.moveToFirst();
            try {
                while (!favoriteMovieCursor.isAfterLast()) {
                    querymId = favoriteMovieCursor.getInt(favoriteMovieCursor.getColumnIndexOrThrow("id"));
                    if (querymId != mId) {
                        favoriteMovieCursor.moveToNext();
                    } else {
                        imgbuttonFavorite.setImageResource(btnFavoriteClicked);
                        imgbuttonFavorite.setTag(btnFavoriteClicked);
                        break;
                    }
                }
            } finally {
                favoriteMovieCursor.close();
            }
        }

        imgbuttonFavorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Uri newUri;

                if (Integer.parseInt(imgbuttonFavorite.getTag().toString()) == btnFavoriteUnclicked) {
                    ContentValues values = new ContentValues();
                    values.put(MovieEntry.COLUMN_MOVIE_NAME, detailmovieIntent.getStringExtra("title"));
                    values.put(MovieEntry.COLUMN_MOVIE_POSTER, detailmovieIntent.getStringExtra("image"));
                    values.put(MovieEntry.COLUMN_MOVIE_RATING, String.valueOf(detailmovieIntent.getDoubleExtra("rating", 0)));
                    values.put(MovieEntry.COLUMN_MOVIE_DATE, detailmovieIntent.getStringExtra("date"));
                    values.put(MovieEntry.COLUMN_MOVIE_OVERVIEW, detailmovieIntent.getStringExtra("overview"));
                    values.put(MovieEntry.COLUMN_MOVIE_ID, detailmovieIntent.getIntExtra("id", 0));
                    newUri = getContentResolver().insert(MovieEntry.CONTENT_URI, values);
                    if (newUri == null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.add_favorite_fail_message),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        imgbuttonFavorite.setImageResource(btnFavoriteClicked);
                        imgbuttonFavorite.setTag(btnFavoriteClicked);
                        Toast.makeText(getApplicationContext(), getString(R.string.add_favorite_success_message),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Cursor favoriteCursor = getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null, null);
                    int rowId = 1;
                    if (favoriteCursor != null) {
                        favoriteCursor.moveToFirst();
                        rowId = favoriteCursor.getInt(favoriteCursor.getColumnIndexOrThrow(MovieEntry._ID));
                        try {
                            while (!favoriteCursor.isAfterLast()) {
                                int movieQueryId = favoriteCursor.getInt(favoriteCursor.getColumnIndexOrThrow(MovieEntry.COLUMN_MOVIE_ID));
                                if (movieQueryId != mId) {
                                    favoriteCursor.moveToNext();
                                    rowId = favoriteCursor.getInt(favoriteCursor.getColumnIndexOrThrow(MovieEntry._ID));
                                } else {
                                    break;
                                }
                            }
                        } finally {
                            favoriteCursor.close();
                        }
                    }

                    Uri movieToDeleteUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, rowId);
                    showDeleteConfirmationDialog(movieToDeleteUri);
                }
            }
        });
    }

    private void showDeleteConfirmationDialog(final Uri uri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Serious Delete this movie?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                if (uri != null) {
                    int rowGone = getContentResolver().delete(uri, null, null);
                    if (rowGone == 0) {
                        Toast.makeText(getApplicationContext(), "ERROR DELETING",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        imgbuttonFavorite.setImageResource(btnFavoriteUnclicked);
                        imgbuttonFavorite.setTag(btnFavoriteUnclicked);
                        Toast.makeText(getApplicationContext(), "FAVORITE DELETED",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private class ReviewAsyncTask extends AsyncTask<URL, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(URL... urls) {
            URL reviewUrl = createUrl(reviewStringUrl);
            String jsonResponse = " ";
            try {
                jsonResponse = getResponseFromHttpUrl(reviewUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<String> reviews = extractReviewFromJson(jsonResponse);
            return reviews;
        }

        @Override
        protected void onPostExecute(ArrayList<String> reviews) {
            ReviewAdapter adapter = new ReviewAdapter(getApplicationContext(), reviews);
            reviewListView = (RecyclerView) findViewById(R.id.review_recycler_view);
            reviewListView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            reviewListView.setLayoutManager(linearLayoutManager);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        btnShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent sIntent = new Intent(Intent.ACTION_SEND);
        sIntent.setType("text/plain");
        btnShare.setShareIntent(sIntent);
        return true;
    }
}
