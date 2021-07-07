package com.cropcircle.filmcircle.models.allmedia;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result implements Comparable<Result> {

    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("original_name")
    @Expose
    private String originalName;
    @SerializedName("origin_country")
    @Expose
    private List<String> originCountry = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("adult")
    @Expose
    private Boolean adult;

    /**
     * No args constructor for use in serialization
     *
     */
    public Result() {
    }

    /**
     *
     * @param overview
     * @param voteAverage
     * @param releaseDate
     * @param mediaType
     * @param video
     * @param genreIds
     * @param originalLanguage
     * @param title
     * @param firstAirDate
     * @param originalName
     * @param originalTitle
     * @param popularity
     * @param originCountry
     * @param name
     * @param id
     * @param backdropPath
     * @param voteCount
     * @param adult
     * @param posterPath
     */
    public Result(String posterPath, String firstAirDate, Double voteAverage, String originalName, List<String> originCountry, Integer id, String name, String backdropPath, Integer voteCount, List<Integer> genreIds, String overview, String originalLanguage, Double popularity, String mediaType, String originalTitle, Boolean video, String releaseDate, String title, Boolean adult) {
        super();
        this.posterPath = posterPath;
        this.firstAirDate = firstAirDate;
        this.voteAverage = voteAverage;
        this.originalName = originalName;
        this.originCountry = originCountry;
        this.id = id;
        this.name = name;
        this.backdropPath = backdropPath;
        this.voteCount = voteCount;
        this.genreIds = genreIds;
        this.overview = overview;
        this.originalLanguage = originalLanguage;
        this.popularity = popularity;
        this.mediaType = mediaType;
        this.originalTitle = originalTitle;
        this.video = video;
        this.releaseDate = releaseDate;
        this.title = title;
        this.adult = adult;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    @Override
    public int compareTo(Result result) {
        return this.popularity.compareTo(result.popularity);
    }
}
