package com.cropcircle.filmcircle.ui.home.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalItemDecoration extends RecyclerView.ItemDecoration {
    int topSize;
    int bottomSize;
    int leftSize;
    int rightSize;
    int paddingSize;

    public HorizontalItemDecoration(int topSize, int bottomSize, int leftSize, int rightSize, int paddingSize) {
        this.topSize = topSize;
        this.bottomSize = bottomSize;
        this.leftSize = leftSize;
        this.rightSize = rightSize;
        this.paddingSize = paddingSize;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = leftSize;
        outRect.right = rightSize;
        outRect.top = topSize;
        outRect.bottom = bottomSize;

        if (parent.getChildAdapterPosition(view) == 0 && paddingSize == 16) {
            outRect.left = 32;
        } else if (parent.getChildAdapterPosition(view) == 0 && paddingSize == 32) {
            outRect.left = 64;
        }
    }
}
