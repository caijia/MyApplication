package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.myapplication.widget.GradientViewPager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cai.jia on 2017/6/1 0001
 */

public class GradientActivity extends AppCompatActivity {

    private GradientViewPager gradientViewPager;
    private String[] imageUrls = {
            "http://img05.tooopen.com/images/20150531/tooopen_sy_127457023651.jpg",
            "http://img04.tooopen.com/images/20131115/sy_47505221718.jpg"
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient);

        gradientViewPager = (GradientViewPager) findViewById(R.id.view_pager);
        gradientViewPager.setAdapter(new MyAdapter(Arrays.asList(imageUrls)));
    }

    private static class MyAdapter implements GradientViewPager.ViewAdapter {

        private List<String> list;

        public MyAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public View createView(ViewGroup parent) {
            return LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_gradient_view, parent, false);
        }

        @Override
        public void bindView(View view, int position) {
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
            String url = list.isEmpty() ? "" : list.get(position % list.size());
            System.out.println("url="+url);
            Glide.with(view.getContext())
                    .load(url)
                    .asBitmap()
                    .into(imageView);
        }
    }
}
