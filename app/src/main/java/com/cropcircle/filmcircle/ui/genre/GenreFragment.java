package com.cropcircle.filmcircle.ui.genre;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.GenreFragmentBinding;
import com.cropcircle.filmcircle.models.movie.Movie;
import com.cropcircle.filmcircle.ui.home.adapter.MovieAdapter;
import com.cropcircle.filmcircle.ui.home.sub.MovieDetailsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GenreFragment extends Fragment {

    private GenreViewModel mViewModel;
    private GenreFragmentBinding binding;
    private MovieAdapter adapter;

    //for paging
    private int page = 1;

    private static final String ARG_GENRE_ID = "genreId";
    private static final String ARG_SORT = "sortBy";

    private int mGenreIdParam;
    private String mSortParam;

    public static GenreFragment newInstance(int genreId) {
        GenreFragment fragment = new GenreFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GENRE_ID, genreId);
        //args.putString(ARG_SORT, sortBy);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(GenreViewModel.class);

        if (getArguments() != null) {
            if (mGenreIdParam != getArguments().getInt(ARG_GENRE_ID)){
                resetPage();
            }
            mGenreIdParam = getArguments().getInt(ARG_GENRE_ID);
            mViewModel.setGenreId(mGenreIdParam);
            //mSortParam = getArguments().getString(ARG_SORT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = GenreFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        adapter = new MovieAdapter(R.layout.item_genre_grid);

        adapter.getLoadMoreModule().setAutoLoadMore(false);
        adapter.setDiffCallback(new DiffUtil.ItemCallback<Movie>() {
            @Override
            public boolean areItemsTheSame(@NonNull @NotNull Movie oldItem, @NonNull @NotNull Movie newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull @NotNull Movie oldItem, @NonNull @NotNull Movie newItem) {
                return oldItem.getId().equals(newItem.getId()) && oldItem.getTitle().equals(newItem.getTitle());
            }
        });
        adapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                nextPage();
                observe();
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                Movie movie = (Movie) adapter.getData().get(position);
                startActivity(new Intent(getContext(), MovieDetailsActivity.class).putExtra(Constants.MOVIE_ID_KEY, movie.getId()));
            }
        });

        binding.genreRc.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.genreRc.setHasFixedSize(true);
        binding.genreRc.addItemDecoration(new GenreItemDecoration(10, 10, 16, 16, 16));
        binding.genreRc.setAdapter(adapter);

        observe();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void observe() {
        adapter.getLoadMoreModule().setEnableLoadMore(false);
        adapter.getLoadMoreModule().isLoading();
        mViewModel.getMovies(page).observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null && movies.size() > 0) {
                    if (isFirstPage()) {
                        adapter.setList(movies);
                    } else {
                        adapter.addData(movies);
                    }
                    adapter.getLoadMoreModule().loadMoreComplete();
                    adapter.getLoadMoreModule().setEnableLoadMore(true);
                }
            }
        });
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