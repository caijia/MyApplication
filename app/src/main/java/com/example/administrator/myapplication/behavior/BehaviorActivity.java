package com.example.administrator.myapplication.behavior;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by cai.jia on 2017/2/22 0022
 */

public class BehaviorActivity extends AppCompatActivity{

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);
        textView = (TextView) findViewById(R.id.view);
    }


    public void clickItem(View view) {
        textView.offsetTopAndBottom(12);
    }
}
