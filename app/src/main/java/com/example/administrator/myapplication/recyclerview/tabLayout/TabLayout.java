package com.example.administrator.myapplication.recyclerview.tabLayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cai.jia on 2017/5/16 0016
 */

public class TabLayout extends RecyclerView {

    private Paint paint;
    private ViewPager viewPager;

    private int position;
    private float pageScrolledOffset;
    private OrientationHelper horizontalHelper;
    private OrientationHelper verticalHelper;

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            TabLayout.this.position = position;
            TabLayout.this.pageScrolledOffset = positionOffset;
            invalidate();
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private PageDataObserver pageDataObserver;
    private PageAdapterChangeListener pageAdapterChangeListener;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        pageAdapterChangeListener = new PageAdapterChangeListener();
        paint = new Paint();
        paint.setColor(Color.CYAN);
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

    private OrientationHelper getOrientationHelper(@NonNull RecyclerView.LayoutManager layoutManager,
                                                   int orientation) {
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

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null || !(layoutManager instanceof LinearLayoutManager)) {
            return;
        }
        LinearLayoutManager llManager = (LinearLayoutManager) layoutManager;
        int orientation = layoutManager.canScrollHorizontally() ? OrientationHelper.HORIZONTAL :
                layoutManager.canScrollVertically() ? OrientationHelper.VERTICAL : 0;

        OrientationHelper helper = getOrientationHelper(layoutManager, orientation);
        if (helper == null) {
            return;
        }

        int drawBottom = 0;
        int drawTop = 0;
        int drawStart = 0;
        int drawEnd = 0;

        View selectedView = layoutManager.findViewByPosition(position);
        if (selectedView != null) {
            View nextView = layoutManager.findViewByPosition(position + 1);

            if (nextView != null) {
                int selectViewStart = helper.getDecoratedStart(selectedView);
                int nextViewStart = helper.getDecoratedStart(nextView);

                int selectViewEnd = helper.getDecoratedEnd(selectedView);
                int nextViewEnd = helper.getDecoratedEnd(nextView);

                drawBottom = layoutManager.canScrollHorizontally() ? getMeasuredHeight() :
                        layoutManager.canScrollVertically() ? getMeasuredWidth() : 0;
                drawTop = drawBottom - 12;

                drawStart = (int) (nextViewStart * pageScrolledOffset + (1f - pageScrolledOffset) * selectViewStart);
                drawEnd = (int) (nextViewEnd * pageScrolledOffset + (1f - pageScrolledOffset) * selectViewEnd);
            }
        }

        c.drawRect(drawStart, drawTop, drawEnd, drawBottom, paint);
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
                                     @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {

        }
    }
}
