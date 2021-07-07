package com.cropcircle.filmcircle.models.tv;

import com.cropcircle.filmcircle.models.movie.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultTV {
    @SerializedName("results")
    @Expose
    private List<MediaTV> results = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public ResultTV() {
    }

    /**
     *
     * @param results
     */
    public ResultTV(List<MediaTV> results) {
        super();
        this.results = results;
    }

    public List<MediaTV> getResults() {
        return results;
    }

    public void setResults(List<MediaTV> results) {
        this.results = results;
    }

}
