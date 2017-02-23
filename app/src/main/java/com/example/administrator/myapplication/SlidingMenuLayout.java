package com.example.administrator.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * 侧滑layout(只允许从左边界滑出)
 * xml attribute:
 * menu_width_percent  侧滑菜单为屏幕宽度的百分比 默认0.7
 * menu_parallax       侧滑菜单的视觉差  默认0.4
 *
 * @see #addOnDragStateListener(OnDragStateListener)  可以监听当前滑动的距离来实现其他动画效果
 * <p>
 * Created by cai.jia on 2016/12/20.
 */
public class SlidingMenuLayout extends FrameLayout {

    /**
     * 侧滑菜单打开状态
     */
    public static final int STATE_OPEN = 1;

    /**
     * 侧滑菜单滑动状态
     */
    public static final int STATE_DRAGGING = 2;

    /**
     * 侧滑菜单关闭状态
     */
    public static final int STATE_CLOSE = 3;

    private ViewDragHelper helper;

    /**
     * 侧滑栏
     */
    private View menuView;

    /**
     * 主页栏
     */
    private View contentView;

    /**
     * 侧滑与屏幕宽度的百分比
     */
    private float menuWidthPercent;

    /**
     * 侧滑栏与主页栏的视觉差
     */
    private float menuParallax;

    /**
     * 允许滑动的范围
     */
    private int horizontalDragRange;

    /**
     * 平移滑动的距离
     */
    private int translateX;
    private int state = STATE_CLOSE;

    private List<OnDragStateListener> onDragStateListenerList;

    public SlidingMenuLayout(Context context) {
        this(context, null);
    }

    public SlidingMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onDragStateListenerList = new ArrayList<>();
        ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == menuView || (state == STATE_OPEN && child == contentView);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                translateX += dx;
                layoutChildView();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (xvel > 0) {
                    open();

                } else if (xvel == 0 && translateX > horizontalDragRange * 0.5f) {
                    open();

                } else {
                    close();
                }
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return horizontalDragRange;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (child == contentView) {
                    return Math.max(0, Math.min(horizontalDragRange, left));
                } else {
                    return (int) Math.max(-menuView.getMeasuredWidth() * menuParallax, Math.min(0, left));
                }
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                if ((edgeFlags & ViewDragHelper.EDGE_LEFT) == ViewDragHelper.EDGE_LEFT) {
                    helper.captureChildView(contentView, pointerId);
                }
            }
        };
        helper = ViewDragHelper.create(this, callback);
        helper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenuLayout);
            menuWidthPercent = a.getFloat(R.styleable.SlidingMenuLayout_menu_width_percent, 0.7f);
            menuParallax = a.getFloat(R.styleable.SlidingMenuLayout_menu_parallax, 0.4f);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    public void open() {
        if (helper.smoothSlideViewTo(contentView, horizontalDragRange, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void close() {
        if (helper.smoothSlideViewTo(contentView, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void toggle() {
        if (state == STATE_OPEN) {
            close();

        } else if (state == STATE_CLOSE) {
            open();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("There are only two view");
        }

        menuView = getChildAt(0);
        contentView = getChildAt(1);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen() && helper.findTopChildUnder(lastX, lastY) == contentView) {
                    close();
                }
            }
        });
    }

    /**
     * 打开之后,如果手指在contentView上,应该拦截事件
     * @param ev
     * @return
     */
    private boolean isOpenAndPointerUpContentView(MotionEvent ev) {
        return isOpen() &&
                helper.findTopChildUnder((int) ev.getX(),(int)ev.getY()) == contentView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return helper.shouldInterceptTouchEvent(ev) || isOpenAndPointerUpContentView(ev);
    }

    private int lastX;
    private int lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        lastX = (int) event.getX();
        lastY = (int) event.getY();
        helper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        menuView.measure(MeasureSpec.makeMeasureSpec((int) (widthSize * menuWidthPercent),
                MeasureSpec.EXACTLY), heightMeasureSpec);
        horizontalDragRange = menuView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutChildView();
    }

    private void layoutChildView() {
        int menuLeft = (int) ((translateX - menuView.getMeasuredWidth()) * menuParallax);
        menuView.layout(menuLeft, 0,
                menuLeft + menuView.getMeasuredWidth(),
                menuView.getMeasuredHeight());

        contentView.layout(translateX, 0,
                translateX + contentView.getMeasuredWidth(),
                contentView.getMeasuredHeight());
    }

    @Override
    public void computeScroll() {
        int curState;
        if (helper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
            curState = STATE_DRAGGING;
        } else {
            //动画完成
            curState = contentView.getLeft() == 0 ? STATE_CLOSE : STATE_OPEN;
        }

        if (state != curState) {
            state = curState;
        }

        if (onDragStateListenerList != null && !onDragStateListenerList.isEmpty()) {
            for (OnDragStateListener onDragStateListener : onDragStateListenerList) {
                onDragStateListener.onDragState(state, translateX, horizontalDragRange);
            }
        }
    }

    public boolean isOpen() {
        return state == STATE_OPEN;
    }

    public void addOnDragStateListener(OnDragStateListener onDragStateListener) {
        if (onDragStateListenerList != null &&
                !onDragStateListenerList.contains(onDragStateListener)) {
            onDragStateListenerList.add(onDragStateListener);
        }
    }

    public void removeOnDragStateListener(OnDragStateListener onDragStateListener) {
        if (onDragStateListenerList != null) {
            onDragStateListenerList.remove(onDragStateListener);
        }
    }

    public View getMenuView() {
        return menuView;
    }

    public View getContentView() {
        return contentView;
    }

    public interface OnDragStateListener {

        void onDragState(@DragState int state, int scrollX, int scrollXMax);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATE_OPEN, STATE_DRAGGING, STATE_CLOSE})
    public @interface DragState {
    }
}
