package com.cropcircle.filmcircle.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.PreferenceManager;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.FragmentHomeBinding;
import com.cropcircle.filmcircle.models.allmedia.Result;
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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.homeToolbar);
        ((AppCompatActivity) getActivity()).setTitle(null);

        PreferenceManager manager = new PreferenceManager(getContext());
        if(manager.getUserdata() != null){
            String sessionId = manager.getSessionId() + "and id = " + manager.getUserdata().getId();
            binding.layoutDiscover.sessionIdDummy.setText(sessionId);
        }
        binding.layoutDiscover.sessionIdDummy.setVisibility(View.GONE);
        showLoading();
        observeData();

        binding.layoutEmpty.emptyRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                observeData();
            }
        });
    }

    private void observeData(){
        binding.layoutEmpty.emptyRefresh.setRefreshing(false);
        if (isNetworkConnected()){
            hideErrorPage();
            setupSlider();
            setupPopularMovies();
            setupTrendingMovie();
            setupTrendingTV();
            setupPopularTV();
            setupUpcoming();
            setupNowPlaying();
        }else {
            errorShowNetworkError();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupNowPlaying(){
        CardSliderAdapter adapter = new CardSliderAdapter();
        binding.layoutDiscover.homeRcNowPlaying.setAdapter(adapter);
        binding.layoutDiscover.homeRcNowPlaying.setOffScreenPageLimit(3);
        binding.layoutDiscover.homeRcNowPlaying.setLifecycleRegistry(getLifecycle());
        homeViewModel.nowPlaying().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                binding.layoutDiscover.homeRcNowPlaying.create(movies);
            }
        });
    }

    private void setupSlider(){
        SliderAdapter adapter = new SliderAdapter();
        binding.layoutDiscover.homeSlider.setAdapter(adapter);
        binding.layoutDiscover.homeRcNowPlaying.setOffScreenPageLimit(3);
        binding.layoutDiscover.homeSlider.setIndicatorSliderGap(6);
        binding.layoutDiscover.homeSlider.setLifecycleRegistry(getLifecycle());
        homeViewModel.banners().observe(getViewLifecycleOwner(), new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                Collections.sort(results);
                binding.layoutDiscover.homeSlider.create(results.subList(0,9));
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
                Result data = (Result) adapter.getItem(position);
                if (data.getMediaType().toLowerCase().contains("movie")){
                    Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                    intent.putExtra(Constants.MOVIE_ID_KEY, data.getId());
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "TV Media Type", Toast.LENGTH_SHORT).show();
                }
            }
        });

        homeViewModel.trendingMovies().observe(getViewLifecycleOwner(), new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> movies) {
                if (movies != null && movies.size()> 0){
                    hideLoading();
                    Collections.sort(movies, Collections.reverseOrder());
                    trendingAdapter.setList(movies);
                }
            }
        });
        binding.layoutDiscover.homeRcTrending.addItemDecoration(new HorizontalItemDecoration(12, 12, 12, 12, 16));

    }

    private void setupUpcoming(){
        //popular
        MovieAdapter newReleaseAdapter = new MovieAdapter(R.layout.item_small_linear);
        binding.layoutDiscover.homeRcNewRelease.setHasFixedSize(true);
        binding.layoutDiscover.homeRcNewRelease.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.layoutDiscover.homeRcNewRelease.setAdapter(newReleaseAdapter);
        binding.layoutDiscover.homeRcNewRelease.addItemDecoration(new HorizontalItemDecoration(8, 32, 8, 8, 16));
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
                if(movies != null && movies.size() > 0){
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

    private void setupPopularMovies(){
        //popular
        MovieAdapter popularAdapter = new MovieAdapter(R.layout.item_small_vertical_movie);
        binding.layoutDiscover.homeRcPopularMovies.setHasFixedSize(true);
        binding.layoutDiscover.homeRcPopularMovies.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.layoutDiscover.homeRcPopularMovies.setAdapter(popularAdapter);
        binding.layoutDiscover.homeRcPopularMovies.addItemDecoration(new VerticalItemDecoration(16, 16, 52, 52));
        popularAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Movie data = (Movie) adapter.getItem(position);
                Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                intent.putExtra(Constants.MOVIE_ID_KEY, data.getId());
                startActivity(intent);
            }
        });

        homeViewModel.getPopularMovies().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null && movies.size() > 0){
                    /*Comparator<Movie> comparator = new Comparator<Movie>() {
                        @Override
                        public int compare(Movie movie, Movie t1) {
                            return movie.getVoteAverage().compareTo(t1.getVoteAverage());
                        }
                    };
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(movies, comparator.reversed());
                    }*/
                    if (movies.size() >= 6){
                        popularAdapter.setList(movies.subList(0,6));
                    }else {
                        popularAdapter.setList(movies);
                    }
                }
            }
        });
    }

    private void setupPopularTV(){
        MediaTVAdapter adapter = new MediaTVAdapter(R.layout.item_small_vertical_tv);
        binding.layoutDiscover.homeRcPopularTv.setHasFixedSize(true);
        binding.layoutDiscover.homeRcPopularTv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.layoutDiscover.homeRcPopularTv.addItemDecoration(new VerticalItemDecoration(16,16,52,52));
        binding.layoutDiscover.homeRcPopularTv.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                MediaTV data = (MediaTV) adapter.getItem(position);
                Intent intent = new Intent(getContext(), MediaTVDetailsActivity.class);
                intent.putExtra(Constants.MOVIE_ID_KEY, data.getId());
                startActivity(intent);
            }
        });

        homeViewModel.getPopularTV().observe(getViewLifecycleOwner(), new Observer<List<MediaTV>>() {
            @Override
            public void onChanged(List<MediaTV> mediaTVS) {
                if (mediaTVS != null && mediaTVS.size() > 0){
                    /*Comparator<MediaTV> comparator = new Comparator<MediaTV>() {
                        @Override
                        public int compare(MediaTV mediaTV, MediaTV t1) {
                            return mediaTV.getPopularity().compareTo(t1.getPopularity());
                        }
                    };
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(mediaTVS, comparator.reversed());
                    }*/
                    if (mediaTVS.size() >= 6){
                        adapter.setList(mediaTVS.subList(0,6));
                    }else {
                        adapter.setList(mediaTVS);
                    }
                }
            }
        });
    }

    private void setupTrendingTV(){
        AllMediaAdapter tvTrendingAdapter = new AllMediaAdapter(R.layout.item_big_linear);
        binding.layoutDiscover.homeRcTvTrending.setHasFixedSize(true);
        binding.layoutDiscover.homeRcTvTrending.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.layoutDiscover.homeRcTvTrending.setAdapter(tvTrendingAdapter);

        tvTrendingAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Result data = (Result) adapter.getItem(position);
                if (data.getMediaType().toLowerCase().contains("tv")){
                    Intent intent = new Intent(getContext(), MediaTVDetailsActivity.class);
                    intent.putExtra(Constants.MOVIE_ID_KEY, data.getId());
                    startActivity(intent);
                }
            }
        });

        homeViewModel.trendingTVs().observe(getViewLifecycleOwner(), new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> movies) {
                tvTrendingAdapter.setList(movies);
            }
        });
        binding.layoutDiscover.homeRcTvTrending.addItemDecoration(new HorizontalItemDecoration(8, 8, 8, 8,16));
    }

    private void errorShowNetworkError(){
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

    private void hideErrorPage(){
        binding.layoutDiscover.getRoot().setVisibility(View.VISIBLE);
        binding.layoutEmpty.getRoot().setVisibility(View.GONE);
    }

    private void showLoading(){
        binding.layoutDiscover.homeContainer.setVisibility(View.GONE);
        binding.layoutDiscover.homeLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        binding.layoutDiscover.homeContainer.setVisibility(View.VISIBLE);
        binding.layoutDiscover.homeLoading.setVisibility(View.GONE);
    }

    private boolean isNetworkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        for (Network network : connectivityManager.getAllNetworks()){
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                isConnected = true;
            }
        }
        return isConnected;
    }
}