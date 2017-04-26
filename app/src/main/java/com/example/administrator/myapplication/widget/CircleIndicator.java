package com.example.administrator.myapplication.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.administrator.myapplication.R;

import java.lang.ref.WeakReference;

/**
 * Created by cai.jia on 2016/11/26.
 */
public class CircleIndicator extends View {

    private int mRadius;
    private int mSpace;
    private Paint mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ViewPager mViewPager;
    private int currentPosition;
    private float currentPositionOffset;
    private boolean smoothScroll;
    private int selectPosition;
    private TabChangeListener tabChangeListener;
    private TabAdapterChangeListener adapterChangeListener;
    private PagerAdapterObserver adapterObserver;
    private int loopCount;

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

            tabChangeListener = new TabChangeListener(this);
            adapterChangeListener = new TabAdapterChangeListener();
            adapterObserver = new PagerAdapterObserver();
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        computeLoopCount();
        int diameter = mRadius * 2; //直径
        int width = loopCount * diameter + mSpace * (loopCount - 1);
        int height = diameter;
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    private void computeLoopCount() {
        if (mViewPager != null && mViewPager.getAdapter() != null) {
            PagerAdapter adapter = mViewPager.getAdapter();
            if (adapter instanceof LoopScroller) {
                loopCount = ((LoopScroller) adapter).loopCount();

            } else {
                loopCount = adapter.getCount();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int diameter = mRadius * 2; //直径
        computeLoopCount();

        int actualWidth = loopCount * diameter + mSpace * (loopCount - 1);
        int left = (width - actualWidth) / 2;
        int top = (height - diameter) / 2;
        int pageWidth = diameter + mSpace;

        for (int i = 0; i < loopCount && loopCount > 1; i++) {
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

        PagerAdapter oldAdapter = null;
        if (mViewPager != null) {
            mViewPager.removeOnAdapterChangeListener(adapterChangeListener);
            mViewPager.clearOnPageChangeListeners();
            oldAdapter = mViewPager.getAdapter();
        }

        mViewPager = view;
        mViewPager.addOnPageChangeListener(tabChangeListener);
        mViewPager.addOnAdapterChangeListener(adapterChangeListener);
        setPagerAdapter(oldAdapter, view.getAdapter());
    }

    private void setPagerAdapter(PagerAdapter oldAdapter, PagerAdapter newAdapter) {
        if (newAdapter == null) {
            return;
        }

        if (oldAdapter != null) {
            oldAdapter.unregisterDataSetObserver(adapterObserver);
        }
        newAdapter.registerDataSetObserver(adapterObserver);

        if (newAdapter instanceof LoopScroller) {
            loopCount = ((LoopScroller) newAdapter).loopCount();
        }

        if (loopCount == 0) {
            loopCount = newAdapter.getCount();
        }

        if (oldAdapter != null && oldAdapter.getCount() != newAdapter.getCount()) {
            requestLayout();
        } else {
            invalidate();
        }
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPager.addOnPageChangeListener(listener);
    }

    private int dp2px(float dpValue) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                getContext().getResources().getDisplayMetrics()));
    }

    public interface LoopScroller {

        int loopCount();
    }

    private static class TabChangeListener implements ViewPager.OnPageChangeListener {

        private WeakReference<CircleIndicator> ref;

        private TabChangeListener(CircleIndicator indicator) {
            ref = new WeakReference<>(indicator);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (ref != null && ref.get() != null) {
                CircleIndicator indicator = ref.get();
                indicator.currentPosition = position % indicator.loopCount;
                indicator.currentPositionOffset = positionOffset;
                indicator.invalidate();
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (ref != null && ref.get() != null) {
                CircleIndicator indicator = ref.get();
                indicator.selectPosition = position;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class TabAdapterChangeListener implements ViewPager.OnAdapterChangeListener {

        private TabAdapterChangeListener() {
        }

        @Override
        public void onAdapterChanged(@NonNull ViewPager viewPager,
                                     @Nullable PagerAdapter oldAdapter,
                                     @Nullable PagerAdapter newAdapter) {
            if (viewPager == mViewPager) {
                setPagerAdapter(oldAdapter, newAdapter);
            }
        }
    }

    private class PagerAdapterObserver extends DataSetObserver {
        PagerAdapterObserver() {
        }

        @Override
        public void onChanged() {
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            requestLayout();
        }
    }
}
