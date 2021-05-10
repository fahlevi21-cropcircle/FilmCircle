package com.cropcircle.filmcircle.models.auth;

public interface OnSessionResponse {
    void onResponse(String sessionId);
    void onError(String e);
}
