package com.cropcircle.filmcircle;

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

    public static String[] genres = {
            "Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary",
            "Drama", "Family", "Fantasy", "History", "Horror", "Music",
            "Mystery", "Romance", "Science Fiction", "TV Movie", "Thriller", "War",
            "Western"
    };

    public static int[] genreIds = {
            28, 12, 16, 35, 80, 99,
            18, 10751, 14, 36, 27, 10402,
            9648, 10749, 878, 10770, 53, 10752,
            37
    };


    /*used for fastadapter livedata DiffUtil.DiffResult diffResult = FastAdapterDiffUtil.calculateDiff(newReleaseAdapter, movies);
                FastAdapterDiffUtil.set(newReleaseAdapter, diffResult);*/
}
