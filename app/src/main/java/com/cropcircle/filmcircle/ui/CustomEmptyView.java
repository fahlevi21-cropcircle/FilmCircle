package com.cropcircle.filmcircle.ui;

import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.module.BaseDraggableModule;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomEmptyView extends BaseDraggableModule {

    public CustomEmptyView(@NotNull BaseQuickAdapter<?, ?> baseQuickAdapter) {
        super(baseQuickAdapter);
    }
}
