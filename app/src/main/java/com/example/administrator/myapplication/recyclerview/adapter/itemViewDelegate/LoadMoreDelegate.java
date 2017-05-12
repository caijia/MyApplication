package com.example.administrator.myapplication.recyclerview.adapter.itemViewDelegate;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.recyclerview.helper.LoadMoreHelper;
import com.example.administrator.myapplication.recyclerview.model.LoadMoreObj;
import com.example.administrator.myapplication.recyclerview.multiType.ItemViewDelegate;
import com.example.administrator.myapplication.widget.LoadMoreFooterView;

import java.util.List;

/**
 * Created by cai.jia on 2017/5/12 0012
 */

public class LoadMoreDelegate
        extends ItemViewDelegate<LoadMoreObj,Object,LoadMoreDelegate.LoadMoreVH>
        implements LoadMoreHelper.OnLoadMoreListener, LoadMoreFooterView.OnRetryListener {

    private LoadMoreVH loadMoreVH;
    private LoadMoreFooterView.Status status;
    private OnLoadMoreDelegateListener onLoadMoreListener;

    public LoadMoreDelegate(OnLoadMoreDelegateListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public LoadMoreVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_load_more, parent, false);
        loadMoreVH = new LoadMoreVH(view);
        loadMoreVH.loadMoreView.setOnRetryListener(this);
        return loadMoreVH;
    }

    @Override
    public void onBindViewHolder(List<Object> dataSource, LoadMoreObj loadMoreObj,
                                 RecyclerView.Adapter adapter, LoadMoreVH holder, int position) {

    }

    @Override
    public boolean isForViewType(@NonNull Object item) {
        return item instanceof LoadMoreObj;
    }

    @Override
    public void onLoadMore() {
        if (loadMoreVH != null) {
            LoadMoreFooterView loadMoreView = loadMoreVH.loadMoreView;
            if (loadMoreView.canLoadMore()) {
                loadMoreVH.loadMoreView.setStatus(LoadMoreFooterView.Status.LOADING);

                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMore(loadMoreVH.loadMoreView);
                }
            }
        }
    }

    public void setStatus(LoadMoreFooterView.Status status) {
        if (loadMoreVH == null) {
            this.status = status;

        }else{
            loadMoreVH.loadMoreView.setStatus(status);
        }
    }

    @Override
    public void onRetry(LoadMoreFooterView view) {
        if (onLoadMoreListener != null) {
            onLoadMoreListener.onLoadMoreClickRetry(view);
        }
    }

    static class LoadMoreVH extends RecyclerView.ViewHolder{

        LoadMoreFooterView loadMoreView;

        public LoadMoreVH(View itemView) {
            super(itemView);
            loadMoreView = (LoadMoreFooterView) itemView.findViewById(R.id.load_more_view);
        }
    }

    @Override
    public int getSpanCount(GridLayoutManager layoutManager) {
        return layoutManager.getSpanCount();
    }

    @Override
    public void onViewAttachedToWindow(LoadMoreVH holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            lp.setFullSpan(true);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (recyclerView != null) {
            LoadMoreHelper.newInstance().attachToRecyclerView(recyclerView,this);
        }
    }

    public interface OnLoadMoreDelegateListener{

        void onLoadMore(LoadMoreFooterView loadMoreView);

        void onLoadMoreClickRetry(LoadMoreFooterView loadMoreView);
    }
}
