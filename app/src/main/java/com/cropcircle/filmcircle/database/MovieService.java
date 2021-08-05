package com.cropcircle.filmcircle.database;

import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.models.action.AccountStates;
import com.cropcircle.filmcircle.models.action.ActionResponse;
import com.cropcircle.filmcircle.models.allmedia.MediaResponse;
import com.cropcircle.filmcircle.models.auth.RequestToken;
import com.cropcircle.filmcircle.models.auth.SessionId;
import com.cropcircle.filmcircle.models.movie.MovieDetails;
import com.cropcircle.filmcircle.models.movie.Images;
import com.cropcircle.filmcircle.models.movie.MovieVideos;
import com.cropcircle.filmcircle.models.movie.Movies;
import com.cropcircle.filmcircle.models.review.Reviews;
import com.cropcircle.filmcircle.models.tv.MediaTV;
import com.cropcircle.filmcircle.models.tv.ResultTV;
import com.cropcircle.filmcircle.models.user.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MovieService {
    @GET("discover/movie" + Constants.API_KEY)
    Call<Movies> discoverMovies(@QueryMap Map<String, String> query);

    @GET("search/movie" + Constants.API_KEY)
    Call<Movies> searchMovies(@QueryMap Map<String, String> query);

    @GET("trending/{media_type}/{time_window}" + Constants.API_KEY)
    Call<MediaResponse> trending(@Path("media_type") String mediaType,
                                 @Path("time_window") String time);

    @GET("movie/{id}" + Constants.API_KEY)
    Call<MovieDetails> movieDetails(@Path("id") Integer movieId);

    @GET("movie/popular" + Constants.API_KEY)
    Call<Movies> popularMovie();

    @GET("movie/now_playing" + Constants.API_KEY)
    Call<Movies> nowPlaying();

    @GET("movie/upcoming" + Constants.API_KEY)
    Call<Movies> upcomingMovies();

    @GET("movie/top_rated" + Constants.API_KEY)
    Call<Movies> topRatedMovie();

    @GET("movie/{id}/recommendations" + Constants.API_KEY)
    Call<Movies> movieRecommendation(@Path("id") Integer movieId, @QueryMap Map<String, String> query);

    @GET("movie/{id}/similar" + Constants.API_KEY)
    Call<Movies> similarMovie(@Path("id") Integer movieId, @QueryMap Map<String, String> query);

    @GET("movie/{id}/images" + Constants.API_KEY)
    Call<Images> movieImages(@Path("id") Integer movieId);

    @GET("movie/{id}/videos" + Constants.API_KEY)
    Call<MovieVideos> movieVideos(@Path("id") Integer movieId, @QueryMap Map<String, String> query);

    @GET("authentication/token/new" + Constants.API_KEY)
    Call<RequestToken> createRequestToken();

    @FormUrlEncoded
    @POST("authentication/token/validate_with_login" + Constants.API_KEY)
    Call<RequestToken> validateTokenWithLogin(@Field("username") String username,
                                              @Field("password") String password,
                                              @Field("request_token") String requestToken);

    @FormUrlEncoded
    @POST("authentication/session/new" + Constants.API_KEY)
    Call<SessionId> createSession(@Field("request_token") String requestToken);

    @GET("account" + Constants.API_KEY)
    Call<User> getUserDetails(@Query("session_id") String sessionId);

    @GET("account/{id}/favorite/movies" + Constants.API_KEY)
    Call<Movies> getFavoriteMovies(@Path("id") Integer userId, @Query("session_id") String sessionId, @Query("sort_by") String sortBy);

    @GET("account/{id}/watchlist/movies" + Constants.API_KEY)
    Call<Movies> getMovieWatchlist(@Path("id") Integer userId, @Query("session_id") String sessionId, @Query("sort_by") String sortBy);

    @FormUrlEncoded
    @POST("account/{userid}/favorite" + Constants.API_KEY)
    Call<ActionResponse> addToFavorites(
            @Path("userid") Integer userId,
            @Query("session_id") String sessionId,
            @Field("media_type") String mediaType,
            @Field("media_id") Integer mediaId,
            @Field("favorite") boolean favorite);

    @GET("movie/{movieid}/account_states" + Constants.API_KEY)
    Call<AccountStates> getAccountStates(@Path("movieid") Integer movieId,
                                         @Query("session_id") String sessionId);
}
