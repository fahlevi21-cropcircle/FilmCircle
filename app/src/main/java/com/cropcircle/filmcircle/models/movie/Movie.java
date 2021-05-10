package com.cropcircle.filmcircle.models.movie;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class Movie extends AbstractItem<Movie, Movie.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.home_rc_new_release;
    }

    @Override
    public long getIdentifier() {
        return id;
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_small_grid;
    }

    protected class ViewHolder extends FastAdapter.ViewHolder<Movie>{
        TextView title,rating;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_movie_title);
            rating = itemView.findViewById(R.id.item_movie_avg);
            image = itemView.findViewById(R.id.item_movie_image);
        }

        @Override
        public void bindView(Movie item, List<Object> payloads) {
            Glide.with(itemView).load(Constants.IMG_PATH_500 + item.backDropPath).into(image);
            title.setText(item.getTitle());
            rating.setText(String.valueOf(item.getVoteAverage()));
        }

        @Override
        public void unbindView(Movie item) {
            image.setImageBitmap(null);
            title.setText(null);
            rating.setText(null);
        }
    }


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
        Glide.with(view).asBitmap().load(Constants.BACKDROP_PATH_780 + image).into(view);
    }

    @BindingAdapter("android:loadSmallGridImage")
    public static void loadSmallGridImage(ImageView view, String image){
        if (image != null && image.toLowerCase().contains("https")){
            Glide.with(view).asBitmap().load(image.substring(1)).override(150,120).into(view);
        }else {
            Glide.with(view).asBitmap().load(Constants.IMG_PATH_500 + image).override(150,120).into(view);
        }
    }
}
