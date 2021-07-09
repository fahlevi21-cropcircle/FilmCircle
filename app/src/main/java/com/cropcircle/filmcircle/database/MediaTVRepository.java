package com.cropcircle.filmcircle.database;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cropcircle.filmcircle.models.movie.Backdrop;
import com.cropcircle.filmcircle.models.movie.Images;
import com.cropcircle.filmcircle.models.movie.MovieCredits;
import com.cropcircle.filmcircle.models.movie.MovieVideos;
import com.cropcircle.filmcircle.models.review.Reviews;
import com.cropcircle.filmcircle.models.tv.MediaTV;
import com.cropcircle.filmcircle.models.tv.ResultTV;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaTVRepository {
    MediaTVService service;

    public MediaTVRepository() {
        AppNetwork network = AppNetwork.getInstance();
        service = network.createService().create(MediaTVService.class);
    }

    public LiveData<List<MediaTV>> topRated() {
        final String TAG = "topRated TV";
        Call<ResultTV> call = service.topRated();

        MutableLiveData<List<MediaTV>> mutableLiveData = new MutableLiveData<>();
        call.enqueue(new Callback<ResultTV>() {
            @Override
            public void onResponse(Call<ResultTV> call, Response<ResultTV> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()){
                    mutableLiveData.postValue(response.body().getResults());
                }else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResultTV> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });


        return mutableLiveData;
    }

    public LiveData<List<MediaTV>> popular() {
        final String TAG = "Popular TV";
        Call<ResultTV> call = service.popularTV();

        MutableLiveData<List<MediaTV>> mutableLiveData = new MutableLiveData<>();
        call.enqueue(new Callback<ResultTV>() {
            @Override
            public void onResponse(Call<ResultTV> call, Response<ResultTV> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()){
                    mutableLiveData.postValue(response.body().getResults());
                }else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResultTV> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });


        return mutableLiveData;
    }

    public LiveData<MediaTV> getDetails(Integer id){
        String TAG = "TV Details";
        Call<MediaTV> call = service.getDetails(id);
        MutableLiveData<MediaTV> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<MediaTV>() {
            @Override
            public void onResponse(Call<MediaTV> call, Response<MediaTV> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()){
                    mutableLiveData.postValue(response.body());
                }else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MediaTV> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<MovieCredits> getCredits(Integer id){
        String TAG = "TV Credits";
        Call<MovieCredits> call = service.getCredits(id);

        MutableLiveData<MovieCredits> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<MovieCredits>() {
            @Override
            public void onResponse(Call<MovieCredits> call, Response<MovieCredits> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()){
                    mutableLiveData.postValue(response.body());
                }else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieCredits> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<Reviews> getReviews(Integer id){
        String TAG = "TV Reviews";
        Call<Reviews> call = service.getReviews(id);

        MutableLiveData<Reviews> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()){
                    mutableLiveData.postValue(response.body());
                }else {
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

    public LiveData<MovieVideos> getVideos(Integer id){
        String TAG = "TV Videos";
        Call<MovieVideos> call = service.getVideos(id);

        MutableLiveData<MovieVideos> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<MovieVideos>() {
            @Override
            public void onResponse(Call<MovieVideos> call, Response<MovieVideos> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()){
                    mutableLiveData.postValue(response.body());
                }else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieVideos> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<Images> getImages(int movieId) {
        final String TAG = "TV Images";
        /*Map<String, String> map = new HashMap<>();
        map.put("language", "en");*/
        Call<Images> call = service.getImages(movieId);
        MutableLiveData<Images> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<Images>() {
            @Override
            public void onResponse(Call<Images> call, Response<Images> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Images> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<MediaTV>> getFavoriteTv(Integer userId, String sessionId) {
        final String TAG = "favorite TV";
        Call<ResultTV> call = service.getFavoriteTV(userId, sessionId);

        MutableLiveData<List<MediaTV>> mutableLiveData = new MutableLiveData<>();
        call.enqueue(new Callback<ResultTV>() {
            @Override
            public void onResponse(Call<ResultTV> call, Response<ResultTV> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()){
                    mutableLiveData.postValue(response.body().getResults());
                }else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResultTV> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });


        return mutableLiveData;
    }
}
