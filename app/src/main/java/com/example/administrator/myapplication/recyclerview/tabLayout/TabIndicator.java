package com.example.administrator.myapplication.recyclerview.tabLayout;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public interface TabIndicator {

    /**
     * 画tab指示器
     *
     * @param canvas             c
     * @param tabLayout          tabLayout 必要时根据{@link TabLayout#getDrawBounds(View, View)}
     *                           找到画Tab的精确范围.
     * @param selectedViewHolder 选中的Item
     * @param nextViewHolder     选中Item的下一个Item
     * @param position           当前选中的position
     * @param positionOffset     ViewPager偏移量[0,1)
     * @param drawBounds         能画的范围
     */
    void drawIndicator(Canvas canvas, TabLayout tabLayout,
                       @Nullable RecyclerView.ViewHolder selectedViewHolder,
                       @Nullable RecyclerView.ViewHolder nextViewHolder,
                       int position, float positionOffset,
                       @NonNull Rect drawBounds);

    /**
     * 是否画tab指示器
     *
     * @param tabLayout          tabLayout
     * @param selectedViewHolder 选中的Item
     * @param nextViewHolder     选中Item的下一个Item
     * @param position           当前选中的position
     * @param positionOffset     ViewPager偏移量[0,1)
     * @return
     */
    boolean isDrawIndicator(TabLayout tabLayout,
                            @Nullable RecyclerView.ViewHolder selectedViewHolder,
                            @Nullable RecyclerView.ViewHolder nextViewHolder,
                            int position, float positionOffset);
}