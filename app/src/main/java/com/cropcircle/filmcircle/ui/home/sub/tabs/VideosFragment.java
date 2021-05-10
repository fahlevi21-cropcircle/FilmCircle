package com.cropcircle.filmcircle.ui.home.sub.tabs;

import android.content.Intent;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.FragmentVideosBinding;
import com.cropcircle.filmcircle.models.movie.Video;
import com.cropcircle.filmcircle.ui.VideoPlayerActivity;
import com.cropcircle.filmcircle.ui.home.adapter.VideoAdapter;
import com.cropcircle.filmcircle.ui.home.itemdecoration.HorizontalItemDecoration;
import com.cropcircle.filmcircle.ui.home.itemdecoration.VerticalItemDecoration;
import com.cropcircle.filmcircle.ui.home.sub.MovieDetailsViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideosFragment extends Fragment implements OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VideosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideosFragment newInstance(String param1, String param2) {
        VideosFragment fragment = new VideosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentVideosBinding binding;
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
        binding = FragmentVideosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VideoAdapter adapter = new VideoAdapter(R.layout.item_video_linear);
        binding.videosRecyclerview.setAdapter(adapter);
        binding.videosRecyclerview.setHasFixedSize(true);
        binding.videosRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.videosRecyclerview.addItemDecoration(new VerticalItemDecoration(8,8,8,8));
        adapter.setOnItemClickListener(this);

        viewModel.getVideos().observe(getViewLifecycleOwner(), new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                if (videos.size() >= 5){
                    for (int i = 0; i < 5; i++ ){
                        adapter.addData(videos.get(i));
                    }
                }else {
                    adapter.addData(videos);
                }
            }
        });
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Video data = (Video) adapter.getItem(position);
        Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
        intent.putExtra(Constants.VIDEO_ID_KEY, data.getKey());
        startActivity(intent);
    }

    private void errorShowEmptyItem(){
        binding.videosRecyclerview.setVisibility(View.GONE);
        binding.layoutEmpty.getRoot().setVisibility(View.VISIBLE);
        String title = "No Video Available";
        String message = "review this movie";
        int icon = R.drawable.empty_list;

        binding.layoutEmpty.errorImage.setImageResource(icon);
        binding.layoutEmpty.errorMessage.setVisibility(View.GONE);
        binding.layoutEmpty.errorTitle.setText(title);
        binding.layoutEmpty.errorAction.setVisibility(View.GONE);

        binding.layoutEmpty.errorAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "This features will be implemented soon!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void hideErrorPage(){
        binding.videosRecyclerview.setVisibility(View.VISIBLE);
        binding.layoutEmpty.getRoot().setVisibility(View.GONE);
    }
}