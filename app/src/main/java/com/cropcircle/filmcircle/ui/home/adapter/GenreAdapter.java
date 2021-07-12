package com.cropcircle.filmcircle.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cropcircle.filmcircle.databinding.ItemChipBinding;
import com.cropcircle.filmcircle.models.movie.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    private List<Genre> list = new ArrayList<>();

    public void setList(List<Genre> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemChipBinding binding = ItemChipBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        ItemChipBinding binding;

        public ViewHolder(@NonNull ItemChipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Genre genre, int position){
            if (position != list.size() - 1){
                binding.itemChip.setText(genre.getName() + ", ");
                binding.executePendingBindings();
                return;
            }
            binding.itemChip.setText(genre.getName());
            binding.executePendingBindings();
        }
    }
}
