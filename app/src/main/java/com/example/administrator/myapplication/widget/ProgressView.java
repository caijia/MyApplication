package com.example.administrator.myapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.myapplication.R;


/**
 * Created by cai.jia on 2017/1/3 0003
 */

public class ProgressView extends View {

    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private int color;
    private int progress = 0;
    private int maxProgress = 100;
    private int direction;
    private Paint paint = new Paint();
    private int targetProgress;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (progress >= targetProgress) {
                removeCallbacks(this);
            } else {
                progress++;
                invalidate();
                postDelayed(this, 16);
            }
        }
    };

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
            color = a.getColor(R.styleable.ProgressView_pv_color, Color.GREEN);
            progress = a.getInt(R.styleable.ProgressView_pv_progress, 0);
            maxProgress = a.getInt(R.styleable.ProgressView_pv_max, 100);
            direction = a.getInt(R.styleable.ProgressView_pv_direction, LEFT);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
        paint.setAntiAlias(true);
        paint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        float percent = width / (float) maxProgress;
        int height = getHeight();
        if (direction == LEFT) {
            canvas.drawRect(0, 0, percent * progress, height, paint);

        } else {
            float left = width - percent * progress;
            canvas.drawRect(left, 0, width, height, paint);
        }
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public void smoothProgress(int progress) {
        if (this.progress == progress) {
            return;
        }
        this.progress = 0;
        this.targetProgress = progress;
        post(runnable);
    }
}
