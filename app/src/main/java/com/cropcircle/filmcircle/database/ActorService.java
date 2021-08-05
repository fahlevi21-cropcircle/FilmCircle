package com.cropcircle.filmcircle.database;

import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.models.people.ActorsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ActorService {

    @GET("person/popular" + Constants.API_KEY)
    Call<ActorsResponse> getPopularActors(@QueryMap Map<String, String> queries);

    @GET("trending/person/day" + Constants.API_KEY)
    Call<ActorsResponse> getLatestActors();

    @GET("search/person/" + Constants.API_KEY)
    Call<ActorsResponse> searchActors(@Query("query") String query, @Query("page") int page);
}
