package com.example.administrator.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.administrator.myapplication.canvas.ClipImageView;

/**
 * Created by cai.jia on 2017/7/24 0024
 */

public class ClipImageActivity extends AppCompatActivity {

    public static final String CLIP_BITMAP = "params:clip_bitmap";

    private ClipImageView clipImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_image);
        clipImageView = (ClipImageView) findViewById(R.id.clip_image_view);
        clipImageView.setImageResource(R.drawable.test);
    }

    public void clipImage(View view) {
        Bitmap bitmap = clipImageView.clip();
        Intent i = new Intent();
        i.putExtra(CLIP_BITMAP, bitmap);
        setResult(RESULT_OK, i);
        finish();
    }
}
