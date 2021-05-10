package com.cropcircle.filmcircle.models.movie;

import com.cropcircle.filmcircle.models.movie.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movies {
    @SerializedName("results")
    @Expose
    private List<Movie> results = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Movies() {
    }

    /**
     *
     * @param results
     */
    public Movies(List<Movie> results) {
        super();
        this.results = results;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

}
