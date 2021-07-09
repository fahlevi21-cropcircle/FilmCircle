package com.cropcircle.filmcircle.ui.collection.movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cropcircle.filmcircle.database.MovieRepository;
import com.cropcircle.filmcircle.models.movie.Movie;

import java.util.List;

public class CollectionsViewModel extends ViewModel {
    private MovieRepository repository;

    public CollectionsViewModel() {
        repository = new MovieRepository();
    }

    public LiveData<List<Movie>> getFavoriteMovieList(int userId, String sessionId) {
        return repository.getFavoriteMovies(userId, sessionId);
    }

    public LiveData<List<Movie>> getMovieWatchlist(int userId, String sessionId) {
        return repository.getMovieWatchlist(userId, sessionId);
    }
}