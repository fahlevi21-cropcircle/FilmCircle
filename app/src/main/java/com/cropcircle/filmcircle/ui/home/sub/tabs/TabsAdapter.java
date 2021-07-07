package com.cropcircle.filmcircle.ui.home.sub.tabs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cropcircle.filmcircle.Constants;

public class TabsAdapter extends FragmentStateAdapter {

    int movieId;

    public TabsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position == 0) {
            fragment = new BasicFragment();
        } else if (position == 1) {
            fragment = new ImagesFragment();
        } else if (position == 2) {
            fragment = new VideosFragment();
        } else {
            fragment = new ReviewsFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.MOVIE_ID_KEY, movieId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
