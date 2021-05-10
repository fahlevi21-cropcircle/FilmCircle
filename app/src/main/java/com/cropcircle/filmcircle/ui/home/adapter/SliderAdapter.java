package com.cropcircle.filmcircle.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ItemCardBannerBinding;
import com.cropcircle.filmcircle.databinding.ItemSliderBinding;
import com.cropcircle.filmcircle.models.allmedia.Result;
import com.cropcircle.filmcircle.models.movie.Movie;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends BaseBannerAdapter<Result> {

    @Override
    protected void bindData(BaseViewHolder<Result> holder, Result data, int position, int pageSize) {
        ItemSliderBinding binding = DataBindingUtil.bind(holder.itemView);
        binding.setData(data);
        binding.executePendingBindings();
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_slider;
    }

}
