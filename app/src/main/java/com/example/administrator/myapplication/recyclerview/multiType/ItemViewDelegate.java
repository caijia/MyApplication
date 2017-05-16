package com.example.administrator.myapplication.recyclerview.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * 参考 https://github.com/sockeqwe/AdapterDelegates
 * https://github.com/Aspsine/IRecyclerView
 * Created by cai.jia on 2017/5/9 0009
 */

public abstract class ItemViewDelegate<Item,VH extends RecyclerView.ViewHolder> {

    public abstract VH onCreateViewHolder(LayoutInflater inflater,ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(List<?> dataSource, Item item,
                                          RecyclerView.Adapter adapter, VH holder, int position);

    public void onBindViewHolder(List<?> dataSource, Item item, RecyclerView.Adapter adapter,
                                 VH holder, int position, List<Object> payloads) {
    }

    public Item getItem(List<?> dataSource,RecyclerView.Adapter adapter, VH holder, int position) {
        return (Item) dataSource.get(position);
    }

    public abstract boolean isForViewType(@NonNull Object item);

    public void onViewRecycled(VH holder) {
    }

    public boolean onFailedToRecycleView(VH holder) {
        return false;
    }

    public void onViewAttachedToWindow(VH holder) {
    }

    public void onViewDetachedFromWindow(VH holder) {
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    }

    public int getSpanCount(GridLayoutManager layoutManager) {
        return 1;
    }
}
