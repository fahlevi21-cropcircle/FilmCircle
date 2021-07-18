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

        outRect.top = 56;
        outRect.bottom = size;
        outRect.right = 24;
        outRect.left = 24;

        if (parent.getChildAdapterPosition(view) == 0){
            outRect.left = 56;
        }else if (parent.getAdapter() != null && parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1){
            outRect.right = 56;
        }
    }
}
