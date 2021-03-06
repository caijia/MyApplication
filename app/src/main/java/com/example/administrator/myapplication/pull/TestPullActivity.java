package com.example.administrator.myapplication.pull;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.widget.RefreshLayout;

/**
 * Created by cai.jia on 2017/2/10 0010
 */

public class TestPullActivity extends AppCompatActivity implements RefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnScrollListener {

    RefreshLayout testLayout;
    private RecyclerView recyclerView;
    private ListView listView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);

        testLayout = (RefreshLayout) findViewById(R.id.test_layout);
//        testLayout.setRefreshingPinHeader(true);
//        testLayout.setOnScrollListener(this);
//        listView = (ListView) findViewById(R.id.list_view);
//        listView.setAdapter(new TestListAdapter(this));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TestPullAdapter());
        testLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                testLayout.setRefreshing(false);
            }
        }, 4000);
    }

    @Override
    public boolean onScroll(float scrollY, View headerView, View target) {
        System.out.println("scrollY="+scrollY);
//        headerView.bringToFront();
        headerView.setTranslationY(scrollY);
//        target.setTranslationY(scrollY);
        return true;
    }
}
