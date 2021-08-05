package com.cropcircle.filmcircle.ui.actors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.cropcircle.filmcircle.database.ActorsRepository;
import com.cropcircle.filmcircle.models.people.Actors;

import java.util.List;

public class ActorsViewModel extends ViewModel {

    private ActorsRepository repository;
    private MutableLiveData<String> querySearch = new MutableLiveData<>();
    public ActorsViewModel() {
        repository = new ActorsRepository();
    }

    public LiveData<List<Actors>> getPopularActors(int page){
        return repository.getPopularActors(page);
    }

    public LiveData<List<Actors>> getLatestActors(){
        return repository.getLatestActors();
    }

    public LiveData<List<Actors>> searchActors(int page, String query){
        return repository.searchActors(query, page);
    }

    public void setQuerySearch(String query) {
        querySearch.postValue(query);
    }
}