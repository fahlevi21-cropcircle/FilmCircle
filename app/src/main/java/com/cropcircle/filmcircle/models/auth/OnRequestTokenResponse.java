package com.cropcircle.filmcircle.models.auth;

public interface OnRequestTokenResponse {
    void onResponse(String requestToken);
    void onError(String error);
}
