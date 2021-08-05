package com.cropcircle.filmcircle.models.movie;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie{

    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("backdrop_path")
    @Expose
    private String backDropPath;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;

    public Movie() {
    }

    public Movie(List<Integer> genreIds, Integer id, String originalLanguage,
                 String overview, Double popularity, String posterPath, String backDropPath, String releaseDate,
                 String title, String originalTitle,Double voteAverage, Integer voteCount) {
        this.genreIds = genreIds;
        this.id = id;
        this.originalLanguage = originalLanguage;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.backDropPath = backDropPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.originalTitle = originalTitle;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackDropPath() {
        return backDropPath;
    }

    public void setBackDropPath(String backDropPath) {
        this.backDropPath = backDropPath;
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

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    @BindingAdapter("android:loadMediumImage")
    public static void loadMediumImage(ImageView view, String image){
        Glide.with(view)
                .asDrawable()
                .load(Constants.BACKDROP_PATH_780 + image)
                .placeholder(R.drawable.logo)
                .error(R.drawable.noimg)
                .into(view);
    }

    @BindingAdapter("android:mediumPosterImage")
    public static void mediumPosterImage(ImageView view, String image){
        Glide.with(view)
                .asDrawable()
                .load(Constants.POSTER_PATH_342 + image)
                .placeholder(R.drawable.logo)
                .error(R.drawable.noimg)
                .fitCenter()
                .into(view);
    }

    @BindingAdapter("android:smallBackdropImage")
    public static void smallBackdropImage(ImageView view, String image){
        Glide.with(view)
                .asDrawable()
                .load(Constants.BACKDROP_PATH_300 + image)
                .placeholder(R.drawable.logo)
                .error(R.drawable.noimg)
                .fitCenter()
                .into(view);
    }

    @BindingAdapter("android:profileImage")
    public static void profileImage(ImageView view, String image){
        Glide.with(view)
                .asBitmap()
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_baseline_person)
                .load(Constants.IMG_PROFILE_180 + image)
                .fitCenter()
                .into(view);
    }


    @BindingAdapter("android:loadSmallGridImage")
    public static void loadSmallGridImage(ImageView view, String image){
        if (image != null && image.toLowerCase().contains("https")){
            Glide.with(view).asBitmap().load(image.substring(1)).placeholder(R.drawable.logo).error(R.drawable.noimg).into(view);
        } else {
            Glide.with(view).asBitmap().load(Constants.POSTER_PATH_500 + image).error(R.drawable.noimg).placeholder(R.drawable.logo).into(view);
        }
    }
}
