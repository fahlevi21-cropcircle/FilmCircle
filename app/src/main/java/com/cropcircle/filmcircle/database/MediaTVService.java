package com.cropcircle.filmcircle.database;

import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.models.movie.Images;
import com.cropcircle.filmcircle.models.movie.MovieCredits;
import com.cropcircle.filmcircle.models.movie.MovieVideos;
import com.cropcircle.filmcircle.models.movie.Movies;
import com.cropcircle.filmcircle.models.review.Reviews;
import com.cropcircle.filmcircle.models.tv.MediaTV;
import com.cropcircle.filmcircle.models.tv.ResultTV;
import com.google.android.exoplayer2.C;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MediaTVService {

    @GET("discover/tv" + Constants.API_KEY)
    Call<ResultTV> discover(@QueryMap Map<String, String> queryMap);

    @GET("tv/top_rated" + Constants.API_KEY)
    Call<ResultTV> topRated();

    @GET("tv/popular" + Constants.API_KEY)
    Call<ResultTV> popularTV();

    @GET("tv/{id}" + Constants.API_KEY)
    Call<MediaTV> getDetails(@Path("id") Integer id);

    @GET("tv/{id}/credits" + Constants.API_KEY)
    Call<MovieCredits> getCredits(@Path("id") Integer id);

    @GET("tv/{id}/reviews" + Constants.API_KEY)
    Call<Reviews> getReviews(@Path("id") Integer id);

    @GET("tv/{id}/images" + Constants.API_KEY)
    Call<Images> getImages(@Path("id") Integer id);

    @GET("tv/{id}/videos" + Constants.API_KEY)
    Call<MovieVideos> getVideos(@Path("id") Integer id);

    @GET("account/{id}/favorite/tv" + Constants.API_KEY)
    Call<ResultTV> getFavoriteTV(@Path("id") Integer userId, @Query("session_id") String sessionId);
}
