package com.example.administrator.myapplication.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by cai.jia on 2017/2/15 0015
 */

public class SwipeRefreshLayout1 extends SwipeRefreshLayout {

    public SwipeRefreshLayout1(Context context) {
        super(context);
    }

    public SwipeRefreshLayout1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("I-down");
                break;

            case MotionEvent.ACTION_MOVE:
                System.out.println("I-MOVE");
                break;

            case MotionEvent.ACTION_UP:
                System.out.println("I-UP");
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("t-down");
                break;

            case MotionEvent.ACTION_MOVE:
                System.out.println("t-MOVE");
                break;

            case MotionEvent.ACTION_UP:
                System.out.println("t-UP");
                break;
        }
        return super.onTouchEvent(ev);
    }
}
