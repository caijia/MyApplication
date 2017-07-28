package com.example.administrator.myapplication.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by cai.jia on 2017/7/25 0025
 */

public class LargeImageView extends AppCompatImageView implements MoveGestureDetector.OnMoveGestureListener {

    public LargeImageView(Context context) {
        this(context,null);
    }

    public LargeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LargeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        InputStream in = null;
        try {
             in = context.getAssets().open("world_map.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bitmapRegionDecoder = BitmapRegionDecoder.newInstance(in, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        region = new Rect();
        moveGesture = new MoveGestureDetector(context, this);
        options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 2;
    }

    private BitmapFactory.Options options;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        region.set(0, 0, getWidth(), getHeight());
        Bitmap bitmap = bitmapRegionDecoder.decodeRegion(region, options);
        setImageBitmap(bitmap);
    }

    private BitmapRegionDecoder bitmapRegionDecoder;
    private MoveGestureDetector moveGesture;
    private Rect region;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        moveGesture.onTouchEvent(event);
        return true;
    }

    @Override
    public void onMoveGestureScroll(float dx, float dy, float distanceX, float distanceY) {
        region.offset(-(int)dx, -(int)dy);
        Bitmap bitmap = bitmapRegionDecoder.decodeRegion(region, options);
        setImageBitmap(bitmap);
    }

    @Override
    public void onMoveGestureUpOrCancel(MotionEvent event) {

    }

    @Override
    public void onMoveGestureDoubleTap(MotionEvent event) {

    }

    @Override
    public boolean onMoveGestureBeginTap(MotionEvent event) {
        return false;
    }
}
