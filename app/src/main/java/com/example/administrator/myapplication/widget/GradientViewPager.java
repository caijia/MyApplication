package com.example.administrator.myapplication.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * Created by cai.jia on 2017/6/1 0001
 */

public class GradientViewPager extends FrameLayout {

    private static final int INTERVAL = 6000;
    private View firstView;
    private View secondView;
    private boolean firstViewVisible;
    private int position;
    private Handler handler;
    private ViewAdapter adapter;
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(200);
            handler.postDelayed(this, INTERVAL);
        }
    };

    public GradientViewPager(@NonNull Context context) {
        this(context, null);
    }

    public GradientViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientViewPager(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GradientViewPager(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        handler = new LooperHandler(this);
    }

    public void setAdapter(ViewAdapter adapter) {
        if (adapter == null) {
            return;
        }

        position = 0;
        firstViewVisible = true;

        this.adapter = adapter;
        int childCount = getChildCount();
        if (childCount == 0) {
            //加入两个View
            for (int i = 0; i < 2; i++) {
                View view = adapter.createView(this);
                addView(view);
                adapter.bindView(view, position);
                position++;
            }

        } else {

            for (int i = 0; i < 2; i++) {
                View view = getChildAt(i);
                adapter.bindView(view, position);
                position++;
            }
        }

        if (getChildCount() != 2) {
            throw new RuntimeException("childCount != 2");
        }
        firstView = getChildAt(0);
        secondView = getChildAt(1);
        ViewCompat.setAlpha(secondView, 0);
        start();
    }

    private void start() {
        stop();
        handler.postDelayed(task, INTERVAL);
    }

    private void stop() {
        handler.removeMessages(200);
        handler.removeCallbacks(task);
    }

    private void gradientSwitchView() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewCompat.setAlpha(firstView, firstViewVisible ? 1 - value : value);
                ViewCompat.setAlpha(secondView, firstViewVisible ? value : 1 - value);
            }
        });
        valueAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                firstViewVisible = !firstViewVisible;
                adapter.bindView(firstViewVisible ? firstView : secondView, ++position);
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    public interface ViewAdapter {

        View createView(ViewGroup parent);

        void bindView(View view, int position);
    }

    private static class LooperHandler extends Handler {

        private WeakReference<GradientViewPager> ref;

        public LooperHandler(GradientViewPager viewPager) {
            ref = new WeakReference<>(viewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 200 && ref.get() != null) {
                ref.get().gradientSwitchView();
            }
        }
    }

    private static class SimpleAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
