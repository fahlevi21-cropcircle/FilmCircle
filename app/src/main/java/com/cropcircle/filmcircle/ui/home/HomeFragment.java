package com.cropcircle.filmcircle.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.MainActivity;
import com.cropcircle.filmcircle.PreferenceManager;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.FragmentHomeBinding;
import com.cropcircle.filmcircle.models.allmedia.AllMedia;
import com.cropcircle.filmcircle.models.movie.Movie;
import com.cropcircle.filmcircle.models.tv.MediaTV;
import com.cropcircle.filmcircle.ui.home.adapter.AllMediaAdapter;
import com.cropcircle.filmcircle.ui.home.adapter.CardSliderAdapter;
import com.cropcircle.filmcircle.ui.home.adapter.MediaTVAdapter;
import com.cropcircle.filmcircle.ui.home.adapter.MovieAdapter;
import com.cropcircle.filmcircle.ui.home.adapter.SliderAdapter;
import com.cropcircle.filmcircle.ui.home.itemdecoration.HorizontalItemDecoration;
import com.cropcircle.filmcircle.ui.home.itemdecoration.VerticalItemDecoration;
import com.cropcircle.filmcircle.ui.home.sub.MediaTVDetailsActivity;
import com.cropcircle.filmcircle.ui.home.sub.MovieDetailsActivity;
import com.cropcircle.filmcircle.util.NetworkChangeReceiver;
import com.cropcircle.filmcircle.util.NetworkUtils;
import com.google.android.exoplayer2.util.Log;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private CountDownTimer timer;
    private NetworkChangeReceiver receiver;
    private boolean isViewDrawed = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        isViewDrawed = false;
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        ((MainActivity) getActivity()).setSupportActionBar(binding.layoutDiscover.homeToolbar);
        binding.layoutEmpty.emptyRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isViewDrawed = false;
            }
        });

        receiver = new NetworkChangeReceiver(new NetworkChangeReceiver.OnNetworkChangeListener() {
            @Override
            public void onNetworkConnected(int status) {
                //Toast.makeText(getContext(), "Connected : " + status, Toast.LENGTH_SHORT).show();
                if (!isViewDrawed){
                    observeData();
                }
            }

            @Override
            public void onNetworkNotConnected(int status) {
                Toast.makeText(getContext(), "Not connected : " + status, Toast.LENGTH_SHORT).show();
            }
        });


        /* Using below timeout timer method to avoid recyclerview multiply margins */
        //init timeout for connections
        //initTimer();
        hideLoading();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(receiver);
    }

    @Deprecated
    private void initTimer() {
        //init timer is used to set timeout for slow network or fetch data from internet with no
        //network within 10 seconds (10000 millis)
        timer = new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long l) {
                //showing loading while checking network connection in background
                //showLoading();
                checkConnection();
            }

            @Override
            public void onFinish() {
                //hideLoading();
                errorShowNetworkError();
                Toast.makeText(getContext(), "Network Unavailable!", Toast.LENGTH_SHORT).show();
            }
        };
        timer.start();
    }

    private void checkConnection() {
        //if network connected show data else do nothing while not connected
        if (isNetworkConnected()) {
            hideErrorPage();
            observeData();
            //stop the timer
            timer.cancel();
        }
    }

    private void observeData() {
        binding.layoutEmpty.emptyRefresh.setRefreshing(false);
        isViewDrawed = true;
        setupSlider();
        setupTrendingMovie();
        setupUpcoming();
        setupNowPlaying();
    }

    private void setupNowPlaying() {
        CardSliderAdapter adapter = new CardSliderAdapter();
        binding.layoutDiscover.homeRcNowPlaying.setAdapter(adapter);
        binding.layoutDiscover.homeRcNowPlaying.setOffScreenPageLimit(3);
        binding.layoutDiscover.homeRcNowPlaying.setLifecycleRegistry(getLifecycle());
        homeViewModel.nowPlaying().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null && movies.size() > 0) {
                    binding.layoutDiscover.homeRcNowPlaying.create(movies);
                }
            }
        });
    }

    private void setupSlider() {
        SliderAdapter adapter = new SliderAdapter();
        binding.layoutDiscover.homeSlider.setAdapter(adapter);
        binding.layoutDiscover.homeRcNowPlaying.setOffScreenPageLimit(3);
        binding.layoutDiscover.homeSlider.setIndicatorSliderGap(6);
        binding.layoutDiscover.homeSlider.setLifecycleRegistry(getLifecycle());
        homeViewModel.banners().observe(getViewLifecycleOwner(), new Observer<List<AllMedia>>() {
            @Override
            public void onChanged(List<AllMedia> allMedia) {
                if (allMedia != null && allMedia.size() > 0) {
                    Collections.sort(allMedia);
                    binding.layoutDiscover.homeSlider.create(allMedia.subList(0, 9));
                }
            }
        });
    }

    private void setupTrendingMovie() {
        AllMediaAdapter trendingAdapter = new AllMediaAdapter(R.layout.item_big_linear);
        binding.layoutDiscover.homeRcTrending.setHasFixedSize(true);
        binding.layoutDiscover.homeRcTrending.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.layoutDiscover.homeRcTrending.setAdapter(trendingAdapter);

        trendingAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                AllMedia data = (AllMedia) adapter.getItem(position);
                Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                intent.putExtra(Constants.MOVIE_ID_KEY, data.getId());
                startActivity(intent);
            }
        });

        homeViewModel.trendingMovies().observe(getViewLifecycleOwner(), new Observer<List<AllMedia>>() {
            @Override
            public void onChanged(List<AllMedia> movies) {
                if (movies != null && movies.size() > 0) {
                    //hideLoading();
                    Collections.sort(movies, Collections.reverseOrder());
                    trendingAdapter.setList(movies);
                }
            }
        });
        binding.layoutDiscover.homeRcTrending.addItemDecoration(new HorizontalItemDecoration(16, 16, 16, 16, 16));

    }

    private void setupUpcoming() {
        //popular
        MovieAdapter newReleaseAdapter = new MovieAdapter(R.layout.item_small_linear);
        binding.layoutDiscover.homeRcNewRelease.setHasFixedSize(true);
        binding.layoutDiscover.homeRcNewRelease.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.layoutDiscover.homeRcNewRelease.setAdapter(newReleaseAdapter);
        binding.layoutDiscover.homeRcNewRelease.addItemDecoration(new HorizontalItemDecoration(16, 32, 16, 16, 16));
        newReleaseAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Movie data = (Movie) adapter.getItem(position);
                Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                intent.putExtra(Constants.MOVIE_ID_KEY, data.getId());
                startActivity(intent);
            }
        });

        homeViewModel.getNewRelease().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null && movies.size() > 0) {
                    Comparator<Movie> comparator = new Comparator<Movie>() {
                        @Override
                        public int compare(Movie movie, Movie t1) {
                            return movie.getReleaseDate().compareTo(t1.getReleaseDate());
                        }
                    };

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(movies, comparator.reversed());
                    }
                    newReleaseAdapter.setList(movies);
                }
            }
        });
    }

    private void errorShowNetworkError() {
        binding.layoutDiscover.getRoot().setVisibility(View.GONE);
        binding.layoutEmpty.getRoot().setVisibility(View.VISIBLE);
        String title = "Network Unavailable";
        String message = "Try to check your current connection and refresh this page";
        int icon = R.drawable.error_conn;

        binding.layoutEmpty.errorImage.setImageResource(icon);
        binding.layoutEmpty.errorMessage.setText(message);
        binding.layoutEmpty.errorTitle.setText(title);
        binding.layoutEmpty.errorAction.setVisibility(View.GONE);
    }

    private void hideErrorPage() {
        binding.layoutDiscover.getRoot().setVisibility(View.VISIBLE);
        binding.layoutEmpty.getRoot().setVisibility(View.GONE);
    }

    private void showLoading(){
        binding.layoutLoadingHome.getRoot().setVisibility(View.VISIBLE);
        binding.layoutDiscover.getRoot().setVisibility(View.INVISIBLE);
    }

    private void hideLoading(){
        new CountDownTimer(1650,1000){

            @Override
            public void onTick(long l) {
                //do nothing as shimmer is already showing
            }

            @Override
            public void onFinish() {
                binding.layoutLoadingHome.getRoot().setVisibility(View.GONE);
                binding.layoutDiscover.getRoot().setVisibility(View.VISIBLE);
                binding.layoutDiscover.homeToolbar.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        for (Network network : connectivityManager.getAllNetworks()) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isConnected = true;
            }
        }
        return isConnected;
    }
}