package com.cropcircle.filmcircle.models.action;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountStates {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("favorite")
    @Expose
    private Boolean favorite;
    /*@SerializedName("error_rated")
    @Expose
    private Boolean rated;*/
    @SerializedName("watchlist")
    @Expose
    private Boolean watchlist;

    /**
     * No args constructor for use in serialization
     *
     */
    public AccountStates() {
    }

    /**
     *
     * @param rated is error
     * @param id
     * @param favorite
     * @param watchlist
     */
    public AccountStates(Integer id, Boolean favorite, Boolean watchlist) {
        super();
        this.id = id;
        this.favorite = favorite;
        this.watchlist = watchlist;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Boolean getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(Boolean watchlist) {
        this.watchlist = watchlist;
    }
}
