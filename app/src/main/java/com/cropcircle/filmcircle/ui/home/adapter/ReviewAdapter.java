package com.cropcircle.filmcircle.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ItemReviewBinding;
import com.cropcircle.filmcircle.databinding.ItemVideoLargeBinding;
import com.cropcircle.filmcircle.models.movie.Video;
import com.cropcircle.filmcircle.models.review.Review;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    List<Review> list = new ArrayList<>();

    public void setList(List<Review> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addData(Review review){
        list.add(review);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemReviewBinding binding = ItemReviewBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        private ItemReviewBinding binding;
        public ViewHolder(@NonNull ItemReviewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(Review video){
            binding.setData(video);
            if (video != null && video.getAuthorDetails().getRating() != null){
                binding.itemReviewRate.setStar(video.getAuthorDetails().getRating().floatValue() - 5.0f);
                if (video.getCreatedAt() != null){
                    Constants constants = Constants.getInstance();
                    String dateCreated = "on " + constants.simpleDateFormatter(video.getCreatedAt());
                    binding.itemReviewDate.setText(dateCreated);
                }else {
                    binding.itemReviewDate.setText("no date");
                }
            }
            binding.executePendingBindings();

            binding.itemReviewContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (binding.itemReviewContent.getMaxLines() == 3){
                        binding.itemReviewContent.setMaxLines(50);
                    }else {
                        binding.itemReviewContent.setMaxLines(3);
                    }
                }
            });
        }
    }
}
