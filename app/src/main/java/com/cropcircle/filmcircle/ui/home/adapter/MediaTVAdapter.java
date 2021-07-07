package com.cropcircle.filmcircle.ui.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ItemSmallVerticalTvBinding;
import com.cropcircle.filmcircle.models.tv.MediaTV;

import org.jetbrains.annotations.NotNull;

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
        }
    }
}
