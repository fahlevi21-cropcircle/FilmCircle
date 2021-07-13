package com.cropcircle.filmcircle.ui.home.adapter;

import android.media.tv.TvContract;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ItemBigLinearBinding;
import com.cropcircle.filmcircle.databinding.ItemCardBannerBinding;
import com.cropcircle.filmcircle.databinding.ItemFavoriteMovieBinding;
import com.cropcircle.filmcircle.databinding.ItemGenreGridBinding;
import com.cropcircle.filmcircle.databinding.ItemImageSmallBinding;
import com.cropcircle.filmcircle.databinding.ItemSmallGridBinding;
import com.cropcircle.filmcircle.databinding.ItemSmallLinearBinding;
import com.cropcircle.filmcircle.databinding.ItemSmallVerticalMovieBinding;
import com.cropcircle.filmcircle.models.movie.Genre;
import com.cropcircle.filmcircle.models.movie.Movie;
import com.google.android.exoplayer2.C;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends BaseQuickAdapter<Movie, BaseDataBindingHolder> implements LoadMoreModule {
    private int viewType;

    public MovieAdapter(int layoutResId) {
        super(layoutResId);
        viewType = layoutResId;
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseDataBindingHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder baseDataBindingHolder, Movie movie) {
        if (viewType == R.layout.item_small_linear){
            ItemSmallLinearBinding binding = (ItemSmallLinearBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(movie);
            if (movie.getVoteAverage() == 0.0){
                binding.itemNewReleaseRating.setText(" unrated ");
            }else {
                binding.itemNewReleaseRating.setText(movie.getVoteAverage().toString());
            }
            binding.executePendingBindings();
        }else if (viewType == R.layout.item_small_grid){
            ItemSmallGridBinding binding = (ItemSmallGridBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(movie);
            binding.executePendingBindings();
        }else if (viewType == R.layout.item_card_banner){
            ItemCardBannerBinding binding = (ItemCardBannerBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(movie);
            binding.executePendingBindings();
        }else if (viewType == R.layout.item_image_small){
            ItemImageSmallBinding binding = (ItemImageSmallBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(movie);
            binding.executePendingBindings();
        }else if (viewType == R.layout.item_small_vertical_movie){
            ItemSmallVerticalMovieBinding binding = (ItemSmallVerticalMovieBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(movie);
            List<Genre> genres = Constants.movieGenres;
            List<Genre> selectedGenres = new ArrayList<>();
            if (movie.getGenreIds() != null && movie.getGenreIds().size() > 0){
                for (Genre g : genres){
                    if (movie.getGenreIds().contains(g.getId())){
                        selectedGenres.add(g);
                    }
                }
            }

            for (int i = 0; i < selectedGenres.size(); i++){
                binding.itemSmallVerticalGenres.append(selectedGenres.get(i).getName());
                if (i != selectedGenres.size() - 1){
                    binding.itemSmallVerticalGenres.append(", ");
                }else {
                    binding.itemSmallVerticalGenres.append(" ");
                }
            }

            binding.executePendingBindings();
        }else if(viewType == R.layout.item_favorite_movie){
            ItemFavoriteMovieBinding binding = (ItemFavoriteMovieBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(movie);
            Constants constants = new Constants();
            binding.itemFavoriteDate.setText(constants.simpleDateFormatter(movie.getReleaseDate()));
            binding.executePendingBindings();
        }else if (viewType == R.layout.item_genre_grid){
            ItemGenreGridBinding binding = (ItemGenreGridBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(movie);
            binding.executePendingBindings();
        }
    }
}
