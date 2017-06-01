package com.example.administrator.myapplication.recyclerview.tabLayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by cai.jia on 2017/5/16 0016
 */

public class TabLayout extends RecyclerView {

    private ViewPager viewPager;

    private int position;
    private float positionOffset;
    private OrientationHelper horizontalHelper;
    private OrientationHelper verticalHelper;

    private PageDataObserver pageDataObserver;
    private PageAdapterChangeListener pageAdapterChangeListener;
    private PageChangeListener pageChangeListener;
    private TabIndicator tabIndicator;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        pageAdapterChangeListener = new PageAdapterChangeListener();
        pageChangeListener = new PageChangeListener(this);
    }

    public void setupWithViewPager(ViewPager viewPager) {
        destroyViewPagerCallback(viewPager);
        setupViewPagerCallback(viewPager);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroyViewPagerCallback(viewPager);
    }

    private void destroyViewPagerCallback(ViewPager viewPager) {
        if (viewPager == null) {
            return;
        }
        this.viewPager = viewPager;
        viewPager.removeOnPageChangeListener(pageChangeListener);
        viewPager.removeOnAdapterChangeListener(pageAdapterChangeListener);

        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter != null && pageDataObserver != null) {
            adapter.unregisterDataSetObserver(pageDataObserver);
            pageDataObserver = null;
        }
    }

    private void setupViewPagerCallback(ViewPager viewPager) {
        if (viewPager == null) {
            return;
        }
        viewPager.addOnPageChangeListener(pageChangeListener);
        viewPager.addOnAdapterChangeListener(pageAdapterChangeListener);

        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter != null && pageDataObserver == null) {
            pageDataObserver = new PageDataObserver();
            adapter.registerDataSetObserver(pageDataObserver);
        }
    }

    private OrientationHelper getOrientationHelper(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager == null || !(layoutManager instanceof LinearLayoutManager)) {
            return null;
        }

        int orientation = layoutManager.canScrollHorizontally() ? OrientationHelper.HORIZONTAL :
                layoutManager.canScrollVertically() ? OrientationHelper.VERTICAL : 0;

        switch (orientation) {
            case OrientationHelper.HORIZONTAL: {
                return getHorizontalHelper(layoutManager);
            }

            case OrientationHelper.VERTICAL: {
                return getVerticalHelper(layoutManager);
            }
        }
        return null;
    }

    private OrientationHelper getVerticalHelper(@NonNull RecyclerView.LayoutManager layoutManager) {
        if (verticalHelper == null) {
            verticalHelper = OrientationHelper
                    .createOrientationHelper(layoutManager, OrientationHelper.VERTICAL);
        }
        return verticalHelper;
    }

    private OrientationHelper getHorizontalHelper(@NonNull RecyclerView.LayoutManager layoutManager) {
        if (horizontalHelper == null) {
            horizontalHelper = OrientationHelper
                    .createOrientationHelper(layoutManager, OrientationHelper.HORIZONTAL);
        }
        return horizontalHelper;
    }

    private int getOrientationHeight() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null) {
            return 0;
        }
        return layoutManager.canScrollHorizontally() ? getMeasuredHeight() :
                layoutManager.canScrollVertically() ? getMeasuredWidth() : 0;
    }

    private int getViewWidth(View view) {
        OrientationHelper helper = getOrientationHelper(getLayoutManager());
        return view != null && helper != null ? helper.getDecoratedMeasurement(view) : 0;
    }

    private int getViewStart(View view) {
        OrientationHelper helper = getOrientationHelper(getLayoutManager());
        return view != null && helper != null ? helper.getDecoratedStart(view) : 0;
    }

    private int getViewEnd(View view) {
        OrientationHelper helper = getOrientationHelper(getLayoutManager());
        return view != null && helper != null ? helper.getDecoratedEnd(view) : 0;
    }

    @NonNull
    private LayoutManager checkLayoutManager() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null || !(layoutManager instanceof LinearLayoutManager)) {
            throw new RuntimeException("layoutManager is null or layout is not LinearLayoutManager");
        }
        return layoutManager;
    }

    private void scrollToTab(int position, float positionOffset) {
        this.position = position;
        this.positionOffset = positionOffset;

        LayoutManager layoutManager = checkLayoutManager();
        LinearLayoutManager llManager = (LinearLayoutManager) layoutManager;

        View selectedView = layoutManager.findViewByPosition(position);
        View nextView = layoutManager.findViewByPosition(position + 1);

        final int selectedWidth = getViewWidth(selectedView);
        final int nextWidth = getViewWidth(nextView);

        int centerOffset = getWidth() / 2 - selectedWidth / 2;
        int scrollOffset = (int) ((selectedWidth + nextWidth) * 0.5f * positionOffset);

        stopScroll();
        llManager.scrollToPositionWithOffset(position, centerOffset - scrollOffset);
        invalidate();
    }

    public void setTabIndicator(TabIndicator tabIndicator) {
        this.tabIndicator = tabIndicator;
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (tabIndicator == null) {
            return;
        }

        LayoutManager layoutManager = checkLayoutManager();
        ViewHolder selectedViewHolder = findViewHolderForLayoutPosition(position);
        ViewHolder nextViewHolder = findViewHolderForLayoutPosition(position + 1);

        if (!tabIndicator.isDrawIndicator(this, selectedViewHolder, nextViewHolder,
                position, positionOffset)) {
            return;
        }

        View selectedView = layoutManager.findViewByPosition(position);
        View nextView = layoutManager.findViewByPosition(position + 1);
        Rect bounds = getDrawBounds(selectedView, nextView);
        tabIndicator.drawIndicator(c, this, selectedViewHolder, nextViewHolder,
                position, positionOffset, bounds);
    }

    public Rect getDrawBounds(View selectedView, View nextView) {
        int selectViewStart = getViewStart(selectedView);
        int nextViewStart = getViewStart(nextView);

        int selectViewEnd = getViewEnd(selectedView);
        int nextViewEnd = getViewEnd(nextView);

        int bottom = getOrientationHeight();
        int top = 0;
        int left = (int) (nextViewStart * positionOffset + (1f - positionOffset) * selectViewStart);
        int right = (int) (nextViewEnd * positionOffset + (1f - positionOffset) * selectViewEnd);
        return new Rect(left, top, right, bottom);
    }

    private static class PageChangeListener implements ViewPager.OnPageChangeListener {

        private WeakReference<TabLayout> ref;
        private int scrollState;

        public PageChangeListener(TabLayout tabLayout) {
            ref = new WeakReference<>(tabLayout);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            TabLayout tabLayout = ref.get();
            if (tabLayout == null) {
                return;
            }
            tabLayout.scrollToTab(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
            TabLayout tabLayout = ref.get();
            if (tabLayout == null) {
                return;
            }

            if (scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                tabLayout.scrollToTab(position, 0);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            scrollState = state;
        }
    }

    private class PageDataObserver extends DataSetObserver {

        @Override
        public void onChanged() {

        }

        @Override
        public void onInvalidated() {

        }
    }

    private class PageAdapterChangeListener implements ViewPager.OnAdapterChangeListener {

        @Override
        public void onAdapterChanged(@NonNull ViewPager viewPager,
                                     @Nullable PagerAdapter oldAdapter,
                                     @Nullable PagerAdapter newAdapter) {
            if (TabLayout.this.viewPager == viewPager) {

            }
        }
    }
}
