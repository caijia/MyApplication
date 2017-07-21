package com.example.administrator.myapplication.canvas;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * ['ridʒən]
 * Created by cai.jia on 2017/7/20 0020
 */

public class RegionView extends View implements ScaleGestureDetector.OnScaleGestureListener {

    private ScaleGestureDetector scaleGesture;

    public RegionView(Context context) {
        this(context,null);
    }

    public RegionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RegionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RegionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private Paint paint;
    private Paint textPaint;
    private Paint linePaint;

    private void init(Context context) {
        paint = new Paint();
        paint.setColor(Color.RED);

        textPaint = new Paint();
        textPaint.setTextSize(42);
        textPaint.setColor(Color.CYAN);

        linePaint = new Paint();
        linePaint.setColor(Color.GREEN);

        scaleGesture = new ScaleGestureDetector(getContext(),this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGesture.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GREEN);
//        canvas.save();
//        canvas.saveLayer(0, 0, 1000, 1000, paint, Canvas.ALL_SAVE_FLAG);
//        canvas.translate(100, 100);
//        canvas.drawCircle(100,100,50, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
//        canvas.drawRect(0,0, 100, 100, paint);
//        paint.setXfermode(null);
//        canvas.restore();


        canvas.save();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, paint);

        paint.setColor(Color.RED);
        canvas.saveLayerAlpha(100, 100, 300, 300, 120, Canvas.ALL_SAVE_FLAG);
        canvas.drawCircle(200, 200, 100, paint);
        canvas.restore();
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float previousSpan = detector.getPreviousSpan();
        float currentSpan = detector.getCurrentSpan();
        Log.d("previousSpan:" ,previousSpan+"");
        Log.d("currentSpan:" ,currentSpan+"");
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}
