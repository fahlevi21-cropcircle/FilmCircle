package com.cropcircle.filmcircle.ui.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ItemFavoriteTvBinding;
import com.cropcircle.filmcircle.databinding.ItemSmallVerticalTvBinding;
import com.cropcircle.filmcircle.models.movie.Genre;
import com.cropcircle.filmcircle.models.tv.MediaTV;
import com.google.android.material.chip.Chip;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MediaTVAdapter extends BaseQuickAdapter<MediaTV, BaseDataBindingHolder> {
    int layoutId;

    public MediaTVAdapter(int layoutResId) {
        super(layoutResId);
        this.layoutId = layoutResId;
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder baseDataBindingHolder, MediaTV mediaTV) {
        if (layoutId == R.layout.item_small_vertical_tv){
            ItemSmallVerticalTvBinding binding = (ItemSmallVerticalTvBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(mediaTV);
            binding.itemSmallVerticalRating.setStar(mediaTV.getVoteAverage().floatValue() - 5.0f);
            binding.itemSmallVerticalDate.setText("Aired at " + mediaTV.getFirstAirDate() + " - " + mediaTV.getOriginalLanguage().toUpperCase());
            binding.executePendingBindings();
        }else if (layoutId == R.layout.item_favorite_tv){
            ItemFavoriteTvBinding binding = (ItemFavoriteTvBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(mediaTV);
            binding.itemFavoriteTvRate.setStar(mediaTV.getVoteAverage().floatValue() - 5.0f);
            float voteAvg = mediaTV.getVoteAverage().floatValue() - 5.0f;
            String mVote = voteAvg + "1";
            String detail = mVote.substring(0,3) + " (" + mediaTV.getVoteCount() + ")";
            binding.itemFavoriteTvRatingDetail.setText(detail);

            List<Genre> genres = Constants.tvGenres;
            List<Genre> selectedGenres = new ArrayList<>();
            if (mediaTV.getGenreIds() != null && mediaTV.getGenreIds().size() > 0){
                for (Genre g : genres){
                    if (mediaTV.getGenreIds().contains(g.getId())){
                        selectedGenres.add(g);
                    }
                }
            }

            for (int i = 0; i < selectedGenres.size(); i++){
                Chip chip = new Chip(this.getContext());
                chip.setText(selectedGenres.get(i).getName());

                binding.itemFavoriteTvParentChip.addView(chip);
            }
            binding.executePendingBindings();
        }
    }
}
