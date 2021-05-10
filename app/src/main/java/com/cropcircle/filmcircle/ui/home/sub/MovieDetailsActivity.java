package com.cropcircle.filmcircle.ui.home.sub;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.PreferenceManager;
import com.cropcircle.filmcircle.databinding.ActivityMovieDetailsBinding;
import com.cropcircle.filmcircle.models.action.AccountStates;
import com.cropcircle.filmcircle.models.action.ActionResponse;
import com.cropcircle.filmcircle.models.action.OnAccountStatesResponse;
import com.cropcircle.filmcircle.models.action.OnActionResponse;
import com.cropcircle.filmcircle.models.movie.MovieDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.models.user.User;
import com.cropcircle.filmcircle.ui.home.sub.tabs.TabsAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MovieDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMovieDetailsBinding binding;
    MovieDetailsViewModel viewModel;
    PreferenceManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        setContentView(binding.getRoot());

        setSupportActionBar(binding.movieDetailsToolbar);
        setTitle(null);
        binding.movieDetailsToolbar.setTitle(null);
        binding.setLifecycleOwner(this);

        manager = new PreferenceManager(this);
        viewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);

        binding.layoutEmpty.emptyRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });

        binding.movieDetailFabFavorites.setOnClickListener(this);

        if (isNetworkConnected()){
            hideErrorPage();
            observeMovieDetails();
        }else {
            errorShowNetworkError();
        }
    }


    @Override
    public void onClick(View view) {
        if(isNetworkConnected()){
            hideErrorPage();
            Intent intent = getIntent();
            int movieId = intent.getIntExtra(Constants.MOVIE_ID_KEY, 0);

            String sessionId = manager.getSessionId();
            User userData = manager.getUserdata();
            if (binding.getData() != null){
                viewModel.addToFavorites(sessionId, binding.getData().getId(),  userData.getId(), new OnActionResponse() {
                    @Override
                    public void OnResponse(ActionResponse response) {
                        if (response != null && response.getStatusCode() != null){
                            Toast.makeText(MovieDetailsActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();
                            viewModel.getMovieStates(movieId, manager.getSessionId(), new OnAccountStatesResponse() {
                                @Override
                                public void OnResponse(AccountStates accountStates) {
                                    if (accountStates != null && accountStates.getId() != null){
                                        if (accountStates.getFavorite()){
                                            binding.movieDetailFabFavorites.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                        }else {
                                            binding.movieDetailFabFavorites.setImageResource(R.drawable.ic_baseline_favorite);
                                        }
                                    }
                                }

                                @Override
                                public void OnError(String e) {
                                    Toast.makeText(MovieDetailsActivity.this, "Network Error : " + e, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void OnError(String e) {
                        Toast.makeText(MovieDetailsActivity.this, "error " + e, Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(MovieDetailsActivity.this, "Something wrong!", Toast.LENGTH_SHORT).show();
            }
        }else {
            errorShowNetworkError();
        }
    }

    private void observeMovieDetails(){
        Intent intent = getIntent();
        int movieId = intent.getIntExtra(Constants.MOVIE_ID_KEY, 0);
        viewModel.setMovieId(movieId);
        viewModel.getDetails().observe(this, new Observer<MovieDetails>() {
            @Override
            public void onChanged(MovieDetails movieDetails) {
                binding.setData(movieDetails);
            }
        });

        prepareTabs();
        getMovieStates(movieId);
    }

    private void prepareTabs(){
        Intent intent = getIntent();
        int movieId = intent.getIntExtra(Constants.MOVIE_ID_KEY, 0);
        TabsAdapter adapter = new TabsAdapter(this);
        adapter.setMovieId(movieId);

        binding.layoutContent.movieDetailsPager.setAdapter(adapter);

        String[] titles = {
                "Information",
                "Images",
                "Videos",
                "Reviews",
                "More"
        };

        new TabLayoutMediator(
                binding.layoutContent.layoutMovieDetailsHeader.movieDetailsTabs,
                binding.layoutContent.movieDetailsPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(titles[position]);
                        //tab.setIcon(icons[position]);
                    }
                }
        ).attach();
    }

    private void getMovieStates(int movieId){
        viewModel.getMovieStates(movieId, manager.getSessionId(), new OnAccountStatesResponse() {
            @Override
            public void OnResponse(AccountStates accountStates) {
                if (accountStates != null && accountStates.getId() != null){
                    if (accountStates.getFavorite()){
                        binding.movieDetailFabFavorites.setImageResource(R.drawable.ic_baseline_favorite);
                    }else {
                        binding.movieDetailFabFavorites.setImageResource(R.drawable.ic_baseline_favorite_border_24);
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
        binding.movieDetailFabFavorites.setVisibility(View.GONE);
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
        binding.movieDetailFabFavorites.setVisibility(View.VISIBLE);
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