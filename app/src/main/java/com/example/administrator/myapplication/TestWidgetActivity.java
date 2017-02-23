package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.myapplication.adapter.ProgressViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai.jia on 2017/1/3 0003
 */

public class TestWidgetActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_progress_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ProgressViewAdapter adapter = new ProgressViewAdapter(this, getData());
        recyclerView.setAdapter(adapter);
    }


    public List<Integer> getData() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i+50);
        }
        return list;
    }
}
