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
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import static android.support.v4.widget.ViewDragHelper.INVALID_POINTER;

/**
 * Created by cai.jia on 2017/2/10 0010
 */

public class TestLayout extends FrameLayout implements NestedScrollingParent, NestedScrollingChild {

    private static final float DRAG_RATE = 0.45f;
    private static final int DEFAULT_DRAG_RANGE_DIP = 260;
    private static final int ANIM_DEFAULT_DURATION = 300;

    private static final int DEFAULT = 1;
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    private static final int REFRESHING = 4;
    private static final int REFRESH_COMPLETE = 5;
    private static final int ANIM_DEFAULT_DELAY = 500;
    private int currentState = DEFAULT;

    protected View headerView;
    protected View contentView;
    protected RefreshTrigger refreshTrigger;
    private float overScrollTop;
    private float dragRange;

    private ValueAnimator animator;
    private OnRefreshListener onRefreshListener;
    private int touchSlop;
    private float lastTouchY;
    private int activePointerId;
    private boolean isBeginDragged;
    private NestedScrollingChildHelper nestedScrollingChildHelper;
    private NestedScrollingParentHelper nestedScrollingParentHelper;
    private float initialMotionY;
    private float initialMotionX;
    private int[] mParentOffsetInWindow = new int[2];
    private int[] parentConsumed = new int[2];
    private ScrollerCompat mScroller;

    private VelocityTracker velocityTracker;
    private float minVelocity;
    private float maxVelocity;
    private int oldCurrY;

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
        minVelocity = configuration.getScaledMinimumFlingVelocity();
        maxVelocity = configuration.getScaledMaximumFlingVelocity();

        nestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mScroller = ScrollerCompat.create(context);
    }

    private float dpToPx(float dp) {
        return (float) Math.ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics()));
    }

    private void onActionDown() {
        if (refreshTrigger != null) {
            refreshTrigger.onStart(headerView.getMeasuredHeight());
        }
    }

    private void addVelocityTracker(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
    }

    private void recyclerVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    private void computeVelocity() {
        if (velocityTracker != null) {
            velocityTracker.computeCurrentVelocity(1000);
        }
    }

    private void onActionMove(float deltaY) {
        onActionMove(deltaY, true);
    }

    private void onActionMove(float deltaY, boolean dragRate) {
        if (isAnimRunning() || currentState == REFRESH_COMPLETE) {
            return;
        }
        overScrollTop += deltaY * (dragRate ? DRAG_RATE : 1);
        overScrollTop = clampValue(
                0, //min
                currentState == REFRESHING ? headerView.getMeasuredHeight(): dragRange, //max
                overScrollTop); //value
        headerView.setTranslationY(overScrollTop);
        contentView.setTranslationY(overScrollTop);
        if (refreshTrigger != null) {
            refreshTrigger.onMove(overScrollTop, headerView.getMeasuredHeight(),
                    currentState == REFRESHING);
        }
    }

    private void onActionUpOrCancel() {
        computeState();
        stateMapAnimation();
    }

    private boolean isAnimRunning() {
        if (animator != null && (animator.isStarted() || animator.isRunning())) {
            return true;
        }
        return false;
    }

    private void stateMapAnimation() {
        switch (currentState) {
            case DEFAULT: {
                if (refreshTrigger != null) {
                    refreshTrigger.onReset();
                }

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                if (animator != null) {
                    animator.cancel();
                }
                activePointerId = INVALID_POINTER;
                isBeginDragged = false;
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
            return;
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled() || canChildScrollUp() || isAnimRunning()
                || currentState == REFRESH_COMPLETE) {
            return false;
        }
        addVelocityTracker(ev);

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
                recyclerVelocityTracker();
                isBeginDragged = false;
                activePointerId = INVALID_POINTER;
                break;
        }
        return isBeginDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isEnabled() || canChildScrollUp() || isAnimRunning()
                || currentState == REFRESH_COMPLETE) {
            return false;
        }
        addVelocityTracker(ev);

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

                lastTouchY = y;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                onSecondaryPointerUp(ev);
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                int index = ev.findPointerIndex(activePointerId);
                if (index < 0) {
                    return false;
                }
                computeVelocity();
                int velocityY = (int) velocityTracker.getYVelocity(activePointerId);
                boolean canFlingUp = canFlingUp(velocityY);
                if (canFlingUp) {
                    //回到初始位置
                    flingToDefault(-velocityY);

                } else {
                    if (currentState == REFRESHING) {
                        return false;
                    }
                    onActionUpOrCancel();
                }
                activePointerId = INVALID_POINTER;
                isBeginDragged = false;
                break;
            }
        }

        return true;
    }

    private void flingToDefault(int velocityY) {
        mScroller.fling(
                0, 0,
                0, velocityY,
                0, 0,
                0, (int) Math.ceil(overScrollTop));
        System.out.println("overScrollTop="+(int) Math.ceil(overScrollTop));
        if (mScroller.computeScrollOffset()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private boolean canFlingUp(float velocity) {
        return velocity < 0 && Math.abs(velocity) > minVelocity && Math.abs(velocity) < maxVelocity
                && currentState == REFRESHING;
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

    @Override
    public boolean isNestedScrollingEnabled() {
        return nestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        nestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
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
        return nestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return nestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return nestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
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
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
        startNestedScroll(nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    @Override
    public void onStopNestedScroll(View target) {
        nestedScrollingParentHelper.onStopNestedScroll(target);
        //fling or scroll,当fling的时候,fling结束时才调用onActionUpOrCancel
        if (mScroller.isFinished() && (currentState != REFRESHING
                && currentState != REFRESH_COMPLETE) && !isAnimRunning()) {
            onActionUpOrCancel();
        }
        stopNestedScroll();
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow);
        int dy = dyUnconsumed + mParentOffsetInWindow[1];
        //move down
        if (dy < 0) {
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

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        //回到初始位置 velocity < 0 down
        if (velocityY > 0) {
            if (currentState == REFRESHING) {
                flingToDefault((int) velocityY);
            }
        }
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public int getNestedScrollAxes() {
        return nestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int dy = oldCurrY - mScroller.getCurrY();
            onActionMove(dy, false);
            oldCurrY = mScroller.getCurrY();
            ViewCompat.postInvalidateOnAnimation(this);

        } else {
            oldCurrY = 0;
        }
    }

    public interface OnRefreshListener {

        void onRefresh();
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
}
