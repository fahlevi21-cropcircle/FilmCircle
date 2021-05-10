package com.cropcircle.filmcircle.models.user;

public interface OnUserDetailsResponse {
    void onResponse(User user);
    void onError(String e);
}
