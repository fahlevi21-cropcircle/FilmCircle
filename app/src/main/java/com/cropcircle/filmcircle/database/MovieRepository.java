package com.cropcircle.filmcircle.database;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cropcircle.filmcircle.models.action.AccountStates;
import com.cropcircle.filmcircle.models.action.ActionResponse;
import com.cropcircle.filmcircle.models.action.OnAccountStatesResponse;
import com.cropcircle.filmcircle.models.action.OnActionResponse;
import com.cropcircle.filmcircle.models.allmedia.MediaResponse;
import com.cropcircle.filmcircle.models.allmedia.Result;
import com.cropcircle.filmcircle.models.auth.OnRequestTokenResponse;
import com.cropcircle.filmcircle.models.auth.RequestToken;
import com.cropcircle.filmcircle.models.auth.SessionId;
import com.cropcircle.filmcircle.models.movie.Backdrop;
import com.cropcircle.filmcircle.models.movie.Images;
import com.cropcircle.filmcircle.models.movie.Movie;
import com.cropcircle.filmcircle.models.movie.MovieDetails;
import com.cropcircle.filmcircle.models.movie.MovieVideos;
import com.cropcircle.filmcircle.models.movie.Movies;
import com.cropcircle.filmcircle.models.movie.Video;
import com.cropcircle.filmcircle.models.tv.MediaTV;
import com.cropcircle.filmcircle.models.tv.ResultTV;
import com.cropcircle.filmcircle.models.user.OnUserDetailsResponse;
import com.cropcircle.filmcircle.models.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private final MovieService service;

    public MovieRepository() {
        AppNetwork network = AppNetwork.getInstance();
        service = network.createService().create(MovieService.class);
    }

    public LiveData<List<Movie>> discoverNewRelease() {
        final String TAG = "New Release Movies";
        Call<Movies> call = service.upcomingMovies();

        MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body().getResults());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Movie>> discoverPopular() {
        final String TAG = "Discover Movies";
        Call<Movies> call = service.popularMovie();


        MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body().getResults());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Movie>> getByGenre(int genreId, int page) {
        final String TAG = "Genre Movies";
        Map<String, String> map = new HashMap<>();
        map.put("sort_by", "popularity.desc");
        map.put("page", String.valueOf(page));
        map.put("with_genres", String.valueOf(genreId));
        Call<Movies> call = service.discoverMovies(map);
        MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body().getResults());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Result>> dailyTrendingAllMedia() {
        final String TAG = "Trending All";
        Call<MediaResponse> call = service.trending("all", "day");

        MutableLiveData<List<Result>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<MediaResponse>() {
            @Override
            public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body().getResults());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MediaResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Movie>> nowPlaying() {
        final String TAG = "Now Playing";
        Call<Movies> call = service.nowPlaying();

        MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body().getResults());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Movie>> topRated() {
        final String TAG = "Top Rated";
        Call<Movies> call = service.topRatedMovie();

        MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body().getResults());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Result>> trendingTv() {
        final String TAG = "Trending";
        Call<MediaResponse> call = service.trending("tv", "week");

        MutableLiveData<List<Result>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<MediaResponse>() {
            @Override
            public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body().getResults());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MediaResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Result>> trendingMovie() {
        final String TAG = "Trending";
        Call<MediaResponse> call = service.trending("movie", "week");

        MutableLiveData<List<Result>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<MediaResponse>() {
            @Override
            public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body().getResults());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MediaResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<MovieDetails> movieDetails(int movieId) {
        final String TAG = "Movie Detail";
        MutableLiveData<MovieDetails> liveData = new MutableLiveData<>();
        Call<MovieDetails> call = service.movieDetails(movieId);
        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    public LiveData<List<Backdrop>> getImages(int movieId) {
        final String TAG = "Images Movies";
        Map<String, String> map = new HashMap<>();
        map.put("language", "en");
        Call<Images> call = service.movieImages(movieId);
        MutableLiveData<List<Backdrop>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<Images>() {
            @Override
            public void onResponse(Call<Images> call, Response<Images> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body().getBackdrops());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Images> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Video>> getVideos(int movieId) {
        final String TAG = "Videos Movies";
        Map<String, String> map = new HashMap<>();
        map.put("language", "en");
        Call<MovieVideos> call = service.movieVideos(movieId, map);
        MutableLiveData<List<Video>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<MovieVideos>() {
            @Override
            public void onResponse(Call<MovieVideos> call, Response<MovieVideos> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body().getResults());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieVideos> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Movie>> getSimilarMovies(int movieId) {
        final String TAG = "Similar Movies";
        Map<String, String> map = new HashMap<>();
        map.put("language", "en");
        Call<Movies> call = service.similarMovie(movieId, map);
        MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body().getResults());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Movie>> getRecommendedMovies(int movieId) {
        final String TAG = "Similar Movies";
        Map<String, String> map = new HashMap<>();
        map.put("language", "en");
        Call<Movies> call = service.movieRecommendation(movieId, map);
        MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body().getResults());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Movie>> getFavoriteMovies(int userId, String sessionId){
        final String TAG = "favorite movies";
        Call<Movies> call = service.getFavoriteMovies(userId, sessionId, "created_at.desc");
        MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()){
                    mutableLiveData.postValue(response.body().getResults());
                }else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<List<Movie>> getMovieWatchlist(int userId, String sessionId){
        final String TAG = "watchlist movies";
        Call<Movies> call = service.getMovieWatchlist(userId, sessionId, "created_at.desc");
        MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();

        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()){
                    mutableLiveData.postValue(response.body().getResults());
                }else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public void createRequestToken(OnRequestTokenResponse onRequestTokenResponse) {
        String TAG = "create token";
        Call<RequestToken> call = service.createRequestToken();
        call.enqueue(new Callback<RequestToken>() {
            @Override
            public void onResponse(Call<RequestToken> call, Response<RequestToken> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    onRequestTokenResponse.onResponse(response.body().getRequestToken());
                } else {
                }
            }

            @Override
            public void onFailure(Call<RequestToken> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                onRequestTokenResponse.onError(t.getLocalizedMessage());
            }
        });
    }

    public void validateRequestToken(String username, String password, String requestToken, OnRequestTokenResponse onRequestTokenResponse) {
        String TAG = "validate token";
        Call<RequestToken> call = service.validateTokenWithLogin(username, password, requestToken);
        call.enqueue(new Callback<RequestToken>() {
            @Override
            public void onResponse(Call<RequestToken> call, Response<RequestToken> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    onRequestTokenResponse.onResponse(response.body().getRequestToken());
                } else {
                    onRequestTokenResponse.onResponse(response.message());
                }
            }

            @Override
            public void onFailure(Call<RequestToken> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                onRequestTokenResponse.onError(t.getLocalizedMessage());
            }
        });
    }

    public LiveData<SessionId> getSessionId(String requestToken) {
        String TAG = "get session";
        MutableLiveData<SessionId> mutableLiveData = new MutableLiveData<>();
        Call<SessionId> call = service.createSession(requestToken);
        call.enqueue(new Callback<SessionId>() {
            @Override
            public void onResponse(Call<SessionId> call, Response<SessionId> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<SessionId> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public void getUserDetails(String sessionId, OnUserDetailsResponse onUserDetailsResponse) {
        final String TAG = "get user details";
        Call<User> call = service.getUserDetails(sessionId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    onUserDetailsResponse.onResponse(response.body());
                } else {
                    onUserDetailsResponse.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                onUserDetailsResponse.onError(t.getLocalizedMessage());
            }
        });
    }

    public void addToFavorites(String sessionId, String mediaType,
                               Integer mediaId, boolean favorite, @Nullable Integer userId, OnActionResponse onActionResponse){
        final String TAG = "add to favorite";
        Call<ActionResponse> call = service.addToFavorites(userId ,sessionId, mediaType, mediaId, favorite);
        call.enqueue(new Callback<ActionResponse>() {
            @Override
            public void onResponse(Call<ActionResponse> call, Response<ActionResponse> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()){
                    onActionResponse.OnResponse(response.body());
                }else {
                    onActionResponse.OnResponse(null);
                }
            }

            @Override
            public void onFailure(Call<ActionResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                onActionResponse.OnError(t.getLocalizedMessage());
            }
        });
    }

    public void getAccountStates(Integer movieId, String sessionId, OnAccountStatesResponse onAccountStatesResponse){
        final String TAG = "account state";
        Call<AccountStates> call = service.getAccountStates(movieId, sessionId);
        call.enqueue(new Callback<AccountStates>() {
            @Override
            public void onResponse(Call<AccountStates> call, Response<AccountStates> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()){
                    onAccountStatesResponse.OnResponse(response.body());
                }else {
                    onAccountStatesResponse.OnError(response.message());
                }
            }

            @Override
            public void onFailure(Call<AccountStates> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                onAccountStatesResponse.OnError(t.getLocalizedMessage());
            }
        });
    }
}
