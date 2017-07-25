package com.example.administrator.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.administrator.myapplication.canvas.ClipImageView;

import java.io.File;
import java.io.IOException;

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

        int i = readPictureDegree(Environment.getExternalStorageDirectory() + File.separator + "test3.jpg");
        Log.d("clipImage", "degree:" + i);
    }

    public void clipImage(View view) {
        Bitmap bitmap = clipImageView.clip();
        Intent i = new Intent();
        i.putExtra(CLIP_BITMAP, bitmap);
        setResult(RESULT_OK, i);
        finish();
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("clipImage", "error");
        }
        return degree;
    }
}
