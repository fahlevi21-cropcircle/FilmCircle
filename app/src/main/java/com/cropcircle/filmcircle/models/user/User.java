package com.cropcircle.filmcircle.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("avatar")
    @Expose
    private Avatar avatar;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;
    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("include_adult")
    @Expose
    private Boolean includeAdult;
    @SerializedName("username")
    @Expose
    private String username;

    /**
     * No args constructor for use in serialization
     *
     */
    public User() {
    }

    /**
     *
     * @param name
     * @param iso31661
     * @param avatar
     * @param id
     * @param iso6391
     * @param includeAdult
     * @param username
     */
    public User(Avatar avatar, Integer id, String iso6391, String iso31661, String name, Boolean includeAdult, String username) {
        super();
        this.avatar = avatar;
        this.id = id;
        this.iso6391 = iso6391;
        this.iso31661 = iso31661;
        this.name = name;
        this.includeAdult = includeAdult;
        this.username = username;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIncludeAdult() {
        return includeAdult;
    }

    public void setIncludeAdult(Boolean includeAdult) {
        this.includeAdult = includeAdult;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
