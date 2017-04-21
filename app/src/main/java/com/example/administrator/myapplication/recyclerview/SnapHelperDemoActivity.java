package com.example.administrator.myapplication.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.recyclerview.snapHelper.SnapHelperAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai.jia on 2017/4/21 0021
 */

public class SnapHelperDemoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_helper);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        SnapHelperAdapter adapter = new SnapHelperAdapter(this, getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        PagerSnapHelper snapHelper = new PagerSnapHelper();
//        LinearSnapHelper snapHelper = new LinearSnapHelper();
//        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);

    }

    public List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 44; i++) {
            list.add("Item" + (i + 1));
        }
        return list;
    }
}
