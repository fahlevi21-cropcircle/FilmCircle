package com.cropcircle.filmcircle.ui.auth;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.cropcircle.filmcircle.database.MovieRepository;
import com.cropcircle.filmcircle.models.auth.OnRequestTokenResponse;
import com.cropcircle.filmcircle.models.auth.RequestToken;
import com.cropcircle.filmcircle.models.auth.SessionId;
import com.cropcircle.filmcircle.models.movie.Movie;
import com.cropcircle.filmcircle.models.user.OnUserDetailsResponse;

import java.util.List;

public class AuthViewModel extends ViewModel {
    private MovieRepository repository;
    private MutableLiveData<String> requestToken = new MutableLiveData<>();

    public AuthViewModel(){
        repository = new MovieRepository();
    }

    public void createRequestToken(OnRequestTokenResponse onRequestTokenResponse){
        repository.createRequestToken(onRequestTokenResponse);
    }

    private void setRequestToken(String requestToken) {
        this.requestToken.setValue(requestToken);
    }

    public LiveData<List<Movie>> getPopularMovies(){
        return repository.discoverPopular();
    }

    public void validateRequestToken(String username, String password, String requestToken){
        repository.validateRequestToken(username, password, requestToken, new OnRequestTokenResponse() {
            @Override
            public void onResponse(String requestToken) {
                setRequestToken(requestToken);
            }

            @Override
            public void onError(String error) {
                setRequestToken(null);
            }
        });
    }

    public LiveData<SessionId> getSessionId(){
        return Transformations.switchMap(requestToken, token-> repository.getSessionId(token));
    }

    public void getUserDetails(String sessionId, OnUserDetailsResponse onUserDetailsResponse){
        repository.getUserDetails(sessionId, onUserDetailsResponse);
    }
}
