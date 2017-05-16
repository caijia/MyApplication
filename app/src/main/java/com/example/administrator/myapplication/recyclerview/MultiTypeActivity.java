package com.example.administrator.myapplication.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.recyclerview.adapter.MultiTypeDemoAdapter;
import com.example.administrator.myapplication.recyclerview.adapter.itemViewDelegate.LoadMoreDelegate;
import com.example.administrator.myapplication.recyclerview.model.TextObj;
import com.example.administrator.myapplication.widget.LoadMoreFooterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai.jia on 2017/5/12 0012
 */

public class MultiTypeActivity extends AppCompatActivity implements LoadMoreDelegate.OnLoadMoreDelegateListener {

    private RecyclerView recyclerView;
    private MultiTypeDemoAdapter adapter;
    private int page = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_type);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new MultiTypeDemoAdapter(true,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, false, Color.CYAN));
        recyclerView.setAdapter(adapter);

        adapter.refreshOrLoadMoreDiffItems(page ,getItems("Item", 15));
    }

    private List<Object> getItems(String s, int count) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(new TextObj(s + (i + 1)));
        }
        return list;
    }

    private Handler handler = new Handler();

    @Override
    public void onLoadMore(RecyclerView recyclerView,final LoadMoreFooterView loadMoreView) {
        if (adapter != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ++page;
                    if (page == 3) {
                        loadMoreView.setStatus(LoadMoreFooterView.Status.THE_END);
                    }else{
                        loadMoreView.setStatus(LoadMoreFooterView.Status.GONE);
                    }
                    adapter.refreshOrLoadMoreDiffItems(page,getItems("loadMore Item "+page+"-", 10));
                }
            },2000);
        }
    }

    @Override
    public void onLoadMoreClickRetry(LoadMoreFooterView loadMoreView) {

    }
}
