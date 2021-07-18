package com.cropcircle.filmcircle.ui.collection.movie;

import android.content.Intent;
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
import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.PreferenceManager;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.FragmentCollectionsBinding;
import com.cropcircle.filmcircle.models.movie.Movie;
import com.cropcircle.filmcircle.ui.home.adapter.MovieAdapter;
import com.cropcircle.filmcircle.ui.home.itemdecoration.GridItemDecoration;
import com.cropcircle.filmcircle.ui.home.sub.MovieDetailsActivity;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CollectionsFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private CollectionsViewModel viewModel;
    private PreferenceManager manager;
    private FragmentCollectionsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CollectionsViewModel.class);
        manager = new PreferenceManager(getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCollectionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.collectionsToolbar);


        binding.rcCollectionsFavorite.setHasFixedSize(true);
        binding.rcCollectionsFavorite.addItemDecoration(new GridItemDecoration(12,12,12,12));

        //default selected tab
        showLoading();
        observeFavorite();

        binding.collectionsTabs.addOnTabSelectedListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        binding.collectionsTabs.removeOnTabSelectedListener(this);
    }

    private void observeFavorite(){
        MovieAdapter adapter = new MovieAdapter(R.layout.item_favorite_movie);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                Movie movie = (Movie) adapter.getData().get(position);
                startActivity(new Intent(getContext(), MovieDetailsActivity.class).putExtra(Constants.MOVIE_ID_KEY, movie.getId()));
            }
        });
        binding.rcCollectionsFavorite.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rcCollectionsFavorite.setAdapter(adapter);
        viewModel.getFavoriteMovieList(manager.getUserdata().getId(), manager.getSessionId()).observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null && movies.size() > 0){
                    hideLoading();
                    adapter.setList(movies);
                }
            }
        });
    }

    private void observeWatchlist(){
        MovieAdapter adapter = new MovieAdapter(R.layout.item_favorite_movie);
        binding.rcCollectionsFavorite.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rcCollectionsFavorite.setAdapter(adapter);
        viewModel.getMovieWatchlist(manager.getUserdata().getId(), manager.getSessionId()).observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null && movies.size() > 0){
                    hideLoading();
                    adapter.setList(movies);
                }
            }
        });
    }

    private void observeList(){}

    private void showLoading(){
        binding.collectionsLoading.setVisibility(View.VISIBLE);
        binding.rcCollectionsFavorite.setVisibility(View.GONE);
    }

    private void hideLoading(){
        binding.collectionsLoading.setVisibility(View.GONE);
        binding.rcCollectionsFavorite.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0){
            showLoading();
            observeFavorite();
        }else if (tab.getPosition() == 1){
            showLoading();
            observeWatchlist();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}