package com.cropcircle.filmcircle.ui.home.adapter;

import androidx.databinding.DataBindingUtil;

import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ItemCardBannerBinding;
import com.cropcircle.filmcircle.models.movie.Movie;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

public class CardSliderAdapter extends BaseBannerAdapter<Movie> {

    @Override
    protected void bindData(BaseViewHolder<Movie> holder, Movie data, int position, int pageSize) {
        ItemCardBannerBinding binding = DataBindingUtil.bind(holder.itemView);
        binding.setData(data);
        binding.executePendingBindings();
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_card_banner;
    }
}
