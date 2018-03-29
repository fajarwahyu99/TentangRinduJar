package com.example.infolabsolution.databasemoviejar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.infolabsolution.databasemoviejar.MovieContract;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.infolabsolution.databasemoviejar.NetworkUtils.createUrl;
import static com.example.infolabsolution.databasemoviejar.NetworkUtils.extractResponseFromJson;
import static com.example.infolabsolution.databasemoviejar.NetworkUtils.getResponseFromHttpUrl;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public MovieAdapter movieAdapter;
    public MovieAdapter upcomingMovieAdapter;
    public RecyclerView movieGridView;
    public GridView gvFavoriteMovie;
    public ArrayList<Movie> listMovies;
    public ArrayList<Movie> listupcomingMovies;
    public MovieParcelable upcomingParcelable;
    public MovieParcelable nowplayingParcelable;
    public MovieParcelable favoriteParcelable;
    public ArrayList<MovieParcelable> listupcomingParcelable;
    public ArrayList<MovieParcelable> listnowplayingParcelable;
    public ArrayList<MovieParcelable> listfavoriteParcelable;
    public FavoriteMovieCursorAdapter favAdapter;
    private static final int FAVORITE_MOVIE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(MainActivity.this, getString(R.string.network_connection_error_message),
                    Toast.LENGTH_LONG).show();
        }

        favAdapter = new FavoriteMovieCursorAdapter(this, null);

        movieGridView = (RecyclerView) findViewById(R.id.movie_recycler_view);
        movieGridView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(getApplicationContext(), 3, LinearLayoutManager.VERTICAL, false);
        movieGridView.setLayoutManager(gridLayoutManager);

        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("key") != null) {
            UpMovieForParcelableAsyncTask taskForMovieParcelable = new UpMovieForParcelableAsyncTask();
            taskForMovieParcelable.execute();
            UpComingMovieAsyncTask taskUpMovieAsync = new UpComingMovieAsyncTask();
            taskUpMovieAsync.execute();

        } else if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("nowplaying_key") != null) {
            MovieForParcelableAsyncTask taskForMovieParcelable = new MovieForParcelableAsyncTask();
            taskForMovieParcelable.execute();
            MovieAsyncTask taskNowPlayingAsync = new MovieAsyncTask();
            taskNowPlayingAsync.execute();
        } else if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("favorite_key") != null) {
            Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null, null);
            favAdapter = new FavoriteMovieCursorAdapter(this, cursor);
            movieGridView.setVisibility(View.INVISIBLE);
            gvFavoriteMovie = (GridView) findViewById(R.id.favoriteMovieGridView);
            gvFavoriteMovie.setVisibility(View.VISIBLE);
            gvFavoriteMovie.setNumColumns(3);
            gvFavoriteMovie.setAdapter(favAdapter);
            getFavoriteMovies(cursor);
        } else {
            MovieAsyncTask task = new MovieAsyncTask();
            task.execute();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("key", listupcomingParcelable);
        outState.putParcelableArrayList("nowplaying_key", listnowplayingParcelable);
        outState.putParcelableArrayList("favorite_key", listfavoriteParcelable);
    }

    private class MovieAsyncTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            URL playMovieUrl = createUrl(BuildConfig.TMDB_NOWPLAYING_URL);
            String jsonResponse = " ";
            try {
                jsonResponse = getResponseFromHttpUrl(playMovieUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<Movie> movies = extractResponseFromJson(jsonResponse);
            return movies;
        }

        @Override
        protected void onPostExecute(final ArrayList<Movie> movies) {
            listMovies = movies;
            movieAdapter = new MovieAdapter(MainActivity.this, listMovies);
            movieGridView.setAdapter(movieAdapter);
        }
    }

    private class UpComingMovieAsyncTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            URL upComingMovieUrl = createUrl(BuildConfig.TMDB_UPCOMING_URL);
            String jsonResponse = " ";
            try {
                jsonResponse = getResponseFromHttpUrl(upComingMovieUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<Movie> movies = extractResponseFromJson(jsonResponse);
            return movies;
        }

        @Override
        protected void onPostExecute(final ArrayList<Movie> movies) {
            listupcomingMovies = movies;
            upcomingMovieAdapter = new MovieAdapter(MainActivity.this, listupcomingMovies);
            movieGridView.setAdapter(upcomingMovieAdapter);
        }
    }

    private class MovieForParcelableAsyncTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            URL playMovieUrl = createUrl(BuildConfig.TMDB_NOWPLAYING_URL);
            String jsonResponse = " ";
            try {
                jsonResponse = getResponseFromHttpUrl(playMovieUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<Movie> movies = extractResponseFromJson(jsonResponse);
            return movies;
        }

        @Override
        protected void onPostExecute(final ArrayList<Movie> movies) {
            if (movies != null && !movies.isEmpty()) {
                for (int i = 0; i < movies.size(); i++) {
                    nowplayingParcelable = new MovieParcelable
                            (movies.get(i).getMovieTitle(),
                                    movies.get(i).getMovieOverview(),
                                    movies.get(i).getMovieReleaseDate(),
                                    movies.get(i).getMovieImage(),
                                    movies.get(i).getMovieRating(),
                                    movies.get(i).getMovieId());
                    listnowplayingParcelable = new ArrayList<>();
                    listnowplayingParcelable.add(upcomingParcelable);
                }
            }
        }
    }

    private class UpMovieForParcelableAsyncTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            URL playMovieUrl = createUrl(BuildConfig.TMDB_UPCOMING_URL);
            String jsonResponse = " ";
            try {
                jsonResponse = getResponseFromHttpUrl(playMovieUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<Movie> movies = extractResponseFromJson(jsonResponse);
            return movies;
        }

        @Override
        protected void onPostExecute(final ArrayList<Movie> movies) {
            if (movies != null && !movies.isEmpty()) {
                for (int i = 0; i < movies.size(); i++) {
                    upcomingParcelable = new MovieParcelable
                            (movies.get(i).getMovieTitle(),
                                    movies.get(i).getMovieOverview(),
                                    movies.get(i).getMovieReleaseDate(),
                                    movies.get(i).getMovieImage(),
                                    movies.get(i).getMovieRating(),
                                    movies.get(i).getMovieId());
                    listupcomingParcelable = new ArrayList<>();
                    listupcomingParcelable.add(upcomingParcelable);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        MovieAsyncTask taskForReloadingPlayMovies = new MovieAsyncTask();
        UpComingMovieAsyncTask taskUpComingMovies = new UpComingMovieAsyncTask();
        gvFavoriteMovie = (GridView) findViewById(R.id.favoriteMovieGridView);


        if (itemThatWasClickedId == R.id.action_now_playing) {
            listupcomingParcelable = null;
            listfavoriteParcelable = null;
            gvFavoriteMovie.setVisibility(View.INVISIBLE);
            movieGridView.setVisibility(View.VISIBLE);
            taskForReloadingPlayMovies.execute();
            MovieForParcelableAsyncTask taskForPlayMovieParcelable = new MovieForParcelableAsyncTask();
            taskForPlayMovieParcelable.execute();
            return true;
        }

        if (itemThatWasClickedId == R.id.action_up_coming) {
            listnowplayingParcelable = null;
            listfavoriteParcelable = null;
            gvFavoriteMovie.setVisibility(View.INVISIBLE);
            movieGridView.setVisibility(View.VISIBLE);
            taskUpComingMovies.execute();
            UpMovieForParcelableAsyncTask taskForMovieParcelable = new UpMovieForParcelableAsyncTask();
            taskForMovieParcelable.execute();
            return true;
        }

        if (itemThatWasClickedId == R.id.action_bahasa) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }

        if (itemThatWasClickedId == R.id.action_favorite) {
            listupcomingParcelable = null;
            listnowplayingParcelable = null;
            Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null, null);
            favAdapter = new FavoriteMovieCursorAdapter(this, cursor);
            movieGridView.setVisibility(View.INVISIBLE);
            gvFavoriteMovie.setVisibility(View.VISIBLE);
            gvFavoriteMovie.setNumColumns(3);
            gvFavoriteMovie.setAdapter(favAdapter);
            getFavoriteMovies(cursor);
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<MovieParcelable> getFavoriteMovies(Cursor cursor) {
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                favoriteParcelable = new MovieParcelable(
                        cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER)),
                        Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_RATING))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_ID))));
                listfavoriteParcelable = new ArrayList<>();
                listfavoriteParcelable.add(favoriteParcelable);
                cursor.moveToNext();
            }
        }
        return listfavoriteParcelable;
    }
}
