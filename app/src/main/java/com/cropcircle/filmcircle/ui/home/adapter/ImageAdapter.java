package com.cropcircle.filmcircle.ui.home.adapter;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ItemImageMediumBinding;
import com.cropcircle.filmcircle.models.movie.Backdrop;
import com.cropcircle.filmcircle.models.movie.Images;

import org.jetbrains.annotations.NotNull;

public class ImageAdapter extends BaseQuickAdapter<Backdrop, BaseDataBindingHolder> {
    private int viewType;
    public ImageAdapter(int layoutResId) {
        super(layoutResId);
        viewType = layoutResId;
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseDataBindingHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder baseDataBindingHolder, Backdrop backdrop) {
        int position = getItemPosition(backdrop);
        if (viewType == R.layout.item_image_medium){
            ItemImageMediumBinding binding = (ItemImageMediumBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(backdrop);
            binding.executePendingBindings();

            if (position % 3 == 0){
                Glide.with(binding.getRoot()).asBitmap().load(
                        Constants.BACKDROP_PATH_780 + backdrop.getFilePath()).override(1024,786).into(binding.itemImage);
            }else{
                Glide.with(binding.getRoot()).asBitmap().load(
                        Constants.BACKDROP_PATH_780 + backdrop.getFilePath()).override(800,600).into(binding.itemImage);
            }
        }
    }
}
