package com.example.administrator.myapplication.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class GradientView extends View {
    public GradientView(Context context) {
        this(context,null);
    }

    public GradientView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private Paint paint;

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setTextSize(60);
    }

    private LinearGradient linearGradient;

    private Bitmap srcBitmap;
    private Canvas srcCanvas;
    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

//        if (linearGradient == null) {
//            linearGradient = new LinearGradient(0, 0, width, 0,
//                    new int[]{Color.RED, Color.GREEN, Color.YELLOW},
//                    null, Shader.TileMode.CLAMP);
//        }
//        paint.setShader(linearGradient);
//        canvas.drawRect(0, 0, width, height, paint);

        if (srcBitmap == null) {
            srcBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        if (srcCanvas == null) {
            srcCanvas = new Canvas();
        }
        srcCanvas.setBitmap(srcBitmap);

        paint.setColor(Color.RED);
        srcCanvas.drawText("createBitmap", 0, height / 2, paint);
        paint.setXfermode(xfermode);
        paint.setColor(Color.YELLOW);
        srcCanvas.drawRect(0, 0, 100, height, paint);
        canvas.drawBitmap(srcBitmap, 0, 0, null);

//        canvas.drawText("createBitmap", 0, height / 2, paint);

    }
}