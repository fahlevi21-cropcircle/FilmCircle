package com.cropcircle.filmcircle.models.action;

public interface OnAccountStatesResponse {
    void OnResponse(AccountStates accountStates);
    void OnError(String e);
}
