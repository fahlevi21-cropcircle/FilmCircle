package com.cropcircle.filmcircle.ui.home.adapter;

import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ItemBigLinearBinding;
import com.cropcircle.filmcircle.databinding.ItemCardBannerBinding;
import com.cropcircle.filmcircle.databinding.ItemImageSmallBinding;
import com.cropcircle.filmcircle.databinding.ItemSmallGridBinding;
import com.cropcircle.filmcircle.databinding.ItemSmallLinearBinding;
import com.cropcircle.filmcircle.models.movie.Movie;

import org.jetbrains.annotations.NotNull;

public class MovieAdapter extends BaseQuickAdapter<Movie, BaseDataBindingHolder> {
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
        }
    }
}
