package com.example.administrator.myapplication.recyclerview;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.recyclerview.adapter.MultiTypeDemoAdapter;
import com.example.administrator.myapplication.recyclerview.adapter.itemViewDelegate.LoadMoreDelegate;
import com.example.administrator.myapplication.recyclerview.helper.LoadMoreHelper;
import com.example.administrator.myapplication.recyclerview.itemdecoration.GridSpacingItemDecoration;
import com.example.administrator.myapplication.recyclerview.model.LoadMoreObj;
import com.example.administrator.myapplication.recyclerview.model.TextObj;
import com.example.administrator.myapplication.widget.LoadMoreFooterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai.jia on 2017/5/12 0012
 */

public class MultiTypeActivity extends AppCompatActivity implements LoadMoreDelegate.OnLoadMoreDelegateListener {

    private RecyclerView recyclerView;
    private LoadMoreDelegate loadMoreDelegate;
    private MultiTypeDemoAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_type);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
         adapter = new MultiTypeDemoAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, false, Color.CYAN));

        loadMoreDelegate = new LoadMoreDelegate(this);
        loadMoreDelegate.setStatus(LoadMoreFooterView.Status.LOADING);
        adapter.delegateManager.addDelegate(loadMoreDelegate);

        recyclerView.setAdapter(adapter);

         data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add(new TextObj("Item" + (i + 1)));
        }
        data.add(new LoadMoreObj());

        adapter.setDataSource(data);
        adapter.notifyDataSetChanged();
    }

    List<Object> data;

    private Handler handler = new Handler();

    @Override
    public void onLoadMore(LoadMoreFooterView loadMoreView) {
        System.out.println("load more");
        if (data != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadMoreDelegate.setStatus(LoadMoreFooterView.Status.THE_END);
                    data.add(data.size() -1,new TextObj("load more Item"));
                    adapter.notifyDataSetChanged();
                }
            },2000);
        }
    }

    @Override
    public void onLoadMoreClickRetry(LoadMoreFooterView loadMoreView) {

    }
}
