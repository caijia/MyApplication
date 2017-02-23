package com.example.administrator.myapplication.widget;

import android.content.Context;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by cai.jia on 2017/2/23 0023
 */

public class ScrollerLayout extends FrameLayout {

    public ScrollerLayout(Context context) {
        this(context,null);
    }

    public ScrollerLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = ScrollerCompat.create(getContext(), null);
    }

    private View child;
    private ScrollerCompat mScroller;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) {
            throw new RuntimeException("only one child");
        }

        child = getChildAt(0);
    }

    public void springBack() {
        mScroller.springBack(200, 200, 0,0, 300,1000);
        invalidate();
    }

    public void fling() {
        mScroller.fling(0, 0, 0, 2000, 0, 0, 0, child.getHeight() - getHeight());
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int currX = mScroller.getCurrX();
            int currY = mScroller.getCurrY();
            scrollTo(currX,currY);
            invalidate();
        }
    }
}
