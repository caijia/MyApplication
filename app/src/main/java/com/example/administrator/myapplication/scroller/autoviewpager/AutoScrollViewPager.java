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
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a>
 */
public class AutoScrollViewPager extends ViewPager {

    public static final int DEFAULT_INTERVAL = 1500;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int SCROLL_WHAT = 0;

    private long interval = DEFAULT_INTERVAL;
    private int direction = RIGHT;
    private boolean isCycle = true;
    private Handler handler;
    private CustomDurationScroller scroller = null;

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
    }

    public void startAutoScroll() {
        stopAutoScroll();
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

        int nextItem = (direction == LEFT) ? --currentItem : ++currentItem;
        if (nextItem < 0) {
            if (isCycle) {
                setCurrentItem(totalCount - 1, false);
            }
        } else if (nextItem == totalCount) {
            if (isCycle) {
                setCurrentItem(0, false);
            }
        } else {
            setCurrentItem(nextItem, true);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                stopAutoScroll();
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                startAutoScroll();
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

    /**
     * set auto scroll direction
     *
     * @param direction {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * set whether automatic cycle when auto scroll reaching the last or first item, default is true
     *
     * @param isCycle the isCycle to set
     */
    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    public static class AutoScrollHandler extends Handler {

        private WeakReference<AutoScrollViewPager> ref;

        public AutoScrollHandler(AutoScrollViewPager viewPager) {
            ref = new WeakReference<>(viewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCROLL_WHAT:
                    if (ref != null && ref.get() != null) {
                        ref.get().scrollOnce();
                        ref.get().sendScrollMessage(ref.get().getInterval());
                    }
                default:
                    break;
            }
        }
    }

    public static class CustomDurationScroller extends Scroller {

        private double scrollFactor = 1;

        public CustomDurationScroller(Context context) {
            super(context);
        }

        public CustomDurationScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public void setScrollDurationFactor(double scrollFactor) {
            this.scrollFactor = scrollFactor;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, (int) (duration * scrollFactor));
        }
    }
}
