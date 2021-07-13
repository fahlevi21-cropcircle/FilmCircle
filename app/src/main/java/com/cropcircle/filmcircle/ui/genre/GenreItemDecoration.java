package com.cropcircle.filmcircle.ui.genre;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class GenreItemDecoration extends RecyclerView.ItemDecoration {
    private int left,right,top,bottom;
    private int paddingSize;

    public GenreItemDecoration(int left, int right, int top, int bottom, int paddingSize) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.paddingSize = paddingSize;
    }

    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.top = top;
        outRect.bottom = bottom;
        outRect.left = left;
        outRect.right = right;

        if (parent.getChildAdapterPosition(view) % 3 == 0){
            outRect.left = 32;
        }else if (parent.getChildAdapterPosition(view) % 3 == 2){
            outRect.right = 32;
        }
    }
}
