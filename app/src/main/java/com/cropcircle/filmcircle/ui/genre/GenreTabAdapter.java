package com.cropcircle.filmcircle.ui.genre;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cropcircle.filmcircle.models.movie.Genre;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GenreTabAdapter extends FragmentStateAdapter {
    List<Genre> genreList;
    public GenreTabAdapter(@NonNull @NotNull Fragment fragment, List<Genre> genres) {
        super(fragment);
        this.genreList = genres;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return GenreFragment.newInstance(genreList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }
}
