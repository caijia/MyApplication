package com.example.administrator.myapplication.recyclerview.helper;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;

/**
 * Created by cai.jia on 2017/5/12 0012
 */

public class LoadMoreHelper {

    public static LoadMoreHelper newInstance() {
        return new LoadMoreHelper();
    }

    private ScrollToBottomListener scrollToBottomListener;

    public void attachToRecyclerView(RecyclerView recyclerView,OnLoadMoreListener loadMoreListener) {
        if (recyclerView == null) {
            return;
        }

        if (scrollToBottomListener != null) {
            recyclerView.removeOnScrollListener(scrollToBottomListener);

        }else{
            scrollToBottomListener = new ScrollToBottomListener();
        }

        scrollToBottomListener.setLoadMoreListener(loadMoreListener);
        recyclerView.addOnScrollListener(scrollToBottomListener);
    }

    private static class ScrollToBottomListener extends RecyclerView.OnScrollListener{

        private boolean scrolled;
        private OnLoadMoreListener loadMoreListener;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();

            if (scrolled && visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE) {
                scrolled = false;
                boolean horizontalScroll = layoutManager.canScrollHorizontally();
                boolean verticalScroll = layoutManager.canScrollVertically();

                if (verticalScroll && !ViewCompat.canScrollVertically(recyclerView, 1)
                        || horizontalScroll && !ViewCompat.canScrollHorizontally(recyclerView, 1)) {

                    if (loadMoreListener != null) {
                        loadMoreListener.onLoadMore(recyclerView);
                    }
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dx != 0 || dy != 0) {
                scrolled = true;
            }
        }

        private void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
            this.loadMoreListener = loadMoreListener;
        }
    }

    public interface OnLoadMoreListener{

        void onLoadMore(RecyclerView recyclerView);

    }
}
