package com.cropcircle.filmcircle.ui.home.sub.tabs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.FragmentImagesBinding;
import com.cropcircle.filmcircle.models.movie.Backdrop;
import com.cropcircle.filmcircle.models.movie.MovieDetails;
import com.cropcircle.filmcircle.ui.home.adapter.ImageAdapter;
import com.cropcircle.filmcircle.ui.home.itemdecoration.GridItemDecoration;
import com.cropcircle.filmcircle.ui.home.sub.MovieDetailsViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImagesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ImagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImagesFragment newInstance(String param1, String param2) {
        ImagesFragment fragment = new ImagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentImagesBinding binding;
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
        binding = FragmentImagesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageAdapter adapter = new ImageAdapter(R.layout.item_image_medium);
        binding.imagesRecyclerview.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2, GridLayoutManager.VERTICAL, false);
        binding.imagesRecyclerview.setLayoutManager(layoutManager);
        binding.imagesRecyclerview.addItemDecoration(new GridItemDecoration(4,4,4,4));
        binding.imagesRecyclerview.setHasFixedSize(true);

        viewModel.getImages().observe(getViewLifecycleOwner(), new Observer<List<Backdrop>>() {
            @Override
            public void onChanged(List<Backdrop> backdrops) {
                if (backdrops.size() >= 6){
                    for (int i = 0; i < 6; i++){
                        adapter.addData(backdrops.get(i));
                    }
                }else {
                    adapter.addData(backdrops);
                }
            }
        });
    }
}