package com.cropcircle.filmcircle.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ItemVideoLargeBinding;
import com.cropcircle.filmcircle.databinding.ItemVideoLinearBinding;
import com.cropcircle.filmcircle.models.movie.Video;
import com.zhpan.bannerview.BaseBannerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends BaseQuickAdapter<Video, BaseDataBindingHolder> {
    int viewType;

    public VideoAdapter(int layoutResId) {
        super(layoutResId);
        viewType = layoutResId;
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder baseDataBindingHolder, Video video) {
        if (viewType == R.layout.item_video_linear){
            ItemVideoLinearBinding binding = (ItemVideoLinearBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(video);
            binding.executePendingBindings();
        }
    }
}
