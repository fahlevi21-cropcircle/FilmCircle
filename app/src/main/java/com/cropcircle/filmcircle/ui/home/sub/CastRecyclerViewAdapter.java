package com.cropcircle.filmcircle.ui.home.sub;

import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ItemCasterBinding;
import com.cropcircle.filmcircle.databinding.ItemLatestActorsBinding;
import com.cropcircle.filmcircle.databinding.ItemPopularActorsBinding;
import com.cropcircle.filmcircle.models.people.Actors;

import org.jetbrains.annotations.NotNull;

public class CastRecyclerViewAdapter extends BaseQuickAdapter<Actors, BaseDataBindingHolder> implements LoadMoreModule {
    int layoutRes;
    public CastRecyclerViewAdapter(int layoutResId) {
        super(layoutResId);
        layoutRes = layoutResId;
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder baseDataBindingHolder, Actors actors) {
        if (layoutRes == R.layout.item_caster){
            ItemCasterBinding binding = (ItemCasterBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(actors);
            binding.executePendingBindings();
        }else if (layoutRes == R.layout.item_popular_actors){
            ItemPopularActorsBinding binding = (ItemPopularActorsBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(actors);

            if (actors.getProfilePath() != null){
                Glide.with(binding.itemPopularActorImage)
                        .asDrawable()
                        .placeholder(binding.itemPopularActorImage.getDrawable())
                        .error(R.drawable.ic_baseline_person)
                        .load(Constants.IMG_PROFILE_180 + actors.getProfilePath())
                        .fitCenter()
                        .into(binding.itemPopularActorImage);
            }else {
                Glide.with(binding.itemPopularActorImage)
                        .asDrawable()
                        .placeholder(binding.itemPopularActorImage.getDrawable())
                        .error(R.drawable.ic_baseline_person)
                        .load(R.drawable.ic_baseline_person)
                        .fitCenter()
                        .into(binding.itemPopularActorImage);
            }
            binding.executePendingBindings();
        }else if (layoutRes == R.layout.item_latest_actors){
            ItemLatestActorsBinding binding = (ItemLatestActorsBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(actors);

            if (actors.getProfilePath() != null){
                Glide.with(binding.itemLatestActorImage)
                        .asDrawable()
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.ic_baseline_person)
                        .load(Constants.IMG_PROFILE_180 + actors.getProfilePath())
                        .fitCenter()
                        .into(binding.itemLatestActorImage);
            }
            for (int i = 0; i < actors.getKnownFor().size(); i++){
                if (actors.getKnownFor().get(i).getTitle() != null){
                    binding.itemLatestActorKnownFor.append(actors.getKnownFor().get(i).getTitle());
                }else {
                    binding.itemLatestActorKnownFor.append(actors.getKnownFor().get(i).getName());
                }

                if (i != actors.getKnownFor().size() - 1){
                    binding.itemLatestActorKnownFor.append(", ");
                }
            }

            binding.itemLatestActorKnownFor.setEllipsize(TextUtils.TruncateAt.END);
            binding.executePendingBindings();
        }
    }
}
