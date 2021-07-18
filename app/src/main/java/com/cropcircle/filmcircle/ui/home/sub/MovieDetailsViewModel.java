package com.cropcircle.filmcircle.ui.home.sub;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.cropcircle.filmcircle.database.MovieDetailsRepository;
import com.cropcircle.filmcircle.database.MovieRepository;
import com.cropcircle.filmcircle.models.action.OnAccountStatesResponse;
import com.cropcircle.filmcircle.models.action.OnActionResponse;
import com.cropcircle.filmcircle.models.movie.Backdrop;
import com.cropcircle.filmcircle.models.movie.Movie;
import com.cropcircle.filmcircle.models.movie.MovieDetails;
import com.cropcircle.filmcircle.models.movie.Video;
import com.cropcircle.filmcircle.models.people.Actors;
import com.cropcircle.filmcircle.models.review.Review;

import java.util.List;

public class MovieDetailsViewModel extends ViewModel {
    private MovieRepository repository;
    private MovieDetailsRepository movieDetailsRepository;
    private MutableLiveData<Integer> movieIdLiveData = new MutableLiveData<>();

    public MovieDetailsViewModel() {
        repository = new MovieRepository();
        movieDetailsRepository = new MovieDetailsRepository();
    }

    public LiveData<MovieDetails> getDetails() {
        return Transformations.switchMap(movieIdLiveData, id -> repository.movieDetails(id));
    }

    public LiveData<List<Backdrop>> getImages() {
        return Transformations.switchMap(movieIdLiveData, id -> repository.getImages(id));
    }

    public LiveData<List<Video>> getVideos() {
        return Transformations.switchMap(movieIdLiveData, id -> repository.getVideos(id));
    }

    public LiveData<List<Movie>> getSimilar() {
        return Transformations.switchMap(movieIdLiveData, id -> repository.getSimilarMovies(id));
    }

    public LiveData<List<Movie>> getRecommended() {
        return Transformations.switchMap(movieIdLiveData, id -> repository.getRecommendedMovies(id));
    }

    public LiveData<List<Review>> getReview() {
        return Transformations.switchMap(movieIdLiveData, id -> movieDetailsRepository.getReviews(id));
    }

    public void setMovieId(int id) {
        movieIdLiveData.setValue(id);
    }

    public void addToFavorites(String sessionId, Integer mediaId,
                               @Nullable Integer userId, OnActionResponse onActionResponse){
        repository.addToFavorites(sessionId,"movie", mediaId, true, userId, onActionResponse);
    }

    @Deprecated
    public void removeFromFavorites(String sessionId, Integer mediaId,
                               @Nullable Integer userId, OnActionResponse onActionResponse){
        repository.addToFavorites(sessionId,"movie", mediaId, false, userId, onActionResponse);
    }

    public void getMovieStates(int movieId, String sessionId, OnAccountStatesResponse onAccountStatesResponse){
        repository.getAccountStates(movieId, sessionId, onAccountStatesResponse);
    }

    public LiveData<List<Actors>> getCasters(){
        return Transformations.switchMap(movieIdLiveData, id -> movieDetailsRepository.getMovieCasters(id));
    }
}
