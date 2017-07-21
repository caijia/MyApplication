package com.example.administrator.myapplication.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by cai.jia on 2017/7/21 0021
 */

public class ClipImageView extends AppCompatImageView {

    private Xfermode xfermode;
    private Paint paint;
    private int shadowColor;
    private int clipBorderColor;
    private int clipBorderStrokeWidth;
    private int clipBorderMargin;
    private float clipBorderAspect;
    private Rect clipBorderRect;
    private Runnable adjustClipBorderAspectTask = new Runnable() {
        @Override
        public void run() {
            adjustClipBorderAspect();
        }
    };

    public ClipImageView(Context context) {
        this(context, null);
    }

    public ClipImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);

        shadowColor = Color.parseColor("#aa000000");
        clipBorderColor = Color.RED;
        clipBorderStrokeWidth = 3;
        clipBorderMargin = 90;
        clipBorderAspect = 1;

        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        clipBorderRect = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = getWidth();
        int height = getHeight();
        clipBorderRect.left = clipBorderMargin;
        clipBorderRect.right = width - clipBorderMargin;
        int clipBorderHeight = (int) (clipBorderRect.width() / clipBorderAspect);
        clipBorderRect.top = (height - clipBorderHeight) / 2;
        clipBorderRect.bottom = clipBorderRect.top + clipBorderHeight;
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        post(adjustClipBorderAspectTask);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        post(adjustClipBorderAspectTask);
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        post(adjustClipBorderAspectTask);
    }

    /**
     * 图片适应裁剪框的宽高比
     */
    private void adjustClipBorderAspect() {
        Drawable drawable = getDrawable();
        int dWidth = drawable.getIntrinsicWidth();
        int dHeight = drawable.getIntrinsicHeight();

        int cWidth = clipBorderRect.width();
        int cHeight = clipBorderRect.height();

        double drawableAspect = (double) dWidth / dHeight;
        int scale;
        if (cWidth > cHeight * drawableAspect) {
            scale = cWidth / dWidth;

        }else{
            scale = cHeight / dHeight;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        canvas.save();
        canvas.saveLayer(0, 0, width, height, paint, Canvas.ALL_SAVE_FLAG);
        paint.setColor(shadowColor);
        canvas.drawRect(0, 0, width, height, paint);

        paint.setXfermode(xfermode);
        paint.setColor(clipBorderColor);
        canvas.drawRect(clipBorderRect, paint);

        paint.setXfermode(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(clipBorderStrokeWidth);
        paint.setColor(clipBorderColor);
        clipBorderRect.inset(-clipBorderStrokeWidth / 2, -clipBorderStrokeWidth / 2);
        canvas.drawRect(clipBorderRect, paint);
        canvas.restore();
    }
}
