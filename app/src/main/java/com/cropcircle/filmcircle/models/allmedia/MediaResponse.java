package com.cropcircle.filmcircle.models.allmedia;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MediaResponse {
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<AllMedia> allMedia = null;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    /**
     * No args constructor for use in serialization
     *
     */
    public MediaResponse() {
    }

    /**
     *
     * @param totalResults
     * @param totalPages
     * @param page
     * @param allMedia
     */
    public MediaResponse(Integer page, List<AllMedia> allMedia, Integer totalPages, Integer totalResults) {
        super();
        this.page = page;
        this.allMedia = allMedia;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<AllMedia> getResults() {
        return allMedia;
    }

    public void setResults(List<AllMedia> allMedia) {
        this.allMedia = allMedia;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
