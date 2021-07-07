package com.cropcircle.filmcircle.models.movie;

import com.cropcircle.filmcircle.models.people.Cast;
import com.cropcircle.filmcircle.models.people.Crew;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieCredits {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cast")
    @Expose
    private List<Cast> cast = null;
    @SerializedName("crew")
    @Expose
    private List<Crew> crew = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public MovieCredits() {
    }

    /**
     *
     * @param cast
     * @param id
     * @param crew
     */
    public MovieCredits(Integer id, List<Cast> cast, List<Crew> crew) {
        super();
        this.id = id;
        this.cast = cast;
        this.crew = crew;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }
}
