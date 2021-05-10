package com.cropcircle.filmcircle.ui.home.adapter;

public interface VideoCallback {
    void callbackObserver(Object object);
    public interface playerCallback{
        void onItemClick(String id);
    }
}
