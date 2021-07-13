package com.cropcircle.filmcircle.ui.genre;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.cropcircle.filmcircle.database.MovieRepository;
import com.cropcircle.filmcircle.models.movie.Movie;

import java.util.List;

public class GenreViewModel extends ViewModel {

    private MovieRepository repository;
    private MutableLiveData<Integer> genreId = new MutableLiveData<>();

    public GenreViewModel() {
        repository = new MovieRepository();
    }

    public void setGenreId(int genreId) {
        this.genreId.setValue(genreId);
    }

    public LiveData<List<Movie>> getMovies(int page) {
        return Transformations.switchMap(genreId, id -> repository.getByGenre(id, page));
    }
}