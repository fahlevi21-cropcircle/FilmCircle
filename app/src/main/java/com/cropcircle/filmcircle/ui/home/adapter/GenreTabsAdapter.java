package com.cropcircle.filmcircle.ui.home.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cropcircle.filmcircle.ui.home.sub.GenreFragment;

public class GenreTabsAdapter extends FragmentStateAdapter {

    public GenreTabsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        GenreFragment fragment = new GenreFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("genre_position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 7;
    }
}
