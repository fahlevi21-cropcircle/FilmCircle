package com.cropcircle.filmcircle.ui.home.sub;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.cropcircle.filmcircle.database.MediaTVRepository;
import com.cropcircle.filmcircle.models.movie.Backdrop;
import com.cropcircle.filmcircle.models.movie.Images;
import com.cropcircle.filmcircle.models.movie.MovieCredits;
import com.cropcircle.filmcircle.models.movie.MovieVideos;
import com.cropcircle.filmcircle.models.movie.Video;
import com.cropcircle.filmcircle.models.review.Review;
import com.cropcircle.filmcircle.models.review.Reviews;
import com.cropcircle.filmcircle.models.tv.MediaTV;

import java.util.List;

public class MediaTVDetailsViewModel extends ViewModel {
    private MediaTVRepository repository;
    private MutableLiveData<Integer> tvId = new MutableLiveData<>();

    public MediaTVDetailsViewModel() {
        repository = new MediaTVRepository();
    }

    public void setTvId(Integer id) {
        tvId.setValue(id);
    }

    public LiveData<MediaTV> getDetails(){
        return Transformations.switchMap(tvId, id -> repository.getDetails(id));
    }

    public LiveData<Images> getImages() {
        return Transformations.switchMap(tvId, id -> repository.getImages(id));
    }

    public LiveData<MovieVideos> getVideos() {
        return Transformations.switchMap(tvId, id -> repository.getVideos(id));
    }

    public LiveData<Reviews> getReview() {
        return Transformations.switchMap(tvId, id -> repository.getReviews(id));
    }

    public LiveData<MovieCredits> getCredits(){
        return Transformations.switchMap(tvId, id -> repository.getCredits(id));
    }
}
