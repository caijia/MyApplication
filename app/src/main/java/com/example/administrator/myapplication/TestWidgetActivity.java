package com.example.administrator.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.myapplication.canvas.ClipImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai.jia on 2017/1/3 0003
 */

public class TestWidgetActivity extends AppCompatActivity {

    private ClipImageView clipImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_progress_view);

        clipImageView = (ClipImageView) findViewById(R.id.clip_image_view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                clipImageView.setImageResource(R.drawable.test);
            }
        },5000);

    }

    public List<Integer> getData() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i+50);
        }
        return list;
    }
}
