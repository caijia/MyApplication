package com.example.administrator.myapplication.pull;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by cai.jia on 2017/2/13 0013
 */

public class TestPullAdapter extends RecyclerView.Adapter<TestPullAdapter.TestPullVH> {

    String[] s = {
            "阿宝",
            "愿",
            "丰盈商行",
            "ACDB168A",
            "酷爱着宁静",
            "小猪追傻猪我",
            "国际米兰球迷",
            "fetters我我",
            "sosok",
            "宇",
            "A§j",
            "吃货",
            "你若信我我必不负你我我必不负你我我必不负你",
            "sea",
            "有事没事",

    };
    @Override
    public TestPullVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test_pull, parent, false);
        return new TestPullVH(view);
    }

    @Override
    public void onBindViewHolder(TestPullVH holder, int position) {
        holder.textView.setText("ITEM" + position);
        holder.textView1.setText("ITEM" + s[position]);
    }

    @Override
    public int getItemCount() {
        return s.length;
    }

    static class TestPullVH extends RecyclerView.ViewHolder {

        private TextView textView;
        private TextView textView1;

        public TestPullVH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.test_view);
            textView1 = (TextView) itemView.findViewById(R.id.text1);
        }
    }
}
