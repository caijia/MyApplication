package com.example.administrator.myapplication.pull;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.widget.TestLayout;

/**
 * Created by cai.jia on 2017/2/10 0010
 */

public class TestPullActivity extends AppCompatActivity implements TestLayout.OnRefreshListener, SwipeRefreshLayout.OnRefreshListener {

    TestLayout testLayout;
//    SwipeRefreshLayout testLayout;
    private RecyclerView recyclerView;
    private ListView listView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);

        testLayout = (TestLayout) findViewById(R.id.test_layout);
//        testLayout = (SwipeRefreshLayout) findViewById(R.id.test_layout);
//        listView = (ListView) findViewById(R.id.list_view);
//        listView.setAdapter(new TestListAdapter(this));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TestPullAdapter());
        testLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println("dy="+dy);
            }
        });
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                testLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
