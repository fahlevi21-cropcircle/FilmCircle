package com.cropcircle.filmcircle.ui.home.sub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.PreferenceManager;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ActivityMediaTvDetailsBinding;
import com.cropcircle.filmcircle.models.action.AccountStates;
import com.cropcircle.filmcircle.models.action.OnAccountStatesResponse;
import com.cropcircle.filmcircle.models.movie.Backdrop;
import com.cropcircle.filmcircle.models.movie.Genre;
import com.cropcircle.filmcircle.models.movie.Images;
import com.cropcircle.filmcircle.models.movie.MovieCredits;
import com.cropcircle.filmcircle.models.movie.MovieVideos;
import com.cropcircle.filmcircle.models.movie.Video;
import com.cropcircle.filmcircle.models.people.Cast;
import com.cropcircle.filmcircle.models.review.Review;
import com.cropcircle.filmcircle.models.review.Reviews;
import com.cropcircle.filmcircle.models.tv.MediaTV;
import com.cropcircle.filmcircle.models.tv.Season;
import com.cropcircle.filmcircle.ui.home.adapter.GenreAdapter;
import com.cropcircle.filmcircle.ui.home.adapter.ImageAdapter;
import com.cropcircle.filmcircle.ui.home.adapter.ReviewAdapter;
import com.cropcircle.filmcircle.ui.home.adapter.SeasonTVAdapter;
import com.cropcircle.filmcircle.ui.home.adapter.VideoAdapter;
import com.cropcircle.filmcircle.ui.home.itemdecoration.ActorsItemDecoration;
import com.cropcircle.filmcircle.ui.home.itemdecoration.HorizontalItemDecoration;
import com.cropcircle.filmcircle.ui.home.itemdecoration.VerticalItemDecoration;

import java.util.List;

