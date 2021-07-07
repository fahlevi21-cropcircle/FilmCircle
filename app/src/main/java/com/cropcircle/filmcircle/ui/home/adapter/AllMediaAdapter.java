package com.cropcircle.filmcircle.ui.home.adapter;

import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ItemBigLinearBinding;
import com.cropcircle.filmcircle.models.allmedia.Result;

import org.jetbrains.annotations.NotNull;

public class AllMediaAdapter extends BaseQuickAdapter<Result, BaseDataBindingHolder> {
    private int layoutType;

    public AllMediaAdapter(int layoutResId) {
        super(layoutResId);
        layoutType = layoutResId;
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseDataBindingHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder baseDataBindingHolder, Result result) {
        if (layoutType == R.layout.item_big_linear){
            ItemBigLinearBinding binding = (ItemBigLinearBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(result);
            binding.itemTrendingVoteAvg.setStar(result.getVoteAverage().floatValue() - 5.0f);
            binding.executePendingBindings();

        }
    }
}
