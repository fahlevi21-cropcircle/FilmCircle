package com.cropcircle.filmcircle.models.movie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideos {
    @SerializedName("results")
    @Expose
    private List<Video> results = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public MovieVideos() {
    }

    /**
     *
     * @param results
     */
    public MovieVideos(List<Video> results) {
        super();
        this.results = results;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
