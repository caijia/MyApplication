package com.example.administrator.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by cai.jia on 2017/1/3 0003
 */

public class TestWidgetActivity extends AppCompatActivity {

    private ImageView clipResultIv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_progress_view);
        clipResultIv = (ImageView) findViewById(R.id.clip_result_iv);
    }

    public void clipImage(View view) {
        Intent i = new Intent(this, ClipImageActivity.class);
        startActivityForResult(i, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case 200:{
                if (data != null && data.getExtras() != null) {
                    Bitmap bitmap = data.getExtras().getParcelable(ClipImageActivity.CLIP_BITMAP);
                    clipResultIv.setImageBitmap(bitmap);
                }
                break;
            }
        }
    }
}
