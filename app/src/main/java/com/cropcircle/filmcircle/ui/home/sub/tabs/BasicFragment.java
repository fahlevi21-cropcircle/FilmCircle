package com.cropcircle.filmcircle.ui.home.sub.tabs;

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

import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.databinding.FragmentBasicBinding;
import com.cropcircle.filmcircle.models.movie.MovieDetails;
import com.cropcircle.filmcircle.ui.home.adapter.GenreAdapter;
import com.cropcircle.filmcircle.ui.home.itemdecoration.GridItemDecoration;
import com.cropcircle.filmcircle.ui.home.sub.MovieDetailsViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BasicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BasicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BasicFragment newInstance(String param1, String param2) {
        BasicFragment fragment = new BasicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentBasicBinding binding;
    private MovieDetailsViewModel viewModel;

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
        binding = FragmentBasicBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GenreAdapter adapter = new GenreAdapter();
        binding.basicGenres.setAdapter(adapter);
        binding.basicGenres.setHasFixedSize(true);
        binding.basicGenres.setLayoutManager(new GridLayoutManager(
                getContext(), 3, GridLayoutManager.VERTICAL, false
        ));
        binding.basicGenres.addItemDecoration(
                new GridItemDecoration(4, 4, 8, 8)
        );

        viewModel.getDetails().observe(getViewLifecycleOwner(), new Observer<MovieDetails>() {
            @Override
            public void onChanged(MovieDetails movieDetails) {
                binding.movieDetailsOverview.setText(movieDetails.getOverview());
                binding.basicHomepage.setText(movieDetails.getHomepage());
                binding.basicRuntime.setText(String.valueOf(movieDetails.getRuntime()) + " Minutes");
                binding.basicStatus.setText(movieDetails.getStatus());
                binding.basicTagLine.setText(movieDetails.getTagline());
                binding.basicReleaseDate.setText(movieDetails.getReleaseDate());

                adapter.setList(movieDetails.getGenres());
            }
        });

    }


}