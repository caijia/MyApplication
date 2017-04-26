package com.example.administrator.myapplication.scroller.autoviewpager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * Auto Scroll View Pager
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a>
 */
public class AutoScrollViewPager extends ViewPager {

    public static final int DEFAULT_INTERVAL = 1500;
    public static final int SCROLL_WHAT = 0;

    private long interval = DEFAULT_INTERVAL;
    private Handler handler;
    private CustomDurationScroller scroller = null;
    private CheckPageChangeListener pageChangeListener = new CheckPageChangeListener();
    private boolean motionEventUpOrCancel;
    private int scrollFactor = 5;
    private boolean autoScroll;

    public AutoScrollViewPager(Context paramContext) {
        super(paramContext);
        init();
    }

    public AutoScrollViewPager(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    private void init() {
        handler = new AutoScrollHandler(this);
        setViewPagerScroller();
        scroller.setScrollDurationFactor(scrollFactor);
        addOnPageChangeListener(pageChangeListener);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAutoScroll();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoScroll();
    }

    public void startAutoScroll() {
        sendScrollMessage(interval);
    }

    public void startAutoScroll(int delayTimeInMills) {
        sendScrollMessage(delayTimeInMills);
    }

    public void stopAutoScroll() {
        handler.removeMessages(SCROLL_WHAT);
    }

    public void setScrollDurationFactor(double scrollFactor) {
        scroller.setScrollDurationFactor(scrollFactor);
    }

    private void sendScrollMessage(long delayTimeInMills) {
        if (!autoScroll) {
            return;
        }
        handler.removeMessages(SCROLL_WHAT);
        handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
    }

    private void setViewPagerScroller() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);

            scroller = new CustomDurationScroller(getContext(), (Interpolator) interpolatorField.get(null));
            scrollerField.set(this, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scrollOnce() {
        PagerAdapter adapter = getAdapter();
        int currentItem = getCurrentItem();
        int totalCount;
        if (adapter == null || (totalCount = adapter.getCount()) <= 1) {
            return;
        }

        int nextItem = ++currentItem;
        if (nextItem < 0) {
            setCurrentItem(totalCount - 1, false);
        } else if (nextItem == totalCount) {
            setCurrentItem(0, false);
        } else {
            setCurrentItem(nextItem, true);
        }
    }

    public void setAutoScroll(boolean autoScroll) {
        this.autoScroll = autoScroll;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!autoScroll) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                stopAutoScroll();
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (pageChangeListener.isPageScroll()) {
                    motionEventUpOrCancel = true;
                    scroller.setScrollDurationFactor(1);

                } else {
                    scroller.setScrollDurationFactor(scrollFactor);
                    startAutoScroll();
                }
                break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public int getScrollFactor() {
        return scrollFactor;
    }

    public void setScrollFactor(int scrollFactor) {
        this.scrollFactor = scrollFactor;
    }

    private static class AutoScrollHandler extends Handler {

        private WeakReference<AutoScrollViewPager> ref;

        AutoScrollHandler(AutoScrollViewPager viewPager) {
            ref = new WeakReference<>(viewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCROLL_WHAT: {
                    if (ref != null && ref.get() != null) {
                        AutoScrollViewPager viewPager = ref.get();
                        viewPager.scrollOnce();
                        viewPager.sendScrollMessage(viewPager.getInterval());
                    }
                }
            }
        }
    }

    private static class CustomDurationScroller extends Scroller {

        private double scrollFactor = 1;

        public CustomDurationScroller(Context context) {
            super(context);
        }

        CustomDurationScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        void setScrollDurationFactor(double scrollFactor) {
            this.scrollFactor = scrollFactor;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, (int) (duration * scrollFactor));
        }
    }

    private class CheckPageChangeListener extends SimpleOnPageChangeListener {

        private int positionOffsetPixels;

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == SCROLL_STATE_IDLE && motionEventUpOrCancel) {
                motionEventUpOrCancel = false;
                scroller.setScrollDurationFactor(scrollFactor);
                startAutoScroll();
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            this.positionOffsetPixels = positionOffsetPixels;
        }

        public boolean isPageScroll() {
            return positionOffsetPixels > 0;
        }

    }
}
