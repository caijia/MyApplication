package com.example.administrator.myapplication.textureview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

/**
 * Created by cai.jia on 2017/7/4 0004
 */

public abstract class GestureVideoController extends RelativeLayout {

    private static final int NONE = 0;
    private static final int HORIZONTAL = 1;
    private static final int VERTICAL_LEFT = 2;
    private static final int VERTICAL_RIGHT = 3;

    private float initialX;
    private float initialY;
    private float startX;
    private float startY;
    private int orientation = NONE;

    private int touchSlop;

    public GestureVideoController(Context context) {
        this(context,null);
    }

    public GestureVideoController(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GestureVideoController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GestureVideoController(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        ViewConfiguration config = ViewConfiguration.get(context);
        touchSlop = config.getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                initialX = x;
                initialY = y;
                startX = x;
                startY = y;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                float deltaX = x - startX;
                float deltaY = y - startY;

                float distanceX = x - initialX;
                float distanceY = y - initialY;

                if (orientation == NONE && Math.abs(distanceX) > Math.abs(distanceY)
                        && Math.abs(distanceX) > touchSlop) {
                    orientation = HORIZONTAL;
                }

                if (orientation == NONE && Math.abs(distanceY) > Math.abs(distanceX)
                        && Math.abs(distanceY) > touchSlop) {
                    boolean left = x < getWidth() / 2;
                    orientation = left ? VERTICAL_LEFT : VERTICAL_RIGHT;
                }

                startX = x;
                startY = y;

                switch (orientation) {
                    case HORIZONTAL: {
                        onHorizontalMove(deltaX);
                        break;
                    }

                    case VERTICAL_LEFT: {
                        onLeftVerticalMove(deltaY);
                        break;
                    }

                    case VERTICAL_RIGHT: {
                        onRightVerticalMove(deltaY);
                        break;
                    }
                }

                if (orientation == NONE) {
                    MotionEvent cancelEvent = MotionEvent.obtain(event);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    onTouchEvent(event);
                    cancelEvent.recycle();
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                initialX = 0;
                initialY = 0;
                startX = 0;
                startY = 0;
                orientation = NONE;
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    public abstract void onLeftVerticalMove(float distance);

    public abstract void onRightVerticalMove(float distance);

    public abstract void onHorizontalMove(float distance);
}
