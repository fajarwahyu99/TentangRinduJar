package com.example.infolabsolution.databasemoviejar;


import android.os.Parcel;
import android.os.Parcelable;

public class MovieParcelable implements Parcelable {

    String textTitle;
    String textReleaseDate;
    String textOverview;
    double textRating;
    String imgImagePath;
    int textMovieId;

    public MovieParcelable(String title, String overview, String date,
                           String imagePath, double rating, int id) {
        textTitle = title;
        textReleaseDate = date;
        textOverview = overview;
        textRating = rating;
        imgImagePath = imagePath;
        textMovieId = id;
    }

    protected MovieParcelable(Parcel in) {
        textTitle = in.readString();
        textReleaseDate = in.readString();
        textOverview = in.readString();
        textRating = in.readDouble();
        imgImagePath = in.readString();
        textMovieId = in.readInt();
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
    public void writeToParcel(Parcel texts, int flags) {
        texts.writeString(textTitle);
        texts.writeString(textReleaseDate);
        texts.writeString(textOverview);
        texts.writeDouble(textRating);
        texts.writeString(imgImagePath);
        texts.writeInt(textMovieId);
    }
}
