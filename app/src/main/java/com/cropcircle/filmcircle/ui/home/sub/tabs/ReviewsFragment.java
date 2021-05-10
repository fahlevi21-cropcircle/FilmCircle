package com.cropcircle.filmcircle.ui.home.sub.tabs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.FragmentReviewsBinding;
import com.cropcircle.filmcircle.models.movie.MovieDetails;
import com.cropcircle.filmcircle.models.review.Review;
import com.cropcircle.filmcircle.ui.home.adapter.ReviewAdapter;
import com.cropcircle.filmcircle.ui.home.itemdecoration.VerticalItemDecoration;
import com.cropcircle.filmcircle.ui.home.sub.MovieDetailsViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReviewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewsFragment newInstance(String param1, String param2) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentReviewsBinding binding;
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
        binding = FragmentReviewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ReviewAdapter adapter = new ReviewAdapter();
        binding.reviewsRc.setHasFixedSize(true);
        binding.reviewsRc.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.reviewsRc.addItemDecoration(new VerticalItemDecoration(24,24,32,32));
        binding.reviewsRc.setAdapter(adapter);

        viewModel.getReview().observe(getViewLifecycleOwner(), new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                if (reviews != null && reviews.size() > 0){
                    hideErrorPage();
                    if (reviews.size() >= 3){
                        for (int i =0; i < 3; i++){
                            adapter.addData(reviews.get(i));
                        }
                    }else {
                        adapter.setList(reviews);
                    }
                }else {
                    errorShowEmptyItem();
                }
            }
        });
    }

    private void errorShowEmptyItem(){
        binding.reviewsRc.setVisibility(View.GONE);
        binding.layoutEmpty.getRoot().setVisibility(View.VISIBLE);
        String title = "No Review Available";
        String message = "review this movie";
        int icon = R.drawable.error_docs;

        binding.layoutEmpty.errorImage.setImageResource(icon);
        binding.layoutEmpty.errorMessage.setVisibility(View.GONE);
        binding.layoutEmpty.errorTitle.setText(title);
        binding.layoutEmpty.errorAction.setText(message);

        binding.layoutEmpty.errorAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "This features will be implemented soon!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void hideErrorPage(){
        binding.reviewsRc.setVisibility(View.VISIBLE);
        binding.layoutEmpty.getRoot().setVisibility(View.GONE);
    }
}