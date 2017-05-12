package com.example.administrator.myapplication.recyclerview.helper;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * 自定义LinearLayoutManager smoothScrollToPosition的滚动速度,自动滚动时看起来平滑。
 * Created by cai.jia on 2017/4/26 0026
 */

public class AutoScrollerLinearLayoutManager extends LinearLayoutManager {

    /**
     * 原来滚动时间的倍数,注意这个时间的设置应该小于{@link LooperRecyclerViewHelper#interval}
     * 否则,会出现当前页还没有滚动结束,就开始下一页的滚动。
     */
    private int times = 6;
    private LinearSmoothScroller scroller;

    public AutoScrollerLinearLayoutManager(Context context) {
        super(context);
        init(context);
    }

    public AutoScrollerLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        init(context);
    }

    public AutoScrollerLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        scroller =
                new LinearSmoothScroller(context) {

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return super.calculateSpeedPerPixel(displayMetrics) * times;
                    }
                };
    }

    public void setTimes(int times) {
        this.times = times;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        if (listener != null) {
            listener.onAttachedToWindow(view);
        }
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        if (listener != null) {
            listener.onDetachedFromWindow(view, recycler);
        }
    }

    public interface OnAttachedDetachedToWindowListener{
        void onAttachedToWindow(RecyclerView view);

        void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler);
    }

    private OnAttachedDetachedToWindowListener listener;

    public void setOnAttachedDetachedToWindowListener(OnAttachedDetachedToWindowListener listener) {
        this.listener = listener;
    }
}
