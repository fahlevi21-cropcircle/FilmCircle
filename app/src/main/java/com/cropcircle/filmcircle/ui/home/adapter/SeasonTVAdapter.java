package com.cropcircle.filmcircle.ui.home.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ItemMediatvSeasonBinding;
import com.cropcircle.filmcircle.models.tv.Season;

import org.jetbrains.annotations.NotNull;

public class SeasonTVAdapter extends BaseQuickAdapter<Season, BaseDataBindingHolder> {
    private int layoutId;

    public SeasonTVAdapter(int layoutResId) {
        super(layoutResId);
        this.layoutId = layoutResId;
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder baseDataBindingHolder, Season season) {
        if (layoutId == R.layout.item_mediatv_season) {
            ItemMediatvSeasonBinding binding = (ItemMediatvSeasonBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(season);
            String premiereDateText = "Premiered on " + season.getAirDate();
            String episodeText = "Total of " + season.getEpisodeCount() + " episodes";
            binding.itemSeasonDate.setText(premiereDateText);
            binding.itemSeasonEpisode.setText(episodeText);
            /*if (season.getOverview() != null) {
                binding.itemSeasonOverview.setVisibility(View.VISIBLE);
            }*/
            binding.executePendingBindings();
        }
    }
}
