package com.cropcircle.filmcircle.ui.home.itemdecoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ActorsItemDecoration extends RecyclerView.ItemDecoration {
    int size;

    public ActorsItemDecoration(int size){
        this.size = size;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.top = size;
        outRect.bottom = size;
        outRect.right = size;
        outRect.left = size;

        if (parent.getChildAdapterPosition(view) == 0){
            outRect.left = 32;
        }
    }
}
