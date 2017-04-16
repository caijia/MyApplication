package com.example.administrator.myapplication.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.administrator.myapplication.R;

/**
 * Created by cai.jia on 2016/11/26.
 */
public class CircleIndicator extends View implements ViewPager.OnPageChangeListener {

    private int mRadius;

    private int mSpace;
    private Paint mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int cycleShapeCount;
    private ViewPager mViewPager;
    private int currentPosition;
    private float currentPositionOffset;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CircleIndicator(Context context) {
        this(context, null);
    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator);
        try {
            mRadius = a.getDimensionPixelOffset(R.styleable.CircleIndicator_radius, dp2px(20));
            int selectedColor = a.getColor(R.styleable.CircleIndicator_selectedColor, 0xff33b5e5);
            int normalColor = a.getColor(R.styleable.CircleIndicator_normalColor, 0xff868686);
            mSpace = a.getDimensionPixelOffset(R.styleable.CircleIndicator_space, dp2px(12));
            smoothScroll = a.getBoolean(R.styleable.CircleIndicator_smoothScroll, true);

            mSelectedPaint.setColor(selectedColor);
            mNormalPaint.setColor(normalColor);

            mSelectedPaint.setAntiAlias(true);
            mNormalPaint.setAntiAlias(true);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int diameter = mRadius * 2; //直径
        int width = cycleShapeCount * diameter + mSpace * (cycleShapeCount - 1);
        int height = diameter;
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    private boolean smoothScroll;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int diameter = mRadius * 2; //直径

        int actualWidth = cycleShapeCount * diameter + mSpace * (cycleShapeCount - 1);
        int left = (width - actualWidth) / 2;
        int top = (height - diameter) / 2;
        int pageWidth = diameter + mSpace;

        for (int i = 0; i < cycleShapeCount && cycleShapeCount > 1; i++) {
            int circleLeft = left + diameter * (i + 1) + i * mSpace;
            canvas.drawCircle(
                    circleLeft,
                    top + mRadius,
                    mRadius,
                    !smoothScroll ? (selectPosition == i ? mSelectedPaint : mNormalPaint) : mNormalPaint);
        }

        if (smoothScroll) {
            int selectCircleLeftOffset = left + diameter * (currentPosition + 1) + currentPosition * mSpace;
            int selectCircleLeft = selectCircleLeftOffset + Math.round(pageWidth * currentPositionOffset);
            canvas.drawCircle(
                    selectCircleLeft,
                    top + mRadius,
                    mRadius,
                    mSelectedPaint);
        }
    }

    public void setViewPager(ViewPager view) {
        if (view == null) {
            return;
        }

        if (view.getAdapter() == null) {
            return;
        }

        mViewPager = view;
        if (cycleShapeCount == 0) {
            cycleShapeCount = view.getAdapter().getCount();
        }
        view.addOnPageChangeListener(this);
        invalidate();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPager.addOnPageChangeListener(listener);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        this.currentPosition = position % cycleShapeCount;
        this.currentPositionOffset = positionOffset;
        invalidate();
    }

    private int selectPosition;

    @Override
    public void onPageSelected(int position) {
        selectPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private int dp2px(float dpValue) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                getContext().getResources().getDisplayMetrics()));
    }
}
