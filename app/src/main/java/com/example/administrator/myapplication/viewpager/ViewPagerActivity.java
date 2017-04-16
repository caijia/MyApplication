package com.example.administrator.myapplication.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.scroller.autoviewpager.AutoScrollViewPager;
import com.example.administrator.myapplication.widget.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai.jia on 2017/4/13 0013
 */

public class ViewPagerActivity extends AppCompatActivity {

    private AutoScrollViewPager viewPager;
    private TabLayout tabLayout;
    private MyPagerAdapter pagerAdapter;
    private CircleIndicator circleIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        viewPager = (AutoScrollViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        circleIndicator = (CircleIndicator) findViewById(R.id.circle_indicator);

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < 4; i++) {
            pagerAdapter.addItem(TestFragment.getInstance(i));
        }

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        circleIndicator.setViewPager(viewPager);
        viewPager.startAutoScroll();
    }

    public void addItem(View view) {
        pagerAdapter.addItem(0, TestFragment.getInstance(pagerAdapter.getCount()));
        pagerAdapter.notifyDataSetChanged();
    }

    public void removeItem(View view) {
        pagerAdapter.remove(pagerAdapter.getCount() - 1);
        pagerAdapter.notifyDataSetChanged();
    }

    public void updateItem(View view) {
        int currentItem = viewPager.getCurrentItem();
        int count = viewPager.getAdapter().getCount();
        ++currentItem;
        if (currentItem >= count) {
            currentItem = 0;
            viewPager.setCurrentItem(currentItem, false);
        } else if (currentItem < 0) {
            currentItem = count - 1;
            viewPager.setCurrentItem(currentItem, false);
        } else {
            viewPager.setCurrentItem(currentItem, true);
        }
    }

    public static class TestFragment extends Fragment {

        static final String POSITION_EXTRA = "position:id";

        int position = -1;

        public static TestFragment getInstance(int position) {
            TestFragment f = new TestFragment();
            Bundle args = new Bundle();
            args.putInt(POSITION_EXTRA, position);
            f.setArguments(args);
            return f;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            position = getArguments().getInt(POSITION_EXTRA);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            TextView textView = new TextView(container.getContext());
            textView.setText("Item position = " + position
                    + "---savedInstanceState="
                    + (savedInstanceState != null ? savedInstanceState.getString("args") : ""));
            return textView;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putString("args", "Item=" + position);
        }
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> fragments;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
        }

        public void addItem(int index, Fragment fragment) {
            if (index < 0 || index >= fragments.size()) {
                addItem(fragment);
            }
            fragments.add(index, fragment);
        }

        public void remove(int i) {
            fragments.remove(i);
        }

        public void addItem(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Item=" + position;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
