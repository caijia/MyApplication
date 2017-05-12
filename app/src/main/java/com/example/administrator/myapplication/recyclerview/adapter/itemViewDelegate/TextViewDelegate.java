package com.example.administrator.myapplication.recyclerview.adapter.itemViewDelegate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.recyclerview.model.TextObj;
import com.example.administrator.myapplication.recyclerview.multiType.ItemViewDelegate;

import java.util.List;

/**
 * Created by cai.jia on 2017/5/12 0012
 */

public class TextViewDelegate extends ItemViewDelegate<TextObj,Object,TextViewDelegate.TextVH> {

    @Override
    public TextVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_multi_type_text, parent, false);
        return new TextVH(view);
    }

    @Override
    public void onBindViewHolder(List<Object> dataSource, TextObj textObj,
                                 RecyclerView.Adapter adapter, TextVH holder, int position) {
        holder.textView.setText(textObj.getText());
    }

    @Override
    public boolean isForViewType(@NonNull Object obj) {
        return obj instanceof TextObj;
    }

    static class TextVH extends RecyclerView.ViewHolder{

        TextView textView;

        public TextVH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
