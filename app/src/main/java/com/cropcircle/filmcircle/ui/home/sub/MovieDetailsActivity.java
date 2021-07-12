package com.cropcircle.filmcircle.ui.home.sub;

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
import com.cropcircle.filmcircle.databinding.ActivityMovieDetailsBinding;
import com.cropcircle.filmcircle.models.action.AccountStates;
import com.cropcircle.filmcircle.models.action.ActionResponse;
import com.cropcircle.filmcircle.models.action.OnAccountStatesResponse;
import com.cropcircle.filmcircle.models.action.OnActionResponse;
import com.cropcircle.filmcircle.models.movie.Backdrop;
import com.cropcircle.filmcircle.models.movie.Genre;
import com.cropcircle.filmcircle.models.movie.MovieDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.models.movie.Video;
import com.cropcircle.filmcircle.models.people.Cast;
import com.cropcircle.filmcircle.models.review.Review;
import com.cropcircle.filmcircle.models.user.User;
import com.cropcircle.filmcircle.ui.home.adapter.GenreAdapter;
import com.cropcircle.filmcircle.ui.home.adapter.ImageAdapter;
import com.cropcircle.filmcircle.ui.home.adapter.ReviewAdapter;
import com.cropcircle.filmcircle.ui.home.adapter.VideoAdapter;
import com.cropcircle.filmcircle.ui.home.itemdecoration.HorizontalItemDecoration;
import com.cropcircle.filmcircle.ui.home.itemdecoration.ImageGridItemDecoration;
import com.cropcircle.filmcircle.ui.home.itemdecoration.VerticalItemDecoration;

