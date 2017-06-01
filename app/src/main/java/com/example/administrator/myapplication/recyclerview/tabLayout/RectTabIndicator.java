package com.example.administrator.myapplication.recyclerview.tabLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

/**
 * Created by cai.jia on 2017/5/17 0017
 */

public class RectTabIndicator implements TabIndicator {

    private Paint paint;

    /**
     * 指示器高度
     */
    private int indicatorHeight;

    /**
     * 指示器左右padding
     */
    private int paddingLR;

    public RectTabIndicator(Context context, float indicatorHeightDip,
                            float indicatorPaddingLR,
                            @ColorRes int indicatorColor) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        int color = context.getResources().getColor(indicatorColor);
        paint.setColor(color);
        paddingLR = dpToPx(context, indicatorPaddingLR);
        indicatorHeight = dpToPx(context, indicatorHeightDip);
    }

    @Override
    public void drawIndicator(Canvas canvas, TabLayout tabLayout,
                              @Nullable RecyclerView.ViewHolder selectedViewHolder,
                              @Nullable RecyclerView.ViewHolder nextViewHolder,
                              int position, float positionOffset, @NonNull Rect drawBounds) {
        canvas.drawRect(
                drawBounds.left + paddingLR,
                drawBounds.bottom - indicatorHeight,
                drawBounds.right - paddingLR,
                drawBounds.bottom, paint);
    }

    @Override
    public boolean isDrawIndicator(TabLayout tabLayout,
                                   @Nullable RecyclerView.ViewHolder selectedViewHolder,
                                   @Nullable RecyclerView.ViewHolder nextViewHolder,
                                   int position, float positionOffset) {
        return true;
    }

    private int dpToPx(Context context, float dip) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
                context.getResources().getDisplayMetrics()));
    }
}
