package com.cropcircle.filmcircle.ui.home.itemdecoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    int left;
    int right;
    int top;
    int bottom;

    public GridItemDecoration(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = bottom;
        outRect.top = top;
        outRect.left = left;
        outRect.right = right;

        if (parent.getChildAdapterPosition(view) % 2 == 0){
            outRect.left = 24;
        }else {
            outRect.right = 24;
        }
    }
}
