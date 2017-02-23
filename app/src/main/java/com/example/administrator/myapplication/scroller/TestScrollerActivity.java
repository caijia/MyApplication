package com.example.administrator.myapplication.scroller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.widget.ScrollerLayout;

/**
 * Created by cai.jia on 2017/2/23 0023
 */

public class TestScrollerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);

        final ScrollerLayout layout = (ScrollerLayout) findViewById(R.id.scroller_layout);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.fling();
            }
        });
    }


}
