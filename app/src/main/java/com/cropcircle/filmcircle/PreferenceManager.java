package com.cropcircle.filmcircle;

import android.content.Context;
import android.content.SharedPreferences;

import com.cropcircle.filmcircle.models.user.User;
import com.google.gson.Gson;

public class PreferenceManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_SESSION_KEY = "session";
    private static final String userdata_key = "user_data";
    private static final String PREF_IS_LOGGED_IN_KEY = "is_logged_in";

    public PreferenceManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences("MainPreference", 0);
        this.editor = sharedPreferences.edit();
    }

    public void setSessionId(String sessionId) {
        editor.putString(PREF_SESSION_KEY, sessionId);
        editor.apply();
        editor.commit();
    }

    public String getSessionId() {
        return sharedPreferences.getString(PREF_SESSION_KEY, "");
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(PREF_IS_LOGGED_IN_KEY, isLoggedIn);
        editor.apply();
        editor.commit();
    }

    public boolean getLoggedIn() {
        return sharedPreferences.getBoolean(PREF_IS_LOGGED_IN_KEY, false);
    }

    public void setUserdata(User user){
        String json = new Gson().toJson(user);
        editor.putString(userdata_key, json);
        editor.apply();
        editor.commit();
    }

    public User getUserdata(){
        String json = sharedPreferences.getString(userdata_key, "");
        return new Gson().fromJson(json, User.class);
    }

    public void destroyPreferences() {
        editor.clear();
        editor.apply();
        editor.commit();
    }
}
