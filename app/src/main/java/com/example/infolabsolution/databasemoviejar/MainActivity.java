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
    private static final String TMDB_POPULAR_MOVIE_REQUEST_URL =
            "http://api.themoviedb.org/3/movie/now_playing?api_key=92fc8095e11194d676367347621d94c0";
    private static final String TMDB_TOP_RATED_MOVIE_REQUEST_URL =
            "http://api.themoviedb.org/3/movie/upcoming?api_key=92fc8095e11194d676367347621d94c0";
    public MovieAdapter movieAdapter;
    public MovieAdapter topRatedMovieAdapter;
    public RecyclerView movieGridView;
    public GridView mFavoriteMovieGridView;
    public ArrayList<Movie> mMovies;
    public ArrayList<Movie> mTopRatedMovies;
    public MovieParcelable mTopRatedMoviesParcelable;
    public MovieParcelable mPopularMovieParcelable;
    public MovieParcelable mFavoriteMovieParcelable;
    public ArrayList<MovieParcelable> mTopRatedMoviesParcelableArrayList;
    public ArrayList<MovieParcelable> mPopularMovieParcelableArrayList;
    public ArrayList<MovieParcelable> mFavoriteMovieParcelableArrayList;
    public FavoriteMovieCursorAdapter mAdapter;
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

        mAdapter = new FavoriteMovieCursorAdapter(this, null);

        movieGridView = (RecyclerView) findViewById(R.id.movie_recycler_view);
        movieGridView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(getApplicationContext(), 3, LinearLayoutManager.VERTICAL, false);
        movieGridView.setLayoutManager(gridLayoutManager);

        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("key") != null) {
            TopMovieForParcelableAsyncTask taskForMovieParcelable = new TopMovieForParcelableAsyncTask();
            taskForMovieParcelable.execute();
            TopRatedMovieAsyncTask taskTopMovieAsync = new TopRatedMovieAsyncTask();
            taskTopMovieAsync.execute();

        } else if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("popular_key") != null) {
            MovieForParcelableAsyncTask taskForMovieParcelable = new MovieForParcelableAsyncTask();
            taskForMovieParcelable.execute();
            MovieAsyncTask taskPopularMovieAsync = new MovieAsyncTask();
            taskPopularMovieAsync.execute();
        } else if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("favorite_key") != null) {
            Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null, null);
            mAdapter = new FavoriteMovieCursorAdapter(this, cursor);
            movieGridView.setVisibility(View.INVISIBLE);
            mFavoriteMovieGridView = (GridView) findViewById(R.id.favoriteMovieGridView);
            mFavoriteMovieGridView.setVisibility(View.VISIBLE);
            mFavoriteMovieGridView.setNumColumns(3);
            mFavoriteMovieGridView.setAdapter(mAdapter);
            getFavoriteMovies(cursor);
        } else {
            MovieAsyncTask task = new MovieAsyncTask();
            task.execute();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("key", mTopRatedMoviesParcelableArrayList);
        outState.putParcelableArrayList("popular_key", mPopularMovieParcelableArrayList);
        outState.putParcelableArrayList("favorite_key", mFavoriteMovieParcelableArrayList);
    }

    private class MovieAsyncTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            URL popularMovieUrl = createUrl(TMDB_POPULAR_MOVIE_REQUEST_URL);
            String jsonResponse = " ";
            try {
                jsonResponse = getResponseFromHttpUrl(popularMovieUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<Movie> movies = extractResponseFromJson(jsonResponse);
            return movies;
        }

        @Override
        protected void onPostExecute(final ArrayList<Movie> movies) {
            mMovies = movies;
            movieAdapter = new MovieAdapter(MainActivity.this, mMovies);
            movieGridView.setAdapter(movieAdapter);
        }
    }

    private class TopRatedMovieAsyncTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            URL topRatedMovieUrl = createUrl(TMDB_TOP_RATED_MOVIE_REQUEST_URL);
            String jsonResponse = " ";
            try {
                jsonResponse = getResponseFromHttpUrl(topRatedMovieUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<Movie> movies = extractResponseFromJson(jsonResponse);
            return movies;
        }

        @Override
        protected void onPostExecute(final ArrayList<Movie> movies) {
            mTopRatedMovies = movies;
            topRatedMovieAdapter = new MovieAdapter(MainActivity.this, mTopRatedMovies);
            movieGridView.setAdapter(topRatedMovieAdapter);
        }
    }

    private class MovieForParcelableAsyncTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            URL popularMovieUrl = createUrl(TMDB_POPULAR_MOVIE_REQUEST_URL);
            String jsonResponse = " ";
            try {
                jsonResponse = getResponseFromHttpUrl(popularMovieUrl);
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
                    mPopularMovieParcelable = new MovieParcelable
                            (movies.get(i).getMovieTitle(),
                                    movies.get(i).getMovieOverview(),
                                    movies.get(i).getMovieReleaseDate(),
                                    movies.get(i).getMovieImage(),
                                    movies.get(i).getMovieRating(),
                                    movies.get(i).getMovieId());
                    mPopularMovieParcelableArrayList = new ArrayList<>();
                    mPopularMovieParcelableArrayList.add(mTopRatedMoviesParcelable);
                }
            }
        }
    }

    private class TopMovieForParcelableAsyncTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            URL popularMovieUrl = createUrl(TMDB_TOP_RATED_MOVIE_REQUEST_URL);
            String jsonResponse = " ";
            try {
                jsonResponse = getResponseFromHttpUrl(popularMovieUrl);
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
                    mTopRatedMoviesParcelable = new MovieParcelable
                            (movies.get(i).getMovieTitle(),
                                    movies.get(i).getMovieOverview(),
                                    movies.get(i).getMovieReleaseDate(),
                                    movies.get(i).getMovieImage(),
                                    movies.get(i).getMovieRating(),
                                    movies.get(i).getMovieId());
                    mTopRatedMoviesParcelableArrayList = new ArrayList<>();
                    mTopRatedMoviesParcelableArrayList.add(mTopRatedMoviesParcelable);
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
        MovieAsyncTask taskForReloadingPopularMovies = new MovieAsyncTask();
        TopRatedMovieAsyncTask taskForTopRatedMovies = new TopRatedMovieAsyncTask();
        mFavoriteMovieGridView = (GridView) findViewById(R.id.favoriteMovieGridView);


        if (itemThatWasClickedId == R.id.action_now_playing) {
            mTopRatedMoviesParcelableArrayList = null;
            mFavoriteMovieParcelableArrayList = null;
            mFavoriteMovieGridView.setVisibility(View.INVISIBLE);
            movieGridView.setVisibility(View.VISIBLE);
            taskForReloadingPopularMovies.execute();
            MovieForParcelableAsyncTask taskForPopularMovieParcelable = new MovieForParcelableAsyncTask();
            taskForPopularMovieParcelable.execute();
            return true;
        }

        if (itemThatWasClickedId == R.id.action_up_coming) {
            mPopularMovieParcelableArrayList = null;
            mFavoriteMovieParcelableArrayList = null;
            mFavoriteMovieGridView.setVisibility(View.INVISIBLE);
            movieGridView.setVisibility(View.VISIBLE);
            taskForTopRatedMovies.execute();
            TopMovieForParcelableAsyncTask taskForMovieParcelable = new TopMovieForParcelableAsyncTask();
            taskForMovieParcelable.execute();
            return true;
        }

        if (itemThatWasClickedId == R.id.action_bahasa) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }

        if (itemThatWasClickedId == R.id.action_favorite) {
            mTopRatedMoviesParcelableArrayList = null;
            mPopularMovieParcelableArrayList = null;
            Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null, null);
            mAdapter = new FavoriteMovieCursorAdapter(this, cursor);
            movieGridView.setVisibility(View.INVISIBLE);
            mFavoriteMovieGridView.setVisibility(View.VISIBLE);
            mFavoriteMovieGridView.setNumColumns(3);
            mFavoriteMovieGridView.setAdapter(mAdapter);
            getFavoriteMovies(cursor);
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<MovieParcelable> getFavoriteMovies(Cursor cursor) {
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mFavoriteMovieParcelable = new MovieParcelable(
                        cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER)),
                        Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_RATING))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_ID))));
                mFavoriteMovieParcelableArrayList = new ArrayList<>();
                mFavoriteMovieParcelableArrayList.add(mFavoriteMovieParcelable);
                cursor.moveToNext();
            }
        }
        return mFavoriteMovieParcelableArrayList;
    }
}
