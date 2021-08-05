package com.cropcircle.filmcircle.ui.discover;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.cropcircle.filmcircle.database.MediaTVRepository;
import com.cropcircle.filmcircle.database.MovieRepository;
import com.cropcircle.filmcircle.models.movie.Movie;
import com.cropcircle.filmcircle.models.tv.MediaTV;

import java.util.List;
import java.util.Map;

public class MovieDiscoverViewModel extends ViewModel {
    private MovieRepository movieRepository;
    private MediaTVRepository tvRepository;
    private MutableLiveData<Map<String, String>> mapMutableLiveData = new MutableLiveData<>();

    public MovieDiscoverViewModel() {
        movieRepository = new MovieRepository();
        tvRepository = new MediaTVRepository();
    }

    public void setQueries(Map<String, String> queries) {
        mapMutableLiveData.setValue(queries);
    }

    public LiveData<List<Movie>> searchMovies(int page, String query){
        return movieRepository.searchMovies(query, page);
    }

    public LiveData<List<Movie>> discoverMovies(int page){
        return Transformations.switchMap(mapMutableLiveData, map -> movieRepository.discoverMovies(map, page));
    }

    public LiveData<List<MediaTV>> discoverTVSeries(){
        return Transformations.switchMap(mapMutableLiveData, map -> tvRepository.discover(map));
    }
}