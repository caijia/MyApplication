package com.example.administrator.myapplication.recyclerview.snapHelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.util.List;

/**
 * Created by cai.jia on 2017/4/21 0021
 */

public class SnapHelperAdapter extends RecyclerView.Adapter<SnapHelperAdapter.SnapVH> {

    private Context context;
    private LayoutInflater inflater;
    private List<String> list;

    public SnapHelperAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public SnapVH onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("onCreateViewHolder");
        View view = inflater.inflate(R.layout.item_snap_helper, parent, false);
        return new SnapVH(view);
    }

    @Override
    public void onBindViewHolder(SnapVH holder, int position) {
        holder.itemTv.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class SnapVH extends RecyclerView.ViewHolder{

        TextView itemTv;
        public SnapVH(View itemView) {
            super(itemView);
            itemTv = (TextView) itemView.findViewById(R.id.item_tv);
        }
    }
}
