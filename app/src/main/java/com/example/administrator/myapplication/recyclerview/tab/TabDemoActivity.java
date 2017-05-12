package com.example.administrator.myapplication.recyclerview.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.recyclerview.adapter.ViewPagerAdapter;
import com.nshmura.recyclertablayout.RecyclerTabLayout;

/**
 * Created by cai.jia on 2017/5/11 0011
 */

public class TabDemoActivity extends AppCompatActivity{

    private ViewPager viewPager;
    private RecyclerTabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_demo);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (RecyclerTabLayout) findViewById(R.id.recycler_tab_layout);

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setUpWithViewPager(viewPager);

    }

}
