package com.cropcircle.filmcircle.database;

import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.models.movie.MovieCredits;
import com.cropcircle.filmcircle.models.review.Reviews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieDetailService {
    @GET("movie/{movie_id}/credits" + Constants.API_KEY)
    Call<MovieCredits> getMovieCredits(@Path("movie_id") Integer movieId);

    @GET("movie/{id}/reviews" + Constants.API_KEY)
    Call<Reviews> movieReviews(@Path("id") Integer movieId);
}


