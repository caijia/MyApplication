package com.example.administrator.myapplication.pathdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.administrator.myapplication.R;

public class PathDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_demo);

        View button = findViewById(R.id.start_btn);
        final SimplePathView pathView = (SimplePathView) findViewById(R.id.path_view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathView.startAnimation();
            }
        });
    }

}
