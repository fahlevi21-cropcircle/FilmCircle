package com.cropcircle.filmcircle;

import com.cropcircle.filmcircle.models.movie.Genre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_KEY = "?api_key=2c462f471b08742ae8128403a4dc6133";
    public static final String IMG_PATH_180 = "https://image.tmdb.org/t/p/w185";
    public static final String IMG_PATH_500 = "https://image.tmdb.org/t/p/w500";
    public static final String BACKDROP_PATH_780 = "https://image.tmdb.org/t/p/w780";
    public static final String BACKDROP_PATH_1280 = "https://image.tmdb.org/t/p/w1280";
    public static final String IMG_PROFILE_180 = "https://image.tmdb.org/t/p/w185";

    public static final String VIDEO_PATH = "https://www.youtube.com/watch?v=";
    public static final String VIDEO_THUMB = "https://img.youtube.com/vi/";
    public static final String THUMB_DEFAULT_RESOLUTION = "/maxresdefault.jpg";

    public static final String MOVIE_ID_KEY = "com.cropcircle.filmcircle.idKey";
    public static final String VIDEO_ID_KEY = "com.cropcircle.filmcircle.videoKey";

    public static List<Genre> movieGenres = new ArrayList<Genre>(
            Arrays.asList(
                            new Genre(28, "Action"),
                            new Genre(12, "Adventure"),
                            new Genre(16, "Animation"),
                            new Genre(35, "Comedy"),
                            new Genre(80, "Crime"),
                            new Genre(99, "Documentary"),
                            new Genre(18, "Drama"),
                            new Genre(10751, "Family"),
                            new Genre(14, "Fantasy"),
                            new Genre(36, "History"),
                            new Genre(27, "Horror"),
                            new Genre(10402, "Music"),
                            new Genre(9648, "Mystery"),
                            new Genre(10749, "Romance"),
                            new Genre(878, "Science Fiction"),
                            new Genre(10770, "TV Movie"),
                            new Genre(53, "Thriller"),
                            new Genre(10752, "War"),
                            new Genre(37, "Western")
                )
    );

    public static List<Genre> tvGenres = new ArrayList<Genre>(
            Arrays.asList(
                    new Genre(10759, "Action & Adventure"),
                    new Genre(16, "Animation"),
                    new Genre(35, "Comedy"),
                    new Genre(80, "Crime"),
                    new Genre(99, "Documentary"),
                    new Genre(18, "Drama"),
                    new Genre(10751, "Family"),
                    new Genre(10762, "Kids"),
                    new Genre(9648, "Mystery"),
                    new Genre(10763, "News"),
                    new Genre(10764, "Reality"),
                    new Genre(10765, "Sci-Fi & Fantasy"),
                    new Genre(10766, "Soap"),
                    new Genre(10767, "Talk"),
                    new Genre(10768, "War & Politics"),
                    new Genre(37, "Western")
            )
    );


    /*used for fastadapter livedata DiffUtil.DiffResult diffResult = FastAdapterDiffUtil.calculateDiff(newReleaseAdapter, movies);
                FastAdapterDiffUtil.set(newReleaseAdapter, diffResult);*/
}
