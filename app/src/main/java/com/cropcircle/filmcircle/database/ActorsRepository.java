package com.cropcircle.filmcircle.database;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cropcircle.filmcircle.models.people.Actors;
import com.cropcircle.filmcircle.models.people.ActorsResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActorsRepository {
    ActorService service;

    public ActorsRepository() {
        service = AppNetwork.getInstance().createService().create(ActorService.class);
    }

    public LiveData<List<Actors>> getPopularActors(int page){
        final String TAG = "popular actors";
        Map<String, String> queries = new HashMap<>();
        queries.put("page", String.valueOf(page));
        Call<ActorsResponse> call = service.getPopularActors(queries);
        MutableLiveData<List<Actors>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<ActorsResponse>() {
            @Override
            public void onResponse(Call<ActorsResponse> call, Response<ActorsResponse> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()){
                    mutableLiveData.postValue(response.body().getResults());
                }else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ActorsResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Actors>> getLatestActors(){
        final String TAG = "latest actors";
        Call<ActorsResponse> call = service.getLatestActors();
        MutableLiveData<List<Actors>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<ActorsResponse>() {
            @Override
            public void onResponse(Call<ActorsResponse> call, Response<ActorsResponse> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()){
                    mutableLiveData.postValue(response.body().getResults());
                }else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ActorsResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }
}
