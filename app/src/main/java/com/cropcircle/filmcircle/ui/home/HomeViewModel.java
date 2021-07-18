package com.cropcircle.filmcircle.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cropcircle.filmcircle.database.MediaTVRepository;
import com.cropcircle.filmcircle.database.MovieRepository;
import com.cropcircle.filmcircle.models.allmedia.AllMedia;
import com.cropcircle.filmcircle.models.movie.Movie;
import com.cropcircle.filmcircle.models.tv.MediaTV;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<Integer> genreId = new MutableLiveData<>();
    private final MovieRepository repository;
    private final MediaTVRepository tvRepository;

    public HomeViewModel() {
        //mContext = application.getApplicationContext();
        repository = new MovieRepository();
        tvRepository = new MediaTVRepository();
    }

    public LiveData<List<AllMedia>> trendingMovies() {
        return repository.trendingMovie();
    }

    public LiveData<List<Movie>> nowPlaying(){
        return repository.nowPlaying();
    }

    public LiveData<List<AllMedia>> trendingTVs() {
        return repository.trendingTv();
    }

    public LiveData<List<AllMedia>> banners(){
        return repository.dailyTrendingAllMedia();
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

    public LiveData<List<MediaTV>> getPopularTV(){
        return tvRepository.popular();
    }

    public LiveData<List<MediaTV>> topRatedTV(){
        return tvRepository.topRated();
    }

    public void setGenreId(int genreId) {
        this.genreId.setValue(genreId);
    }
}