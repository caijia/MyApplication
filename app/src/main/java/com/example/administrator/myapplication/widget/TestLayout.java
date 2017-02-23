package com.example.administrator.myapplication.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;

import static android.support.v4.widget.ViewDragHelper.INVALID_POINTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by cai.jia on 2017/2/10 0010
 */

public class TestLayout extends ViewGroup implements NestedScrollingParent,NestedScrollingChild{

    private static final float DRAG_RATE = 0.45f;
    private static final int DEFAULT_DRAG_RANGE_DIP = 260;
    private static final int ANIM_DEFAULT_DURATION = 300;
    private static final int DEFAULT = 1;
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    private static final int REFRESHING = 4;
    private static final int REFRESH_COMPLETE = 5;
    private static final int ANIM_DEFAULT_DELAY = 500;

    protected View headerView;
    protected View contentView;
    protected RefreshTrigger refreshTrigger;

    private float overScrollTop;
    private float dragRange;
    private int currentState;
    private ValueAnimator animator;
    private OnRefreshListener onRefreshListener;

    private int touchSlop;
    private float lastTouchY;
    private int activePointerId;
    private boolean isBeginDragged;

    private NestedScrollingChildHelper nestedScrollingChildHelper;
    private NestedScrollingParentHelper nestedScrollingParentHelper;

    public TestLayout(Context context) {
        this(context, null);
    }

    public TestLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        touchSlop = configuration.getScaledTouchSlop();

        nestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    private float dpToPx(float dp) {
        return (float) Math.ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics()));
    }

    private void onActionDown() {
        if (isRefreshingOrAnimRunning()) return;
        if (refreshTrigger != null) {
            refreshTrigger.onStart(headerView.getMeasuredHeight());
        }
    }

    private void onActionMove(float deltaY) {
        if (isRefreshingOrAnimRunning()) return;
        overScrollTop += deltaY * DRAG_RATE;
        System.out.println("overScrollTop="+overScrollTop);
        overScrollTop = clampValue(0, dragRange, overScrollTop);
        headerView.setTranslationY(overScrollTop);
        contentView.setTranslationY(overScrollTop);
        if (refreshTrigger != null) {
            refreshTrigger.onMove(overScrollTop, headerView.getMeasuredHeight());
        }
    }

    private void onActionUpOrCancel() {
        if (isRefreshingOrAnimRunning()) return;
        computeState();
//        stateMapAnimation();
    }

    private boolean isRefreshingOrAnimRunning() {
//        if (animator != null && (animator.isStarted() || animator.isRunning()) ||
//                currentState == REFRESHING) {
//            return true;
//        }
        return false;
    }

    private void stateMapAnimation() {
        switch (currentState) {
            case DEFAULT: {
                isBeginDragged = false;
                if (refreshTrigger != null) {
                    refreshTrigger.onReset();
                }
                break;
            }

            case PULL_TO_REFRESH: {
                animator(headerView, overScrollTop, 0,
                        ANIM_DEFAULT_DURATION, 0, new SimpleAnimatorListener());
                break;
            }

            case RELEASE_TO_REFRESH: {
                animator(headerView, overScrollTop, headerView.getMeasuredHeight(),
                        ANIM_DEFAULT_DURATION, 0, new SimpleAnimatorListener());
                break;
            }

            case REFRESHING: {
                if (refreshTrigger != null) {
                    refreshTrigger.onRefreshing();
                }

                if (onRefreshListener != null) {
                    onRefreshListener.onRefresh();
                }
                break;
            }

            case REFRESH_COMPLETE: {
                if (refreshTrigger != null) {
                    refreshTrigger.onRefreshComplete();
                }
                animator(headerView, overScrollTop, 0,
                        ANIM_DEFAULT_DURATION, ANIM_DEFAULT_DELAY, new SimpleAnimatorListener());
                break;
            }
        }
    }

    private void animator(final View targetView, float start, float end, int duration, int delay,
                          @Nullable Animator.AnimatorListener listener) {
        if (animator != null && (animator.isStarted() || animator.isRunning())) {
            animator.cancel();
        }

        animator = ValueAnimator.ofFloat(start, end).setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                overScrollTop = (float) animation.getAnimatedValue();
                targetView.setTranslationY(overScrollTop);
                contentView.setTranslationY(overScrollTop);
            }
        });
        if (listener != null) {
            animator.addListener(listener);
        }
        animator.setStartDelay(delay);
        animator.start();
    }

    private float clampValue(float min, float max, float value) {
        return Math.max(min, Math.min(max, value));
    }

    private void computeState() {
        if (overScrollTop == 0) {
            currentState = DEFAULT;

        } else if (overScrollTop > headerView.getMeasuredHeight()) {
            currentState = RELEASE_TO_REFRESH;

        } else if (overScrollTop == headerView.getMeasuredHeight()) {
            currentState = REFRESHING;

        } else if (overScrollTop < headerView.getMeasuredHeight()) {
            currentState = PULL_TO_REFRESH;
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        // if this is a List < L or another view that doesn't support nested
        // scrolling, ignore this request so that the vertical scroll event
        // isn't stolen
        if ((android.os.Build.VERSION.SDK_INT < 21 && contentView instanceof AbsListView)
                || (contentView != null && !ViewCompat.isNestedScrollingEnabled(contentView))) {
            // Nope.
        } else {
            super.requestDisallowInterceptTouchEvent(b);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        dragRange = Math.max(dpToPx(DEFAULT_DRAG_RANGE_DIP), headerView.getMeasuredHeight() * 2);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        headerView.layout(0, -headerView.getMeasuredHeight(), headerView.getMeasuredWidth(), 0);
        contentView.layout(0, 0, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new RuntimeException("only two child");
        }

        View headerView = getChildAt(0);
        contentView = getChildAt(1);
        if (!(headerView instanceof RefreshTrigger)) {
            throw new RuntimeException("header must be implements refreshTrigger");
        }
        this.headerView = headerView;
        refreshTrigger = (RefreshTrigger) headerView;

    }

    private boolean canChildScrollUp() {
        return ViewCompat.canScrollVertically(contentView, -1);
    }

    private static final String TAG = "event";
    private float initialMotionY;
    private float initialMotionX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (!isEnabled() || canChildScrollUp()) {
            return false;
        }

        int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                onMoveDown(ev);
                onActionDown();
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                onSecondaryPointerDown(ev);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                int index = ev.findPointerIndex(activePointerId);
                if (index < 0) {
                    return false;
                }

                float x = ev.getX(index);
                float y = ev.getY(index);

                float diffX = x - initialMotionX;
                float diffY = y - initialMotionY;

                if (!isBeginDragged && Math.abs(diffY) > Math.abs(diffX)
                        && diffY > touchSlop) {
                    isBeginDragged = true;
                }

                lastTouchY = y;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                onSecondaryPointerUp(ev);
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isBeginDragged = false;
                activePointerId = INVALID_POINTER;
                break;
        }
        return isBeginDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isEnabled() || canChildScrollUp()) {
            return false;
        }

        int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                onMoveDown(ev);
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                onSecondaryPointerDown(ev);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                int index = ev.findPointerIndex(activePointerId);
                if (index < 0) {
                    return false;
                }

                float x = ev.getX(index);
                float y = ev.getY(index);

                float deltaY = y - lastTouchY;

                float diffX = x - initialMotionX;
                float diffY = y - initialMotionY;

                if (!isBeginDragged && Math.abs(diffY) > Math.abs(diffX)
                        && diffY > touchSlop) {
                    isBeginDragged = true;
                    deltaY = diffY - touchSlop;
                }

                if (isBeginDragged) {
                    onActionMove(deltaY);
                }
                System.out.println("deltaY="+deltaY);

                lastTouchY = y;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                onSecondaryPointerUp(ev);
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                onActionUpOrCancel();
                activePointerId = INVALID_POINTER;
                isBeginDragged = false;
                return false;
        }

        return true;
    }

    private void onMoveDown(MotionEvent ev) {
        initialMotionY = lastTouchY = ev.getY(0);
        initialMotionX = ev.getX(0);
        activePointerId = ev.getPointerId(0);
        isBeginDragged = false;
    }

    private void onSecondaryPointerDown(MotionEvent ev) {
        int index = ev.getActionIndex();
        activePointerId = ev.getPointerId(index);
        lastTouchY = ev.getY(index);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int index = ev.getActionIndex();
        if (ev.getPointerId(index) == activePointerId) {
            final int newIndex = index == 0 ? 1 : 0;
            activePointerId = ev.getPointerId(newIndex);
            lastTouchY = ev.getY(newIndex);
        }
    }

    @Override
    protected TestLayout.LayoutParams generateDefaultLayoutParams() {
        return new TestLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
    }

    @Override
    public TestLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new TestLayout.LayoutParams(getContext(), attrs);
    }

    public void setRefreshing(boolean refreshing) {
        if (!refreshing) {
            currentState = REFRESH_COMPLETE;
            stateMapAnimation();

        } else {
            animator(headerView, 0, headerView.getMeasuredHeight(),
                    ANIM_DEFAULT_DURATION, ANIM_DEFAULT_DELAY, new SimpleAnimatorListener());
        }
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public interface OnRefreshListener {

        void onRefresh();
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

    }

    private class SimpleAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            computeState();
            stateMapAnimation();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        nestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return nestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return nestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        nestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return nestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return nestedScrollingChildHelper.dispatchNestedScroll(dxConsumed,dyConsumed,
                dxUnconsumed,dyUnconsumed,offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return nestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return nestedScrollingChildHelper.dispatchNestedFling(velocityX,velocityY,consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return nestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled()
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        nestedScrollingParentHelper.onNestedScrollAccepted(child,target,nestedScrollAxes);
        startNestedScroll(nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    @Override
    public void onStopNestedScroll(View target) {
        nestedScrollingParentHelper.onStopNestedScroll(target);
        onActionUpOrCancel();
        stopNestedScroll();
    }

    private int[] mParentOffsetInWindow = new int[2];

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow);

        int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy < 0 && !canChildScrollUp()) {
            onActionMove(Math.abs(dyUnconsumed));
        }
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && overScrollTop > 0) {
            if (dy > overScrollTop) {
                consumed[1] = (int) (dy - overScrollTop);

            } else {
                consumed[1] = dy;
            }

            onActionMove(-consumed[1]);
        }

        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    private int[] parentConsumed = new int[2];

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX,velocityY,consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        System.out.println("velocityY="+velocityY);
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public int getNestedScrollAxes() {
        return nestedScrollingParentHelper.getNestedScrollAxes();
    }
}
