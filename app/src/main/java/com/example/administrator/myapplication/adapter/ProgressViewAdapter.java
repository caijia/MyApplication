package com.example.administrator.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.widget.ProgressView;

import java.util.List;

/**
 * Created by cai.jia on 2017/1/3 0003
 */

public class ProgressViewAdapter extends RecyclerView.Adapter<ProgressViewAdapter.ProgressVH>{

    private Context context;
    private List<Integer> list;
    private SparseBooleanArray animArray;

    public ProgressViewAdapter(Context context, List<Integer> list) {
        this.context = context;
        animArray = new SparseBooleanArray();
        this.list = list;
    }

    @Override
    public ProgressVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_progress_view, parent, false);
        return new ProgressVH(view);
    }

    @Override
    public void onBindViewHolder(ProgressVH holder, int position) {
        Integer i = list.get(position);
        if (!animArray.get(position, false)) {
            animArray.put(position,true);
            holder.pv.smoothProgress(i);

        } else {
            holder.pv.setProgress(i);
        }

        holder.textView.setText(i+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ProgressVH extends RecyclerView.ViewHolder{

        ProgressView pv;
        TextView textView;

        public ProgressVH(View itemView) {
            super(itemView);
            pv = (ProgressView) itemView.findViewById(R.id.pv);
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
