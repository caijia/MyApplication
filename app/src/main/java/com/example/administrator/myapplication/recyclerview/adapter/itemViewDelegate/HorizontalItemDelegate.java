package com.example.administrator.myapplication.recyclerview.adapter.itemViewDelegate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.recyclerview.multiType.ItemViewDelegate;

import java.util.List;

/**
 * Created by cai.jia on 2017/5/16 0016
 */

public class HorizontalItemDelegate extends ItemViewDelegate<String,HorizontalItemDelegate.HorizontalVH> {

    @Override
    public HorizontalVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_honrizontal, parent, false);
        return new HorizontalVH(view);
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, String s, RecyclerView.Adapter adapter, HorizontalVH holder, int position) {
            holder.textView.setText(s);
    }

    @Override
    public boolean isForViewType(@NonNull Object item) {
        return item instanceof String;
    }

    static class HorizontalVH extends RecyclerView.ViewHolder{

        private TextView textView;
        public HorizontalVH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
