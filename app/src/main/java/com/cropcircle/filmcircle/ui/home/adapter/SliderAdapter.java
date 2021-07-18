package com.cropcircle.filmcircle.ui.home.adapter;

import androidx.databinding.DataBindingUtil;

import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ItemSliderBinding;
import com.cropcircle.filmcircle.models.allmedia.AllMedia;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

public class SliderAdapter extends BaseBannerAdapter<AllMedia> {

    @Override
    protected void bindData(BaseViewHolder<AllMedia> holder, AllMedia data, int position, int pageSize) {
        Constants constants = Constants.getInstance();
        ItemSliderBinding binding = DataBindingUtil.bind(holder.itemView);
        if (data.getReleaseDate() != null){
            binding.itemSliderDate.setText(constants.simpleDateFormatter(data.getReleaseDate()));
        }else {
            binding.itemSliderDate.setText(constants.simpleDateFormatter(data.getFirstAirDate()));
        }
        binding.setData(data);
        String mediaType;
        if (data.getMediaType().contains("movie")){
            mediaType = "Movie";
        }else {
            mediaType = "TV Series";
        }
        binding.itemSliderChipType.setText(mediaType);
        binding.executePendingBindings();
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_slider;
    }

}
