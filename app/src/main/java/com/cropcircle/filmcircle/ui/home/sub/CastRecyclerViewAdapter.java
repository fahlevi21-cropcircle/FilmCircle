package com.cropcircle.filmcircle.ui.home.sub;

import android.graphics.drawable.Drawable;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ItemCardBannerBinding;
import com.cropcircle.filmcircle.databinding.ItemCasterBinding;
import com.cropcircle.filmcircle.models.people.Cast;

import org.jetbrains.annotations.NotNull;

public class CastRecyclerViewAdapter extends BaseQuickAdapter<Cast, BaseDataBindingHolder> {
    int layoutRes;
    public CastRecyclerViewAdapter(int layoutResId) {
        super(layoutResId);
        layoutRes = layoutResId;
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder baseDataBindingHolder, Cast cast) {
        if (layoutRes == R.layout.item_caster){
            ItemCasterBinding binding = (ItemCasterBinding) baseDataBindingHolder.getDataBinding();

            if (cast.getProfilePath() != null){
                Glide.with(binding.itemCastAvatar).asBitmap().load(
                        Constants.IMG_PROFILE_180 + cast.getProfilePath()
                ).fitCenter().into(binding.itemCastAvatar);
            }else {
                Glide.with(binding.itemCastAvatar).asBitmap().load(
                        R.drawable.ic_baseline_person
                ).fitCenter().into(binding.itemCastAvatar);
            }

            binding.setData(cast);
            binding.executePendingBindings();
        }
    }
}
