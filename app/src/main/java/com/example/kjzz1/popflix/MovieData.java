package com.example.kjzz1.popflix;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

//Is this the best way to handle this?

public class MovieData implements Parcelable {
    private String image;
    private String title;
    private String releaseDate;
    private String plotSummary;
    private String userRating;
    private String id;


    public MovieData() {
        super();

    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getRelaseDate() {
        return releaseDate;
    }

    public String getPlotSummary() {
        return plotSummary;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getId() {
        return id;
    }

    public void setImage(String image) {
        this.image = image;
        Log.v("MovieData", image);
    }

    public void setMovieTitle (String movieTitle) {
        this.title = movieTitle;
    }

    public void setReleaseDate (String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPlotSummary (String plotSummary) {
        this.plotSummary = plotSummary;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public void setId(String id) {
        this.id = id;
    }


    protected MovieData(Parcel in) {
        image = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        plotSummary = in.readString();
        userRating = in.readString();
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(plotSummary);
        dest.writeString(userRating);
        dest.writeString(id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };
}