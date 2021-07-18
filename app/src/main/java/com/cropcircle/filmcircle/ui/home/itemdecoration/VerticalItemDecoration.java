package com.cropcircle.filmcircle.ui.home.itemdecoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VerticalItemDecoration extends RecyclerView.ItemDecoration {
    int topSize;
    int bottomSize;
    int leftSize;
    int rightSize;

    public VerticalItemDecoration(int topSize, int bottomSize, int leftSize, int rightSize) {
        this.topSize = topSize;
        this.bottomSize = bottomSize;
        this.leftSize = leftSize;
        this.rightSize = rightSize;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = leftSize;
        outRect.right = rightSize;
        outRect.top = topSize;
        outRect.bottom = bottomSize;

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = 56;
        }
    }
}
