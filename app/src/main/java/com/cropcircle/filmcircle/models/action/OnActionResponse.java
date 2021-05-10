package com.cropcircle.filmcircle.models.action;

public interface OnActionResponse {
    void OnResponse(ActionResponse response);
    void OnError(String e);
}
