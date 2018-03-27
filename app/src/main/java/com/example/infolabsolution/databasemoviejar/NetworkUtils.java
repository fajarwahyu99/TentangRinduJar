package com.example.infolabsolution.databasemoviejar;

import android.util.Log;

import com.example.infolabsolution.databasemoviejar.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static com.example.infolabsolution.databasemoviejar.MainActivity.LOG_TAG;

public class NetworkUtils {

    public static ArrayList<Movie> extractResponseFromJson(String jsonRequestResults) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject rootJsonResponse = new JSONObject(jsonRequestResults);
            JSONArray resultsArray = rootJsonResponse.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObject = resultsArray.getJSONObject(i);

                String posterPath = resultObject.getString("poster_path");
                String overview = resultObject.getString("overview");
                String releaseDate = resultObject.getString("release_date");
                int id = Integer.parseInt(resultObject.getString("id"));
                String title = resultObject.getString("title");
                double voteAverage = resultObject.getDouble("vote_average");

                movies.add(new Movie(title, overview, releaseDate, posterPath, voteAverage, id));
            }
            return movies;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the movie JSON results", e);
        }
        return null;
    }


    public static ArrayList<String> extractReviewFromJson(String jsonRequestResults) {
        ArrayList<String> reviews = new ArrayList<>();
        try{
            JSONObject rootJsonObject = new JSONObject(jsonRequestResults);
            JSONArray resultsArray = rootJsonObject.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObject = resultsArray.getJSONObject(i);
                String review = resultObject.getString("content");
                reviews.add(review);
            }
            return reviews;
        }catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the review JSON results", e);
        }
        return null;
    }

    public static URL createUrl(String urlString) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException exception) {
            return null;
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
