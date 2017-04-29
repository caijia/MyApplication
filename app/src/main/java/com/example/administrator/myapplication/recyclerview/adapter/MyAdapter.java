package com.example.administrator.myapplication.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.recyclerview.MyRecyclerViewActivity.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cai.jia on 2017/4/7 0007
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<Student> oldList;
    private List<Student> newList;
    private DiffCallback diffCallback;

    public MyAdapter(Context context) {
        this.context = context;
        oldList = new ArrayList<>();
        newList = new ArrayList<>();
        diffCallback = new DiffCallback();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_my_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        System.out.println("adapter position="+holder.getAdapterPosition());
        Student item = getItem(position);
        holder.textView.setText(item.getName()+"age="+item.getAge());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);

        }else{
            Object o = payloads.get(0);
            if (o instanceof Student) {
                Student item = (Student) o;
                holder.textView.setText(item.getName()+"age="+item.getAge());
            }
        }
    }

    @Override
    public int getItemCount() {
        return oldList == null ? 0 : oldList.size();
    }

    public Student getItem(int position) {
        return oldList.get(position);
    }

    public void swapPosition(int fromPos, int toPos) {
        Collections.swap(oldList, toPos, fromPos);
        notifyItemMoved(fromPos,toPos);
    }

    public void setList(List<Student> list) {
        this.oldList = list;
    }

    public void remove(int adapterPosition) {
        oldList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    public List<Student> getList() {
        return oldList;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_text);
        }
    }

    public void updateAllItem(@NonNull List<Student> list) {
        this.oldList = list;
        notifyDataSetChanged();
    }

    public void updateDiffItem(@NonNull List<Student> newList) {
        this.newList = newList;
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback);
        result.dispatchUpdatesTo(this);
    }

    private class DiffCallback extends DiffUtil.Callback{

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return TextUtils.equals(oldList.get(oldItemPosition).getName(),
                    newList.get(newItemPosition).getName());
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return newList.get(newItemPosition);
        }
    }

}
