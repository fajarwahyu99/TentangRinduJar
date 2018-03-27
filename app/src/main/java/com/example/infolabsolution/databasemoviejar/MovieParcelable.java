package com.example.infolabsolution.databasemoviejar;


import android.os.Parcel;
import android.os.Parcelable;

public class MovieParcelable implements Parcelable {

    String mMovieTitle;
    String mMovieReleaseDate;
    String mMovieOverview;
    double mMovieRating;
    String mMovieImagePath;
    int mMovieId;

    public MovieParcelable(String title, String overview, String date,
                           String imagePath, double rating, int id) {
        mMovieTitle = title;
        mMovieReleaseDate = date;
        mMovieOverview = overview;
        mMovieRating = rating;
        mMovieImagePath = imagePath;
        mMovieId = id;
    }

    protected MovieParcelable(Parcel in) {
        mMovieTitle = in.readString();
        mMovieReleaseDate = in.readString();
        mMovieOverview = in.readString();
        mMovieRating = in.readDouble();
        mMovieImagePath = in.readString();
        mMovieId = in.readInt();
    }

    public static final Creator<MovieParcelable> CREATOR = new Creator<MovieParcelable>() {
        @Override
        public MovieParcelable createFromParcel(Parcel in) {
            return new MovieParcelable(in);
        }

        @Override
        public MovieParcelable[] newArray(int size) {
            return new MovieParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mMovieTitle);
        out.writeString(mMovieReleaseDate);
        out.writeString(mMovieOverview);
        out.writeDouble(mMovieRating);
        out.writeString(mMovieImagePath);
        out.writeInt(mMovieId);
    }
}
