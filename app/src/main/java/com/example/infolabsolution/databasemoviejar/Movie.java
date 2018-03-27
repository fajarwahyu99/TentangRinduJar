package com.example.infolabsolution.databasemoviejar;

public class Movie {
    private String mMovieTitle;
    private String mMovieOverview;
    private String mMovieReleaseDate;
    private String mMovieImage;
    private double mMovieRating;
    private int mMovieId;

    public Movie(String title, String overview, String releaseDate, String image, double rating, int id) {
        mMovieTitle = title;
        mMovieOverview = overview;
        mMovieReleaseDate = releaseDate;
        mMovieImage = image;
        mMovieRating = rating;
        mMovieId = id;
    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public String getMovieOverview() {
        return mMovieOverview;
    }

    public String getMovieReleaseDate() {
        return mMovieReleaseDate;
    }

    public String getMovieImage() {
        return mMovieImage;
    }

    public double getMovieRating() {
        return mMovieRating;
    }

    public int getMovieId() {
        return mMovieId;
    }

}
