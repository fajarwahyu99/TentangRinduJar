package com.example.infolabsolution.databasemoviejar;

public class Movie {
    private String textTitle;
    private String textOverview;
    private String textReleaseDate;
    private String imgMovies;
    private double textRating;
    private int textMovieId;

    public Movie(String title, String overview, String releaseDate, String image, double rating, int id) {
        textTitle = title;
        textOverview = overview;
        textReleaseDate = releaseDate;
        imgMovies = image;
        textRating = rating;
        textMovieId = id;
    }

    public String getMovieTitle() {
        return textTitle;
    }

    public String getMovieOverview() {
        return textOverview;
    }

    public String getMovieReleaseDate() {
        return textReleaseDate;
    }

    public String getMovieImage() {
        return imgMovies;
    }

    public double getMovieRating() {
        return textRating;
    }

    public int getMovieId() {
        return textMovieId;
    }

}
