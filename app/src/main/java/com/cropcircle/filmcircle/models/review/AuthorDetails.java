package com.cropcircle.filmcircle.models.review;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthorDetails {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("avatar_path")
    @Expose
    private String avatarPath;
    @SerializedName("rating")
    @Expose
    private Double rating;

    /**
     * No args constructor for use in serialization
     *
     */
    public AuthorDetails() {
    }

    /**
     *
     * @param name
     * @param avatarPath
     * @param rating
     * @param username
     */
    public AuthorDetails(String name, String username, String avatarPath, Double rating) {
        super();
        this.name = name;
        this.username = username;
        this.avatarPath = avatarPath;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
