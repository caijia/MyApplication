package com.example.administrator.myapplication.recyclerview.multiType;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.administrator.myapplication.recyclerview.adapter.itemViewDelegate.LoadMoreDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai.jia on 2017/5/15 0015
 */

public class LoadMoreDelegationAdapter extends AbsDelegationAdapter {

    private List<Object> totalList;
    private boolean loadMore;
    private LoadMoreDelegate.LoadMoreItem loadMoreItem;
    private AdapterDelegateDiffCallback diffCallback;

    public LoadMoreDelegationAdapter(boolean loadMore,
                                     @Nullable LoadMoreDelegate.OnLoadMoreDelegateListener listener) {
        init(loadMore, listener);
    }

    public LoadMoreDelegationAdapter(boolean loadMore,
                                     @Nullable LoadMoreDelegate.OnLoadMoreDelegateListener listener,
                                     @NonNull ItemViewDelegateManager delegateManager) {
        super(delegateManager);
        init(loadMore, listener);
    }

    private void init(boolean loadMore, LoadMoreDelegate.OnLoadMoreDelegateListener listener) {
        diffCallback = new AdapterDelegateDiffCallback();
        this.loadMore = loadMore;
        loadMoreItem = new LoadMoreDelegate.LoadMoreItem();
        totalList = new ArrayList<>();
        setDataSource(totalList);
        delegateManager.addDelegate(new LoadMoreDelegate(listener));
    }

    private void addLoadMoreItemNotify() {
        if (loadMore) {
            totalList.add(loadMoreItem);
        }
        notifyDataSetChanged();
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
    }

    public void refreshOrLoadMoreItems(int page, @NonNull List<?> items) {
        if (page < 1) {
            return;
        }

        if (page == 1) {
            updateItems(items);

        } else {
            appendItems(items);
        }
    }

    public void refreshOrLoadMoreDiffItems(int page, @NonNull List<?> items) {
        if (page < 1) {
            return;
        }

        if (page == 1) {
            updateDiffItems(items);

        } else {
            appendDiffItems(items);
        }
    }

    public void updateDiffItems(@NonNull List<?> items) {
        if (totalList.isEmpty()) {
            updateItems(items);

        } else {
            diffCallback.setOldAndNewList(delegateManager, totalList, items);
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback);
            result.dispatchUpdatesTo(this);
        }
    }

    public void appendDiffItems(@NonNull List<?> items) {
        if (totalList.isEmpty()) {
            appendItems(items);

        } else {
            List<Object> oldList = new ArrayList<>();
            oldList.addAll(totalList);

            List<Object> newList = new ArrayList<>();
            newList.addAll(totalList);
            newList.remove(loadMoreItem);
            newList.addAll(items);
            if (loadMore) {
                newList.add(loadMoreItem);
            }

            diffCallback.setOldAndNewList(delegateManager, oldList, newList);
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback);
            result.dispatchUpdatesTo(this);

            totalList.clear();
            totalList.addAll(newList);
        }
    }

    public void updateItems(@NonNull List<?> items) {
        totalList.clear();
        totalList.addAll(items);
        addLoadMoreItemNotify();
    }

    public void appendItems(@NonNull List<?> items) {
        totalList.remove(loadMoreItem);
        totalList.addAll(items);
        addLoadMoreItemNotify();
    }

    public void appendItem(@NonNull Object item) {
        totalList.remove(loadMoreItem);
        totalList.add(item);
        addLoadMoreItemNotify();
    }
}
