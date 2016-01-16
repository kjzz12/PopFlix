package com.example.kjzz1.popflix;

import android.util.Log;

//Is this the best way to handle this?

public class MovieDataTool {
    private String image;
    private String title;
    private String releaseDate;
    private String plotSummary;
    private String userRating;
    private String id;


    public MovieDataTool() {
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
        Log.v("MovieDataTool", image);
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
}