public class MediaTVDetailsActivity extends AppCompatActivity {
    private ActivityMediaTvDetailsBinding binding;
    private MediaTVDetailsViewModel viewModel;
    private int movieId;
    private PreferenceManager manager;
    private MenuItem menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_media_tv_details);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        setSupportActionBar(binding.tvDetailsToolbar);
        setTitle(null);

        Intent intent = getIntent();
        movieId = intent.getIntExtra(Constants.MOVIE_ID_KEY, 0);

        viewModel = new ViewModelProvider(this).get(MediaTVDetailsViewModel.class);
        manager = new PreferenceManager(this);


        if (isNetworkConnected()){
            observeDetails();
        }

        binding.tvDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie_details, menu);
        return true;
    }

    private void observeDetails(){
        viewModel.setTvId(movieId);
        viewModel.getDetails().observe(this, new Observer<MediaTV>() {
            @Override
            public void onChanged(MediaTV mediaTV) {
                if (mediaTV != null){
                    binding.setData(mediaTV);
                    String bullet = " &bull; ";
                    String tagLine;
                    binding.layoutContent.tvDetailsPopularity.setText(mediaTV.getPopularity().toString());
                    binding.layoutContent.tvDetailsRating.setStar(mediaTV.getVoteAverage().floatValue() - 5.0f);
                    int score = (int) (mediaTV.getVoteAverage() * 10);
                    String scoreText = score + " %";
                    binding.layoutContent.tvDetailsScore.setText(scoreText);
                    if (mediaTV.getTagline() != null){
                        tagLine = mediaTV.getTagline();
                    }else if (mediaTV.getStatus() != null){
                        tagLine = mediaTV.getStatus();
                    }else {
                        tagLine = "Voted by " + mediaTV.getVoteCount() + " users";
                    }
                    //binding.movieDetailsPoster.movieDetailsTagline.setText(Html.fromHtml(tagLine));
                    String releaseYearText = mediaTV.getFirstAirDate().substring(0,4) + bullet;
                    String durationText = bullet + mediaTV.getEpisodeRunTime().get(0).toString() + " Minutes";
                    if (mediaTV.getGenres() != null && mediaTV.getGenres().size() >= 3){
                        getMovieGenres(mediaTV.getGenres().subList(0,3));
                    }else {
                        getMovieGenres(mediaTV.getGenres());
                    }

                    if (mediaTV.getSeasons() != null && mediaTV.getSeasons().size() > 0){
                        getSeasons(mediaTV.getSeasons());
                    }
                }
            }
        });

        getMovieCasters();
        getMovieReviews();
        getMovieImages();
        getMovieVideos();
        //getMovieStates();
    }

    private void getMovieGenres(List<Genre> genreList){
        GenreAdapter adapter = new GenreAdapter();
        adapter.setList(genreList);
        binding.tvDetailsPoster.tvDetailsGenreRc.setAdapter(adapter);
        binding.tvDetailsPoster.tvDetailsGenreRc.setHasFixedSize(true);
        binding.tvDetailsPoster.tvDetailsGenreRc.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void getSeasons(List<Season> list){
        SeasonTVAdapter adapter = new SeasonTVAdapter(R.layout.item_mediatv_season);
        binding.layoutContent.tvDetailsSeasonRc.setHasFixedSize(true);
        binding.layoutContent.tvDetailsSeasonRc.setLayoutManager(new LinearLayoutManager(this));
        binding.layoutContent.tvDetailsSeasonRc.addItemDecoration(new VerticalItemDecoration(16,16, 16,16));
        adapter.setList(list);
        binding.layoutContent.tvDetailsSeasonRc.setAdapter(adapter);
    }

    private void getMovieCasters(){
        CastRecyclerViewAdapter adapter = new CastRecyclerViewAdapter(R.layout.item_caster);
        binding.layoutContent.tvDetailsCastRc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.layoutContent.tvDetailsCastRc.setHasFixedSize(true);
        binding.layoutContent.tvDetailsCastRc.addItemDecoration(new HorizontalItemDecoration(8,8,8,8, 16));

        viewModel.getCredits().observe(this, new Observer<MovieCredits>() {
            @Override
            public void onChanged(MovieCredits movieCredits) {
                if (movieCredits.getCast() != null && movieCredits.getCast().size() > 0){
                    if (movieCredits.getCast().size() >= 8){
                        adapter.setList(movieCredits.getCast().subList(0,8));
                    }else {
                        adapter.setList(movieCredits.getCast());
                    }
                    binding.layoutContent.tvDetailsCastRc.setAdapter(adapter);
                }
            }
        });
    }

    private void getMovieReviews(){
        ReviewAdapter adapter = new ReviewAdapter();
        binding.layoutContent.tvDetailsReviewRc.setHasFixedSize(true);
        binding.layoutContent.tvDetailsReviewRc.setLayoutManager(new LinearLayoutManager(this));
        binding.layoutContent.tvDetailsReviewRc.setAdapter(adapter);
        binding.layoutContent.tvDetailsReviewRc.addItemDecoration(new ActorsItemDecoration(16));

        viewModel.getReview().observe(this, new Observer<Reviews>() {
            @Override
            public void onChanged(Reviews reviews) {
                if (reviews.getResults() != null && reviews.getResults().size() > 0){
                    binding.layoutContent.tvDetailsNoReview.setVisibility(View.GONE);
                    if (reviews.getResults().size() > 3){
                        adapter.setList(reviews.getResults().subList(0,3));
                    }else {
                        adapter.setList(reviews.getResults());
                    }
                }else{
                    binding.layoutContent.tvDetailsNoReview.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getMovieImages(){
        ImageAdapter adapter = new ImageAdapter(R.layout.item_mediatv_details_image);
        binding.layoutContent.tvDetailsImagesRc.setHasFixedSize(true);
        binding.layoutContent.tvDetailsImagesRc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.layoutContent.tvDetailsImagesRc.addItemDecoration(new HorizontalItemDecoration(8,8,4,4, 16));
        binding.layoutContent.tvDetailsImagesRc.setAdapter(adapter);

        viewModel.getImages().observe(this, new Observer<Images>() {
            @Override
            public void onChanged(Images images) {
                if (images.getBackdrops() != null && images.getBackdrops().size() > 0){
                    if (images.getBackdrops().size() >= 6){
                        adapter.setList(images.getBackdrops().subList(0, 6));
                    }else {
                        adapter.setList(images.getBackdrops());
                    }
                }
            }
        });
    }

    private void getMovieVideos(){
        VideoAdapter adapter = new VideoAdapter(R.layout.item_video_large);
        binding.layoutContent.tvDetailsVideoRc.setHasFixedSize(true);
        binding.layoutContent.tvDetailsVideoRc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.layoutContent.tvDetailsVideoRc.addItemDecoration(new HorizontalItemDecoration(24,8,8,8,16));
        binding.layoutContent.tvDetailsVideoRc.setAdapter(adapter);

        viewModel.getVideos().observe(this, new Observer<MovieVideos>() {
            @Override
            public void onChanged(MovieVideos movieVideos) {
                if (movieVideos.getResults() != null && movieVideos.getResults().size() > 0){
                    if (movieVideos.getResults().size() >= 4){
                        adapter.setList(movieVideos.getResults().subList(0,4));
                    }else {
                        adapter.setList(movieVideos.getResults());
                    }
                }
            }
        });
    }

    /*private void getMovieStates(){
        viewModel.getMovieStates(movieId, manager.getSessionId(), new OnAccountStatesResponse() {
            @Override
            public void OnResponse(AccountStates accountStates) {
                if (accountStates != null && accountStates.getId() != null){
                    if (accountStates.getFavorite()){
                        menu.getItem( 2).setIcon(R.drawable.ic_baseline_favorite);
                    }else {
                        menu.getItem(2).setIcon(R.drawable.ic_baseline_favorite_border_24);
                    }

                    if (accountStates.getWatchlist()){
                        //unimplemented
                    }else {
                        //unimplemented
                    }
                }
            }

            @Override
            public void OnError(String e) {

            }
        });
    }*/

    private void errorShowNetworkError(){
        binding.layoutContent.getRoot().setVisibility(View.GONE);
        //binding.movieDetailFabFavorites.setVisibility(View.GONE);
        binding.layoutEmpty.getRoot().setVisibility(View.VISIBLE);
        String title = "Network Unavailable";
        String message = "Try to check your current connection and refresh this page";
        int icon = R.drawable.error_conn;

        binding.layoutEmpty.errorImage.setImageResource(icon);
        binding.layoutEmpty.errorMessage.setText(message);
        binding.layoutEmpty.errorTitle.setText(title);
        binding.layoutEmpty.errorAction.setVisibility(View.GONE);
    }

    private void hideErrorPage(){
        binding.layoutContent.getRoot().setVisibility(View.VISIBLE);
        //binding.movieDetailFabFavorites.setVisibility(View.VISIBLE);
        binding.layoutEmpty.getRoot().setVisibility(View.GONE);
    }

    private boolean isNetworkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        for (Network network : connectivityManager.getAllNetworks()){
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                isConnected = true;
            }
        }
        return isConnected;
    }

    /*private void refreshPage(){
        binding.layoutEmpty.emptyRefresh.setRefreshing(true);
        if (isNetworkConnected()){
            binding.layoutEmpty.emptyRefresh.setRefreshing(false);
            hideErrorPage();
            observeMovieDetails();
        }else {
            binding.layoutEmpty.emptyRefresh.setRefreshing(false);
            errorShowNetworkError();
        }
    }*/
}