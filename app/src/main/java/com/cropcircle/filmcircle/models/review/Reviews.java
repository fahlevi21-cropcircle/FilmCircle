package com.cropcircle.filmcircle.models.review;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reviews {
    @SerializedName("results")
    @Expose
    private List<Review> results = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Reviews() {
    }

    /**
     *
     * @param results
     */
    public Reviews(List<Review> results) {
        super();
        this.results = results;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
