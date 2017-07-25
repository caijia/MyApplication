package com.example.administrator.myapplication.canvas;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by cai.jia on 2017/7/25 0025
 */

public class MoveGestureDetector {

    private float initialMotionX;
    private float initialMotionY;
    private int activePointerId;
    private float lastTouchX;
    private float lastTouchY;
    private int touchSlop;
    private boolean isBeginDragged;
    private OnMoveGestureListener listener;


    public MoveGestureDetector(Context context, OnMoveGestureListener listener) {
        ViewConfiguration viewConfig = ViewConfiguration.get(context);
        touchSlop = viewConfig.getScaledTouchSlop();
        this.listener = listener;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                initialMotionX = lastTouchX = event.getX(0);
                initialMotionY = lastTouchY = event.getY(0);
                activePointerId = event.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                int pointerIndex = event.getActionIndex();
                activePointerId = event.getPointerId(pointerIndex);
                lastTouchX = event.getX(pointerIndex);
                lastTouchY = event.getY(pointerIndex);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                int pointerIndex = event.findPointerIndex(activePointerId);
                if (pointerIndex < 0) {
                    return true;
                }

                float x = event.getX(pointerIndex);
                float y = event.getY(pointerIndex);
                float dx = x - lastTouchX;
                float dy = y - lastTouchY;
                float distanceX = x - initialMotionX;
                float distanceY = y - initialMotionY;
                if (!isBeginDragged && Math.hypot(Math.abs(distanceX), Math.abs(distanceY)) > touchSlop) {
                    isBeginDragged = true;
                }
                boolean b = false;
                if (listener != null) {
                    b =  listener.onMoveGestureScroll(dx, dy, distanceX, distanceY);
                }
                lastTouchX = x;
                lastTouchY = y;
                return b;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                activePointerId = -1;
                isBeginDragged = false;
                boolean b = false;
                if (listener != null) {
                    b = listener.onMoveGestureUpOrCancel();
                }
                return b;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                int pointerIndex = event.getActionIndex();
                if (event.getPointerId(pointerIndex) == activePointerId) {
                    int newIndex = pointerIndex == 0 ? 1 : 0;
                    activePointerId = event.getPointerId(newIndex);
                    lastTouchX = event.getX(newIndex);
                    lastTouchY = event.getY(newIndex);
                }
                break;
            }
        }
        return false;
    }

    public interface OnMoveGestureListener{

        boolean onMoveGestureScroll(float dx, float dy, float distanceX, float distanceY);

        boolean onMoveGestureUpOrCancel();
    }
}