import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {
    ActivityMovieDetailsBinding binding;
    MovieDetailsViewModel viewModel;
    PreferenceManager manager;
    private Menu menu;
    int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        movieId = intent.getIntExtra(Constants.MOVIE_ID_KEY, 0);

        setSupportActionBar(binding.movieDetailsToolbar);
        setTitle(null);
        binding.setLifecycleOwner(this);
        binding.movieDetailsToolbar.setTitle(null);
        manager = new PreferenceManager(this);
        viewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);

        binding.layoutEmpty.emptyRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });

        binding.movieDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (isNetworkConnected()){
            hideErrorPage();
            observeMovieDetails();
        }else {
            errorShowNetworkError();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_to_favorite){
            viewModel.getMovieStates(movieId, manager.getSessionId(), new OnAccountStatesResponse() {
                @Override
                public void OnResponse(AccountStates accountStates) {
                    if (accountStates != null){
                        if (accountStates.getFavorite()){
                            viewModel.removeFromFavorites(manager.getSessionId(), movieId, manager.getUserdata().getId(), new OnActionResponse() {
                                @Override
                                public void OnResponse(ActionResponse response) {
                                    getMovieStates();
                                }

                                @Override
                                public void OnError(String e) {
                                    Toast.makeText(MovieDetailsActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            viewModel.addToFavorites(manager.getSessionId(), movieId, manager.getUserdata().getId(), new OnActionResponse() {
                                @Override
                                public void OnResponse(ActionResponse response) {
                                    getMovieStates();
                                }

                                @Override
                                public void OnError(String e) {
                                    Toast.makeText(MovieDetailsActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }

                @Override
                public void OnError(String e) {

                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void observeMovieDetails(){
        viewModel.setMovieId(movieId);
        viewModel.getDetails().observe(this, new Observer<MovieDetails>() {
            @Override
            public void onChanged(MovieDetails movieDetails) {
                if (movieDetails != null){
                    binding.setData(movieDetails);
                    binding.layoutContent.movieDetailsRating.setStar(movieDetails.getVoteAverage().floatValue() - 5.0f);
                    String bullet = " &bull; ";
                    String voteCount = movieDetails.getVoteCount() + " User(s)";
                    binding.layoutContent.movieDetailsVoteCount.setText(voteCount);
                    if (movieDetails.getTagline() != null){
                        //String tagline = "' " + movieDetails.getTagline() + " '";
                        binding.layoutContent.movieDetailsTagline.setText(movieDetails.getTagline());
                        binding.layoutContent.movieDetailsTagline.setVisibility(View.VISIBLE);
                    }else{
                        binding.layoutContent.movieDetailsTagline.setVisibility(View.GONE);
                    }
                    String releaseYearText = movieDetails.getReleaseDate().substring(0,4) + bullet;
                    String durationText = bullet + movieDetails.getRuntime().toString() + " Minutes";
                    getMovieGenres(movieDetails.getGenres());
                }
            }
        });

        getMovieCasters();
        getMovieReviews();
        getMovieImages();
        getMovieVideos();
        getMovieStates();
        //prepareTabs();
    }

    private void getMovieGenres(List<Genre> genreList){
        for (int i = 0; i < genreList.size(); i++){
            binding.layoutContent.movieDetailsGenre.append(genreList.get(i).getName());
            if (i != genreList.size() - 1){
                binding.layoutContent.movieDetailsGenre.append(", ");
            }else {
                binding.layoutContent.movieDetailsGenre.append(" ");
            }
        }
    }

    private void getMovieCasters(){
        CastRecyclerViewAdapter adapter = new CastRecyclerViewAdapter(R.layout.item_caster);
        binding.layoutContent.movieDetailsCastRc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.layoutContent.movieDetailsCastRc.setHasFixedSize(true);
        binding.layoutContent.movieDetailsCastRc.addItemDecoration(new HorizontalItemDecoration(8,8,0,0, 16));
        viewModel.getCasters().observe(this, new Observer<List<Cast>>() {
            @Override
            public void onChanged(List<Cast> casts) {
                if (casts != null && casts.size() > 0){
                    if (casts.size() >= 8){
                        adapter.setList(casts.subList(0, 8));
                    }else {
                        adapter.setList(casts);
                    }
                    binding.layoutContent.movieDetailsCastRc.setAdapter(adapter);
                }
            }
        });
    }

    private void getMovieReviews(){
        ReviewAdapter adapter = new ReviewAdapter();
        binding.layoutContent.movieDetailsReviewRc.setHasFixedSize(true);
        binding.layoutContent.movieDetailsReviewRc.setLayoutManager(new LinearLayoutManager(this));
        binding.layoutContent.movieDetailsReviewRc.setAdapter(adapter);
        binding.layoutContent.movieDetailsReviewRc.addItemDecoration(new VerticalItemDecoration(16,16,16,16));

        viewModel.getReview().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                if (reviews != null && reviews.size() > 0){
                    binding.layoutContent.movieDetailsNoReview.setVisibility(View.GONE);
                    if (reviews.size() > 3){
                        adapter.setList(reviews.subList(0,3));
                        int totalReview = reviews.size() - 3;
                        binding.layoutContent.movieDetailsReviewAll.setText("See all (" + totalReview + ")");
                    }else {
                        binding.layoutContent.movieDetailsReviewAll.setVisibility(View.GONE);
                        adapter.setList(reviews);
                    }
                }else{
                    binding.layoutContent.movieDetailsReviewAll.setVisibility(View.GONE);
                    binding.layoutContent.movieDetailsNoReview.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getMovieImages(){
        ImageAdapter adapter = new ImageAdapter(R.layout.item_image_small_grid);
        binding.layoutContent.movieDetailsImagesRc.setHasFixedSize(true);
        binding.layoutContent.movieDetailsImagesRc.setLayoutManager(new GridLayoutManager(this, 3));
        binding.layoutContent.movieDetailsImagesRc.addItemDecoration(new ImageGridItemDecoration(8,8,8,8));
        binding.layoutContent.movieDetailsImagesRc.setAdapter(adapter);

        viewModel.getImages().observe(this, new Observer<List<Backdrop>>() {
            @Override
            public void onChanged(List<Backdrop> backdrops) {
                if (backdrops != null && backdrops.size() > 0){
                    if (backdrops.size() >= 6){
                        adapter.setList(backdrops.subList(0, 6));
                    }else {
                        adapter.setList(backdrops);
                    }
                }
            }
        });
    }

    private void getMovieVideos(){
        VideoAdapter adapter = new VideoAdapter(R.layout.item_video_large);
        binding.layoutContent.movieDetailsVideoRc.setHasFixedSize(true);
        binding.layoutContent.movieDetailsVideoRc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.layoutContent.movieDetailsVideoRc.addItemDecoration(new HorizontalItemDecoration(24,8,8,8,16));
        binding.layoutContent.movieDetailsVideoRc.setAdapter(adapter);

        viewModel.getVideos().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                if (videos != null && videos.size() > 0){
                    if (videos.size() >= 4){
                        adapter.setList(videos.subList(0,4));
                    }else {
                        adapter.setList(videos);
                    }
                }
            }
        });
    }

    private void getMovieStates(){
        viewModel.getMovieStates(movieId, manager.getSessionId(), new OnAccountStatesResponse() {
            @Override
            public void OnResponse(AccountStates accountStates) {
                if (accountStates != null && accountStates.getId() != null){
                    if (accountStates.getFavorite()){
                        menu.getItem( 1).setIcon(R.drawable.ic_baseline_favorite);
                    }else {
                        menu.getItem(1).setIcon(R.drawable.ic_baseline_favorite_border_24);
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
    }

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

    private void refreshPage(){
        binding.layoutEmpty.emptyRefresh.setRefreshing(true);
        if (isNetworkConnected()){
            binding.layoutEmpty.emptyRefresh.setRefreshing(false);
            hideErrorPage();
            observeMovieDetails();
        }else {
            binding.layoutEmpty.emptyRefresh.setRefreshing(false);
            errorShowNetworkError();
        }
    }

}