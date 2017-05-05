package com.example.administrator.myapplication.recyclerview.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 简化开源项目多布局
 * Created by cai.jia on 2017/4/18 0018
 */

public class MultiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayMap<Class<?>, ItemViewBinder> typeMap;
    private List<Object> dataList;
    private List<Object> newList;
    private InternalCallback diffCallback;
    private List<Object> tempOldList;

    public MultiTypeAdapter() {
        super();
        typeMap = new ArrayMap<>();
        dataList = new ArrayList<>();
        newList = new ArrayList<>();
        tempOldList = new ArrayList<>();
        diffCallback = new InternalCallback();
    }

    public <T> void register(
            @NonNull Class<? extends T> clazz, @NonNull ItemViewBinder<T, ? extends RecyclerView.ViewHolder> binder) {
        binder.setMultiTypeAdapter(this);
        typeMap.put(clazz, binder);
    }

    public @Nullable
    ItemViewBinder getItemViewBinder(Class clazz) {
        return typeMap.get(clazz);
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Set<Class<?>> classes = typeMap.keySet();
        if (classes.isEmpty() || viewType >= classes.size() || viewType < 0) {
            return new EmptyVH(parent.getContext());
        }
        ItemViewBinder itemViewBinder = typeMap.valueAt(viewType);
        return itemViewBinder.onCreateViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        int viewType = holder.getItemViewType();
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        Set<Class<?>> classes = typeMap.keySet();
        if (classes.isEmpty() || viewType >= classes.size() || viewType < 0) {
            return;
        }

        Object item = getItem(position);
        ItemViewBinder binder = typeMap.valueAt(holder.getItemViewType());
        if (payloads != null && !payloads.isEmpty()) {
            binder.onBindViewHolder(holder, item, payloads);

        } else {
            binder.onBindViewHolder(holder, item);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public Object getItem(int position) {
        int size = dataList.size();
        if (position < 0 || position >= size) {
            return null;
        }
        return dataList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
        if (item == null) {
            return Integer.MIN_VALUE;
        }
        return findItemType(item.getClass());
    }

    public int findItemType(Class clazz) {
        Set<Class<?>> classes = typeMap.keySet();
        int index = 0;
        for (Class<?> clz : classes) {
            if (clz.isAssignableFrom(clazz)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private
    @Nullable
    ItemViewBinder findItemViewBinder(Object item) {
        int itemType = findItemType(item.getClass());
        if (itemType == -1) {
            return null;
        }
        Set<Class<?>> classes = typeMap.keySet();
        if (classes.isEmpty() || itemType >= classes.size() || itemType < 0) {
            return null;
        }
        return typeMap.valueAt(itemType);
    }

    public void updateItems(@NonNull List<?> items) {
        dataList.clear();
        dataList.addAll(items);
        notifyDataSetChanged();
    }

    public void updateDiffItems(@NonNull List<?> items) {
        this.newList.clear();
        this.newList.addAll(items);

        List<Object> oldList = new ArrayList<>();
        oldList.addAll(dataList);
        diffCallback.setOldAndNewList(oldList,newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback);

        dataList.clear();
        dataList.addAll(this.newList);
        result.dispatchUpdatesTo(this);
    }

    public List<Object> getOldList() {
        tempOldList.clear();
        tempOldList.addAll(dataList);
        return tempOldList;
    }

    public List<Object> getDataList() {
        return dataList;
    }

    private static class EmptyVH extends RecyclerView.ViewHolder {

        public EmptyVH(Context context) {
            super(new View(context));
        }
    }

    private class InternalCallback extends DiffUtil.Callback {

        private List<?> oldList;
        private List<?> newList;

        public void setOldAndNewList(List<?> oldList, List<?> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList == null ? 0 : oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList == null ? 0 : newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            Object oldItem = oldList.get(oldItemPosition);
            Object newItem = newList.get(newItemPosition);
            ItemViewBinder binder = findItemViewBinder(newItem);
            if (binder == null) {
                return true;
            }

            if (!oldItem.getClass().isAssignableFrom(newItem.getClass())) {
                return false;
            }
            return binder.areItemsTheSame(oldItem, newItem);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Object oldItem = oldList.get(oldItemPosition);
            Object newItem = newList.get(newItemPosition);
            ItemViewBinder binder = findItemViewBinder(newItem);
            if (binder == null) {
                return true;
            }

            if (!oldItem.getClass().isAssignableFrom(newItem.getClass())) {
                return false;
            }
            return binder.areContentsTheSame(oldItem, newItem);
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            Object oldItem = oldList.get(oldItemPosition);
            Object newItem = newList.get(newItemPosition);
            ItemViewBinder binder = findItemViewBinder(newItem);
            if (binder == null) {
                return null;
            }

            if (!oldItem.getClass().isAssignableFrom(newItem.getClass())) {
                return null;
            }
            return binder.getChangePayload(oldItem, newItem);
        }
    }
}
