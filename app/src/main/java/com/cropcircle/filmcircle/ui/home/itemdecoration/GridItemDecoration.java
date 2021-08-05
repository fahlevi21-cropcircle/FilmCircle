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
    int spanSize;

    public GridItemDecoration(int left, int right, int top, int bottom, int spanSize) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.spanSize = spanSize;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = bottom;
        outRect.top = top;
        outRect.left = left;
        outRect.right = right;

        if (spanSize == 2){
            if (parent.getChildAdapterPosition(view) == 0){
                outRect.top = 56;
            }else if (parent.getChildAdapterPosition(view) == 1){
                outRect.top = 56;
            }
        }

        if (parent.getChildAdapterPosition(view) % 2 == 0){
            outRect.left = 48;
        }else {
            outRect.right = 48;
        }
    }
}
