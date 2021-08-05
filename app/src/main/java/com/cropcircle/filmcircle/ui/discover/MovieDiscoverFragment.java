package com.cropcircle.filmcircle.ui.discover;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.MainActivity;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.MovieDiscoverFragmentBinding;
import com.cropcircle.filmcircle.models.movie.Genre;
import com.cropcircle.filmcircle.models.movie.Movie;
import com.cropcircle.filmcircle.models.tv.MediaTV;
import com.cropcircle.filmcircle.ui.home.adapter.MediaTVAdapter;
import com.cropcircle.filmcircle.ui.home.adapter.MovieAdapter;
import com.cropcircle.filmcircle.ui.home.itemdecoration.GridItemDecoration;
import com.cropcircle.filmcircle.ui.home.sub.MediaTVDetailsActivity;
import com.cropcircle.filmcircle.ui.home.sub.MovieDetailsActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.tabs.TabLayout;
import com.paulrybitskyi.persistentsearchview.PersistentSearchView;
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchConfirmedListener;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchQueryChangeListener;
import com.paulrybitskyi.persistentsearchview.listeners.OnSuggestionChangeListener;
import com.paulrybitskyi.persistentsearchview.model.Suggestion;
import com.paulrybitskyi.persistentsearchview.utils.SuggestionCreationUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDiscoverFragment extends Fragment {

    private MovieDiscoverViewModel mViewModel;
    private MovieDiscoverFragmentBinding binding;
    private GridItemDecoration movieItemDecoration;
    private DividerItemDecoration tvItemDecoration;
    private MovieAdapter movieAdapter;
    private MediaTVAdapter mediaTVAdapter;
    private String mQuery;
    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    private int page = 1;


    public static MovieDiscoverFragment newInstance() {
        return new MovieDiscoverFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = MovieDiscoverFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MovieDiscoverViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(false);

        movieItemDecoration = new GridItemDecoration(16, 16, 16, 16, 2);
        tvItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);

        initMovieAdapter();
        initMovieSheets();
        filterMovieOptions();
        observeMovie();
        initSearchView();

        binding.discoverTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                resetPage();
                closeSheets();
                if (tab.getPosition() == 0) {
                    initMovieSheets();
                    initMovieAdapter();
                    observeMovie();
                    binding.discoverSearch.setQueryInputHint("Search Movies");
                } else {
                    initTVLayout();
                    initTVAdapter();
                    observeTVSeries();
                    binding.discoverSearch.setQueryInputHint("Search TV Series");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.sheetDiscover.sortSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPage();
                initMovieAdapter();
                filterMovieOptions();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_discover, menu);
        android.widget.SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.menu_discover_action_search).getActionView();
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQuery = query;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.menu_discover_action_filter) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                openSheets();
            } else {
                closeSheets();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initMovieSheets() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.sheetDiscover.sheetDiscoverMovie);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    movieSheetsExpanded(bottomSheet);
                } else {
                    movieSheetsCollapsed(bottomSheet);
                }
            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {
                movieSheetsCollapsed(bottomSheet);
            }
        });
    }

    private void movieSheetsExpanded(View bottomSheet) {
        bottomSheet.setBackgroundResource(R.drawable.custom_bottom_sheet_background_state);
        binding.sheetDiscover.sheetDiscoverCloseBtn.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_bottom_sheet_background_state, null));
        binding.sheetDiscover.sheetDiscoverCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        binding.discoverAppbar.setVisibility(View.INVISIBLE);
        binding.sheetDiscover.sheetDiscoverCustomToolbar.setVisibility(View.VISIBLE);
        binding.sheetDiscover.sheetDiscoverMovieTitle.setVisibility(View.GONE);
    }

    private void movieSheetsCollapsed(View bottomSheet) {
        bottomSheet.setBackgroundResource(R.drawable.sheet_background);
        binding.discoverAppbar.setVisibility(View.VISIBLE);
        binding.sheetDiscover.sheetDiscoverCustomToolbar.setVisibility(View.GONE);
        binding.sheetDiscover.sheetDiscoverMovieTitle.setVisibility(View.VISIBLE);
    }

    private void openSheets() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    private void closeSheets() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void filterMovieOptions() {
        String withGenres = "";
        String sortBy;
        boolean includeAdult = false;

        if (binding.sheetDiscover.sortTypeSpinner.getSelectedItem().toString().toLowerCase().equals("ascending")) {
            if (binding.sheetDiscover.sheetDiscoverMovieSort.getCheckedRadioButtonId() == R.id.sort_by_popularity) {
                sortBy = "popularity.asc";
            } else if (binding.sheetDiscover.sheetDiscoverMovieSort.getCheckedRadioButtonId() == R.id.sort_by_date) {
                sortBy = "primary_release_date.asc";
            } else if (binding.sheetDiscover.sheetDiscoverMovieSort.getCheckedRadioButtonId() == R.id.sort_by_vote_avg) {
                sortBy = "vote_average.asc";
            } else {
                sortBy = "original_title.asc";
            }
        } else {
            if (binding.sheetDiscover.sheetDiscoverMovieSort.getCheckedRadioButtonId() == R.id.sort_by_popularity) {
                sortBy = "popularity.desc";
            } else if (binding.sheetDiscover.sheetDiscoverMovieSort.getCheckedRadioButtonId() == R.id.sort_by_date) {
                sortBy = "primary_release_date.desc";
            } else if (binding.sheetDiscover.sheetDiscoverMovieSort.getCheckedRadioButtonId() == R.id.sort_by_vote_avg) {
                sortBy = "vote_average.desc";
            } else {
                sortBy = "original_title.desc";
            }
        }

        if (binding.sheetDiscover.sortAdult.isChecked()) {
            includeAdult = true;
        }

        if (getSelectedGenres().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < getSelectedGenres().size(); i++) {
                if (i != getSelectedGenres().size() - 1) {
                    stringBuilder.append(getSelectedGenres().get(i) + ",");
                } else {
                    stringBuilder.append(getSelectedGenres().get(i));
                }
            }
            withGenres = stringBuilder.toString();
            Log.d("FILTER GENRE HEHE", "filterMovieOptions: " + withGenres);
        }
        Map<String, String> queries = new HashMap<>();
        queries.put("sort_by", sortBy);
        queries.put("vote_average.gte", String.valueOf(6));
        queries.put("vote_count.gte", String.valueOf(50));
        queries.put("with_genres", withGenres);
        queries.put("include_adult", String.valueOf(includeAdult));
        setQueries(queries);

        closeSheets();
    }

    private List<Integer> getSelectedGenres() {
        List<Integer> list = new ArrayList<>();
        List<Genre> genreList = Constants.movieGenres;

        if (binding.sheetDiscover.sortGenreChipGroup.getCheckedChipIds().size() > 0) {
            for (int i = 0; i < genreList.size(); i++) {
                Chip chip = (Chip) binding.sheetDiscover.sortGenreChipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    list.add(genreList.get(i).getId());
                }
            }
        }
        return list;
    }

    private void observeMovie() {
        mViewModel.discoverMovies(page).observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null && movies.size() > 0) {
                    if (isFirstPage()) {
                        movieAdapter.setList(movies);
                    } else {
                        movieAdapter.addData(movies);
                    }

                    if (page == 5) {
                        movieAdapter.getLoadMoreModule().loadMoreEnd();
                    } else {
                        movieAdapter.getLoadMoreModule().loadMoreComplete();
                        movieAdapter.getLoadMoreModule().setEnableLoadMore(true);
                    }
                }
            }
        });
    }

    private void searchMovie() {
        mViewModel.searchMovies(page, mQuery).observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null && movies.size() > 0) {
                    binding.discoverSearch.setProgressBarEnabled(false);
                    movieAdapter.setList(movies);
                    //resetPage();
                }
            }
        });
    }

    private void setQueries(Map<String, String> map) {
        mViewModel.setQueries(map);
    }

    private void observeTVSeries() {
        mViewModel.discoverTVSeries().observe(getViewLifecycleOwner(), new Observer<List<MediaTV>>() {
            @Override
            public void onChanged(List<MediaTV> mediaTVS) {
                if (mediaTVS != null && mediaTVS.size() > 0) {
                    mediaTVAdapter.setList(mediaTVS);
                } else {
                    mediaTVAdapter.setUseEmpty(true);
                }
            }
        });
    }

    private void initMovieAdapter() {
        movieAdapter = new MovieAdapter(R.layout.item_discover_movie);
        initMovieLayout();
        movieAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                Movie movie = (Movie) adapter.getData().get(position);
                startActivity(new Intent(getContext(), MovieDetailsActivity.class).putExtra(Constants.MOVIE_ID_KEY, movie.getId()));
            }
        });
        movieAdapter.setDiffCallback(new DiffUtil.ItemCallback<Movie>() {
            @Override
            public boolean areItemsTheSame(@NonNull @NotNull Movie oldItem, @NonNull @NotNull Movie newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull @NotNull Movie oldItem, @NonNull @NotNull Movie newItem) {
                return oldItem.getId().equals(newItem.getId()) && oldItem.getOriginalTitle().equals(newItem.getOriginalTitle());
            }
        });
        movieAdapter.getLoadMoreModule().setAutoLoadMore(false);
        movieAdapter.getLoadMoreModule().setEnableLoadMore(false);
        movieAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                nextPage();
                observeMovie();
            }
        });
    }

    private void initTVAdapter() {
        mediaTVAdapter = new MediaTVAdapter(R.layout.item_discover_mediatv);
        initTVLayout();
        mediaTVAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                MediaTV mediaTV = (MediaTV) adapter.getData().get(position);
                startActivity(new Intent(getContext(), MediaTVDetailsActivity.class).putExtra(Constants.MOVIE_ID_KEY, mediaTV.getId()));
            }
        });
    }

    private void initMovieLayout() {
        removeItemDecoration(movieItemDecoration);
        removeItemDecoration(tvItemDecoration);
        binding.discoverRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.discoverRecyclerview.setHasFixedSize(true);
        binding.discoverRecyclerview.addItemDecoration(movieItemDecoration);
        binding.discoverRecyclerview.setAdapter(movieAdapter);
    }

    private void initTVLayout() {
        removeItemDecoration(movieItemDecoration);
        removeItemDecoration(tvItemDecoration);
        tvItemDecoration.setDrawable(getResources().getDrawable(R.drawable.custom_divider));
        binding.discoverRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.discoverRecyclerview.setHasFixedSize(true);
        binding.discoverRecyclerview.addItemDecoration(tvItemDecoration);
        binding.discoverRecyclerview.setAdapter(mediaTVAdapter);
    }

    private void initSearchView() {
        binding.discoverSearch.setDimBackground(false);
        binding.discoverSearch.setVoiceInputButtonEnabled(false);
        binding.discoverSearch.setQueryInputHint("Search Movies");
        binding.discoverSearch.setOnLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        binding.discoverSearch.setOnSearchQueryChangeListener(new OnSearchQueryChangeListener() {
            @Override
            public void onSearchQueryChanged(PersistentSearchView searchView, String oldQuery, String newQuery) {
                //setSearchQueries(newQuery);
                mQuery = newQuery;
                setSearchSuggestions(newQuery);
            }
        });

        binding.discoverSearch.setOnSearchConfirmedListener(new OnSearchConfirmedListener() {
            @Override
            public void onSearchConfirmed(PersistentSearchView searchView, String query) {
                binding.discoverTabs.setVisibility(View.GONE);
                mQuery = query;
                searchMovie();
                binding.discoverSearch.collapse();
                binding.discoverSearch.hideRightButton();
            }
        });

        binding.discoverSearch.setOnClearInputBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.discoverSearch.showRightButton();
                binding.discoverTabs.setVisibility(View.VISIBLE);
                movieAdapter.setList(null);
                filterMovieOptions();
                observeMovie();
            }
        });

        binding.discoverSearch.setOnSuggestionChangeListener(new OnSuggestionChangeListener() {
            @Override
            public void onSuggestionPicked(SuggestionItem suggestion) {
                Suggestion item = suggestion.getItemModel();
                mQuery = item.getText();
                searchMovie();
            }

            @Override
            public void onSuggestionRemoved(SuggestionItem suggestion) {

            }
        });

        binding.discoverSearch.setRightButtonDrawable(R.drawable.ic_baseline_filter);
        binding.discoverSearch.showRightButton();
        binding.discoverSearch.setOnRightBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    openSheets();
                } else {
                    closeSheets();
                }
            }
        });

    }

    private void setSearchSuggestions(String query) {
        mViewModel.searchMovies(page, query).observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> list) {
                if (list != null && list.size() > 0){
                    List<String> suggestionList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getTitle() == null) {
                            suggestionList.add(list.get(i).getOriginalTitle());
                        } else {
                            suggestionList.add(list.get(i).getTitle());
                        }
                    }
                    binding.discoverSearch.setSuggestions(SuggestionCreationUtil.asRegularSearchSuggestions(suggestionList));
                }
            }
        });
    }

    private void removeItemDecoration(RecyclerView.ItemDecoration decoration) {
        binding.discoverRecyclerview.removeItemDecoration(decoration);
    }

    private void nextPage() {
        page++;
    }

    private void resetPage() {
        page = 1;
    }

    private boolean isFirstPage() {
        return page == 1;
    }

}