package com.cropcircle.filmcircle.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tmdb {
    @SerializedName("avatar_path")
    @Expose
    private String avatarPath;

    /**
     * No args constructor for use in serialization
     *
     */
    public Tmdb() {
    }

    /**
     *
     * @param avatarPath
     */
    public Tmdb(String avatarPath) {
        super();
        this.avatarPath = avatarPath;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}
