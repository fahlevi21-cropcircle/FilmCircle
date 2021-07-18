package com.cropcircle.filmcircle.database;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cropcircle.filmcircle.models.movie.MovieCredits;
import com.cropcircle.filmcircle.models.people.Actors;
import com.cropcircle.filmcircle.models.review.Review;
import com.cropcircle.filmcircle.models.review.Reviews;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsRepository {
    static MovieDetailService service;

    public MovieDetailsRepository() {
        AppNetwork network = AppNetwork.getInstance();
        service = network.createService().create(MovieDetailService.class);
    }

    public LiveData<List<Actors>> getMovieCasters(int movieId){
        final String TAG = "movie credits";
        Call<MovieCredits> call = service.getMovieCredits(movieId);
        MutableLiveData<List<Actors>> mutableLiveData = new MutableLiveData<>();
        call.enqueue(new Callback<MovieCredits>() {
            @Override
            public void onResponse(Call<MovieCredits> call, Response<MovieCredits> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful() && response.body().getCast() != null){
                    mutableLiveData.postValue(response.body().getCast());
                }else {
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieCredits> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.postValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Review>> getReviews(int movieId) {
        final String TAG = "Review Movie";
        Call<Reviews> call = service.movieReviews(movieId);
        MutableLiveData<List<Review>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body().getResults());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }
}
