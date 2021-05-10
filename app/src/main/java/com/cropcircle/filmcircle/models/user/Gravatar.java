package com.cropcircle.filmcircle.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gravatar {
    @SerializedName("hash")
    @Expose
    private String hash;

    /**
     * No args constructor for use in serialization
     *
     */
    public Gravatar() {
    }

    /**
     *
     * @param hash
     */
    public Gravatar(String hash) {
        super();
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
