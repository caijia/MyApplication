package com.example.administrator.myapplication.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.recyclerview.helper.AutoScrollerLinearLayoutManager;
import com.example.administrator.myapplication.recyclerview.helper.LooperRecyclerViewHelper;
import com.example.administrator.myapplication.recyclerview.helper.PageChangeSnapHelper;
import com.example.administrator.myapplication.recyclerview.helper.SnapHelperAdapter;
import com.example.administrator.myapplication.widget.RecyclerViewCircleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai.jia on 2017/4/21 0021
 */

public class SnapHelperDemoActivity extends AppCompatActivity implements PageChangeSnapHelper.OnPageChangeListener {

    private RecyclerView recyclerView;
    private RecyclerViewCircleIndicator indicator;
    private AutoScrollerLinearLayoutManager layoutManager;
    private PageChangeSnapHelper snapHelper;
    private SnapHelperAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_helper);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        indicator = (RecyclerViewCircleIndicator) findViewById(R.id.rv_circle_indicator);
        mAdapter = new SnapHelperAdapter(this, getData(4));
        layoutManager = new AutoScrollerLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        snapHelper = new PageChangeSnapHelper();
        LooperRecyclerViewHelper looperHelper = new LooperRecyclerViewHelper(2000);
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
        looperHelper.attachToRecyclerView(recyclerView);
//        recyclerView.scrollToPosition(4 * 500);
        indicator.setSnapHelper(4, layoutManager, snapHelper);
        snapHelper.addOnPageChangeListener(this);
    }

    public List<String> getData(int count) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add("Item" + (i + 1));
        }

        return list;
    }

    public void onNext(View view) {
        List<String> oldList = mAdapter.getData();
        List<String> newList = getData(5);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallback(oldList, newList));
        mAdapter.setData(newList);
        result.dispatchUpdatesTo(mAdapter);
    }

    @Override
    public void onPageSelected(RecyclerView.LayoutManager layoutManager, int position) {
        System.out.println("current item =" + position);
    }

    private static class DiffCallback extends DiffUtil.Callback {

        private List<String> oldList;
        private List<String> newList;

        public DiffCallback(List<String> oldList, List<String> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

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
            return false;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return false;
        }
    }
}
