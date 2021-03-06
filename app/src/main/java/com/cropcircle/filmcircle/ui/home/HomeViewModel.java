package com.cropcircle.filmcircle.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.cropcircle.filmcircle.database.MovieRepository;
import com.cropcircle.filmcircle.models.allmedia.Result;
import com.cropcircle.filmcircle.models.movie.Movie;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<Integer> genreId = new MutableLiveData<>();
    private MovieRepository repository;

    public HomeViewModel() {
        //mContext = application.getApplicationContext();
        repository = new MovieRepository();
    }

    public LiveData<List<Result>> trendingMovies() {
        return repository.trendingMovie();
    }

    public LiveData<List<Movie>> nowPlaying(){
        return repository.nowPlaying();
    }

    public LiveData<List<Result>> trendingTVs() {
        return repository.trendingTv();
    }

    public LiveData<List<Result>> banners(){
        return repository.weeklyTrendingAllMedia();
    }

    public LiveData<List<Movie>> getNewRelease() {
        return repository.discoverNewRelease();
    }

    public LiveData<List<Movie>> topRatedMovies() {
        return repository.topRated();
    }

    public LiveData<List<Movie>> getPopularMovies() {
        return repository.discoverPopular();
    }

    public LiveData<List<Movie>> getGenreList() {
        return Transformations.switchMap(genreId, id-> repository.getByGenre(id));
    }

    public void setGenreId(int genreId) {
        this.genreId.setValue(genreId);
    }
}