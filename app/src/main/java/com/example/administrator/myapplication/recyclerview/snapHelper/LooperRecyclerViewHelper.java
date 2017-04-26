package com.example.administrator.myapplication.recyclerview.snapHelper;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * RecyclerView 自动滚动的工具类
 * Created by cai.jia on 2017/4/26 0026
 */

public class LooperRecyclerViewHelper implements AutoScrollerLinearLayoutManager.OnAttachedDetachedToWindowListener {

    private static final int MSG_SCROLL_TO_NEXT = 123456;

    private AutoScrollerLinearLayoutManager layoutManager;
    private TimerHandler timerHandler;
    private RecyclerView recyclerView;

    /**
     * 隔多少时间,自动滚动到下一页,注意这个时间的设置应该大于{@link AutoScrollerLinearLayoutManager#times},
     * 滚动一页需要的时间
     * 否则,会出现当前页还没有滚动结束,就开始下一页的滚动。
     */
    private int interval = 4000;

    public LooperRecyclerViewHelper() {
        timerHandler = new TimerHandler(this);
    }

    public LooperRecyclerViewHelper(int interval) {
        this.interval = interval;
        timerHandler = new TimerHandler(this);
    }

    public void attachToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof AutoScrollerLinearLayoutManager) {
            this.layoutManager = (AutoScrollerLinearLayoutManager) layoutManager;
            this.layoutManager.setOnAttachedDetachedToWindowListener(this);
            sendScrollToNextMessage(interval);
        }
    }

    private void sendScrollToNextMessage(int delay) {
        timerHandler.postDelayed(task, delay);
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            timerHandler.sendEmptyMessage(MSG_SCROLL_TO_NEXT);
            timerHandler.postDelayed(this, interval);
            if (!recyclerView.isAttachedToWindow()) {
                timerHandler.removeMessages(MSG_SCROLL_TO_NEXT);
            }
        }
    };


    private void scrollToNextPosition() {
        if (layoutManager == null || layoutManager.getChildCount() == 0) {
            return;
        }

        View snapView = findSnapView(layoutManager);
        if (snapView == null) {
            return;
        }
        int position = layoutManager.getPosition(snapView);
        int[] ints = calculateDistanceToFinalSnap(layoutManager, snapView);
        if (ints[0] == 0 && ints[1] == 0) {
            position++;
        }
        scrollToPosition(position);
    }

    private void scrollToPosition(int position) {
        if (position < 0) {
            position = layoutManager.getItemCount() - 1;
        }

        if (position >= layoutManager.getItemCount()) {
            position = 0;
        }
        layoutManager.smoothScrollToPosition(recyclerView, null, position);
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        View snapView = findSnapView(layoutManager);
        if (snapView == null) {
            return;
        }
        int position = layoutManager.getPosition(snapView);
        int[] ints = calculateDistanceToFinalSnap(layoutManager, snapView);
        if (ints[0] != 0 || ints[1] != 0) {
            scrollToPosition(position);
        }
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {

    }

    private static class TimerHandler extends Handler {

        WeakReference<LooperRecyclerViewHelper> ref;

        TimerHandler(LooperRecyclerViewHelper helper) {
            ref = new WeakReference<>(helper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SCROLL_TO_NEXT) {
                if (ref != null && ref.get() != null) {
                    ref.get().scrollToNextPosition();
                }
            }
        }
    }

    /**
     * 以下代码{@link android.support.v7.widget.PagerSnapHelper 源码提供}
     */


    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
                                              @NonNull View targetView) {
        int[] out = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToCenter(layoutManager, targetView,
                    getHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }

        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToCenter(layoutManager, targetView,
                    getVerticalHelper(layoutManager));
        } else {
            out[1] = 0;
        }
        return out;
    }

    @Nullable
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return findCenterView(layoutManager, getVerticalHelper(layoutManager));
        } else if (layoutManager.canScrollHorizontally()) {
            return findCenterView(layoutManager, getHorizontalHelper(layoutManager));
        }
        return null;
    }

    private int distanceToCenter(@NonNull RecyclerView.LayoutManager layoutManager,
                                 @NonNull View targetView, OrientationHelper helper) {
        final int childCenter = helper.getDecoratedStart(targetView)
                + (helper.getDecoratedMeasurement(targetView) / 2);
        final int containerCenter;
        if (layoutManager.getClipToPadding()) {
            containerCenter = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
        } else {
            containerCenter = helper.getEnd() / 2;
        }
        return childCenter - containerCenter;
    }

    /**
     * Return the child view that is currently closest to the center of this parent.
     *
     * @param layoutManager The {@link RecyclerView.LayoutManager} associated with the attached
     *                      {@link RecyclerView}.
     * @param helper The relevant {@link OrientationHelper} for the attached {@link RecyclerView}.
     *
     * @return the child view that is currently closest to the center of this parent.
     */
    @Nullable
    private View findCenterView(RecyclerView.LayoutManager layoutManager,
                                OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }

        View closestChild = null;
        final int center;
        if (layoutManager.getClipToPadding()) {
            center = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
        } else {
            center = helper.getEnd() / 2;
        }
        int absClosest = Integer.MAX_VALUE;

        for (int i = 0; i < childCount; i++) {
            final View child = layoutManager.getChildAt(i);
            int childCenter = helper.getDecoratedStart(child)
                    + (helper.getDecoratedMeasurement(child) / 2);
            int absDistance = Math.abs(childCenter - center);

            /** if child center is closer than previous closest, set it as closest  **/
            if (absDistance < absClosest) {
                absClosest = absDistance;
                closestChild = child;
            }
        }
        return closestChild;
    }

    // Orientation helpers are lazily created per LayoutManager.
    @Nullable
    private OrientationHelper mVerticalHelper;
    @Nullable
    private OrientationHelper mHorizontalHelper;

    @NonNull
    private OrientationHelper getVerticalHelper(@NonNull RecyclerView.LayoutManager layoutManager) {
        if (mVerticalHelper == null) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return mVerticalHelper;
    }

    @NonNull
    private OrientationHelper getHorizontalHelper(
            @NonNull RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }
}
