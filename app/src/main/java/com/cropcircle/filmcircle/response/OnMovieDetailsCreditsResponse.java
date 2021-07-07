package com.cropcircle.filmcircle.response;

import com.cropcircle.filmcircle.models.movie.MovieCredits;

public interface OnMovieDetailsCreditsResponse {
    void OnResponse(MovieCredits movieCredits);
    void OnError(String e);
}
