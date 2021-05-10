package com.cropcircle.filmcircle.ui.home.sub.tabs;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.FragmentMoreBinding;
import com.cropcircle.filmcircle.models.movie.Movie;
import com.cropcircle.filmcircle.ui.home.adapter.MovieAdapter;
import com.cropcircle.filmcircle.ui.home.itemdecoration.GridItemDecoration;
import com.cropcircle.filmcircle.ui.home.sub.MovieDetailsActivity;
import com.cropcircle.filmcircle.ui.home.sub.MovieDetailsViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment implements OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private MovieDetailsViewModel viewModel;
    private FragmentMoreBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        viewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);
        viewModel.setMovieId(getArguments().getInt(Constants.MOVIE_ID_KEY));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSimilar();
        setupRecommendation();
    }

    private void setupSimilar() {
        MovieAdapter adapter = new MovieAdapter(R.layout.item_small_grid);
        binding.movieDetailsSimilar.setLayoutManager(new
                GridLayoutManager(getContext(), 3));
        binding.movieDetailsSimilar.setAdapter(adapter);
        binding.movieDetailsSimilar.addItemDecoration(new GridItemDecoration(8,8,8,8));
        adapter.setOnItemClickListener(this);

        viewModel.getSimilar().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies.size() >= 3) {
                    for (int i = 0; i < 3; i++) {
                        adapter.addData(movies.get(i));
                    }
                } else {
                    adapter.addData(movies);
                }
            }
        });
    }

    private void setupRecommendation() {
        MovieAdapter adapter = new MovieAdapter(R.layout.item_small_grid);
        binding.movieDetailsRecommended.setLayoutManager(new
                GridLayoutManager(getContext(), 3));
        binding.movieDetailsRecommended.setAdapter(adapter);
        binding.movieDetailsRecommended.addItemDecoration(new GridItemDecoration(8,8,8,8));
        adapter.setOnItemClickListener(this);

        viewModel.getRecommended().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies.size() >= 3) {
                    for (int i = 0; i < 3; i++) {
                        adapter.addData(movies.get(i));
                    }
                } else {
                    adapter.addData(movies);
                }
            }
        });
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Movie movie = (Movie) adapter.getData().get(position);
        Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
        intent.putExtra(Constants.MOVIE_ID_KEY, movie.getId());
        startActivity(intent);
    }
}