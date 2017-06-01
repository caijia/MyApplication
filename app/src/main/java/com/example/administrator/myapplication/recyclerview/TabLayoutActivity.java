package com.example.administrator.myapplication.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.helper.NavigationHelper;
import com.example.administrator.myapplication.recyclerview.adapter.ViewPagerAdapter;
import com.example.administrator.myapplication.recyclerview.adapter.itemViewDelegate.HorizontalItemDelegate;
import com.example.administrator.myapplication.recyclerview.multiType.LoadMoreDelegationAdapter;
import com.example.administrator.myapplication.recyclerview.tabLayout.RectTabIndicator;
import com.example.administrator.myapplication.recyclerview.tabLayout.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai.jia on 2017/5/16 0016
 */

public class TabLayoutActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private LoadMoreDelegationAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        tabLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tabLayout.setTabIndicator(new RectTabIndicator(this, 2, 24, R.color.colorAccent));
        mAdapter = new LoadMoreDelegationAdapter(false, null);
        mAdapter.delegateManager.addDelegate(new HorizontalItemDelegate());
        tabLayout.setAdapter(mAdapter);
        mAdapter.updateItems(getData());

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add("Item" + (i + 1));
        }
        return list;
    }
}
