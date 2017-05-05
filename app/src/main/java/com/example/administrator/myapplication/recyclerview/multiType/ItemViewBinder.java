package com.example.administrator.myapplication.recyclerview.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by cai.jia on 2017/4/18 0018
 */

public abstract class ItemViewBinder<T,VH extends RecyclerView.ViewHolder> {

    private MultiTypeAdapter multiTypeAdapter;

    public MultiTypeAdapter getMultiTypeAdapter() {
        return multiTypeAdapter;
    }

    public void setMultiTypeAdapter(MultiTypeAdapter multiTypeAdapter) {
        this.multiTypeAdapter = multiTypeAdapter;
    }

    @NonNull
    public abstract VH onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    public abstract void onBindViewHolder(@NonNull VH holder, @NonNull T item);

    public void onBindViewHolder(
            @NonNull VH holder, @NonNull T item, @NonNull List<Object> payloads) {
        onBindViewHolder(holder, item);
    }

    /**
     * 当调用{@link MultiTypeAdapter#updateDiffItems(List)} 需要实现
     * {@link android.support.v7.util.DiffUtil.Callback}
     * @param oldItem
     * @param newItem
     * @return
     */
    public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
        return oldItem == newItem;
    }

    /**
     * 当调用{@link MultiTypeAdapter#updateDiffItems(List)} 需要实现
     * {@link android.support.v7.util.DiffUtil.Callback}
     * @param oldItem
     * @param newItem
     * @return
     */
    public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
        return oldItem.equals(newItem);
    }

    /**
     * 当调用{@link MultiTypeAdapter#updateDiffItems(List)} 需要实现
     * {@link android.support.v7.util.DiffUtil.Callback}
     * @param oldItem
     * @param newItem
     * @return
     */
    public Object getChangePayload(@NonNull T oldItem, @NonNull T newItem) {
        return null;
    }

    public int spanCount() {
        return -1;
    }
}
