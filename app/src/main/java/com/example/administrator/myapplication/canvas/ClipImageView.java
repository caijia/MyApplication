package com.example.administrator.myapplication.canvas;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;

/**
 * Created by cai.jia on 2017/7/21 0021
 */

public class ClipImageView extends AppCompatImageView implements ScaleGestureDetector.OnScaleGestureListener {

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

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    public ClipImageView(Context context) {
        this(context, null);
    }

    public ClipImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);

        shadowColor = Color.parseColor("#aa000000");
        clipBorderColor = Color.RED;
        clipBorderStrokeWidth = 3;
        clipBorderMargin = 90;
        clipBorderAspect = 1;

        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        clipBorderRect = new Rect();
        ViewConfiguration viewConfig = ViewConfiguration.get(context);
        touchSlop = viewConfig.getScaledTouchSlop();

        scaleGestureDetector = new ScaleGestureDetector(context, this);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return true;
            }
        });
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

    private float initialMotionX;
    private float initialMotionY;
    private int activePointerId;
    private float lastTouchX;
    private float lastTouchY;
    private int touchSlop;
    private boolean isBeginDragged;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (transAnimator != null && transAnimator.isRunning()) {
            return true;
        }

        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        scaleGestureDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:{
                initialMotionX = lastTouchX = event.getX(0);
                initialMotionY = lastTouchY = event.getY(0);
                activePointerId = event.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN:{
                int pointerIndex = event.getActionIndex();
                activePointerId = event.getPointerId(pointerIndex);
                lastTouchX = event.getX(pointerIndex);
                lastTouchY = event.getY(pointerIndex);
                break;
            }

            case MotionEvent.ACTION_MOVE:{
                int pointerIndex = event.findPointerIndex(activePointerId);
                if (pointerIndex < 0) {
                    return true;
                }

                float x = event.getX(pointerIndex);
                float y = event.getY(pointerIndex);
                float dx = x - lastTouchX;
                float dy = y - lastTouchY;
                float distanceX = x - initialMotionX;
                float distanceY = y - initialMotionY;
                if (!isBeginDragged && Math.hypot(Math.abs(distanceX), Math.abs(distanceY)) > touchSlop) {
                    isBeginDragged = true;
                }
                onScroll(dx,dy,distanceX, distanceY);
                lastTouchX = x;
                lastTouchY = y;
                break;
            }

            case MotionEvent.ACTION_UP:{
                activePointerId = -1;
                isBeginDragged = false;
                checkBoarder();
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:{
                int pointerIndex = event.getActionIndex();
                if (event.getPointerId(pointerIndex) == activePointerId) {
                    int newIndex = pointerIndex == 0 ? 1 : 0;
                    activePointerId = event.getPointerId(newIndex);
                    lastTouchX = event.getX(newIndex);
                    lastTouchY = event.getY(newIndex);
                }
                break;
            }
        }
        return true;
    }

    private void onScroll(float dx,float dy,float distanceX, float distanceY) {
        imageMatrix.postTranslate(dx, dy);
        setImageMatrix(imageMatrix);
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
     * 图片经过平移后,如果图片没有包含裁剪框,则平移调整
     * 图片经过缩放后,如果缩放值小于初始缩放值,则缩放回弹
     */
    private void checkBoarder(){
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        RectF bounds = getDrawableBounds();
        final float[] matrixValues = new float[9];
        imageMatrix.getValues(matrixValues);
        float scale = matrixValues[Matrix.MSCALE_X];
        if (scale < initScale) {
            //缩放回弹

        }

        float transX = matrixValues[Matrix.MTRANS_X];
        float transY = matrixValues[Matrix.MTRANS_Y];
        float needTransX = 0;
        float needTransY = 0;
        if (transX > clipBorderRect.left) {
            //需要向左平移
            needTransX = clipBorderRect.left - transX;
        }

        if (transX + bounds.width() < clipBorderRect.right) {
            //需要向右平移
            needTransX = clipBorderRect.right - (transX + bounds.width());
        }

        if (transY > clipBorderRect.top) {
            //需要向上平移
            needTransY = clipBorderRect.top - transY;
        }

        if (transY + bounds.height() < clipBorderRect.bottom) {
            //需要向下平移
            needTransY = clipBorderRect.bottom - (transY + bounds.height());
        }
        smoothScaleOrTranslate(needTransX,needTransY,scale);
    }

    private ValueAnimator transAnimator;

    private void smoothScaleOrTranslate(final float translateX, final float translateY,float scale) {
        if (translateX != 0 || translateY != 0) {
            final float[] preValueXY = {0,0};
            int duration = (int) (pxToDp(Math.max(Math.abs(translateX), Math.abs(translateY))) * 0.8f);
            transAnimator = ValueAnimator.ofFloat(0, translateX > 0 ? translateX : translateY);
            transAnimator.setDuration(duration);
            transAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = animation.getAnimatedFraction();
                    float currentX = translateX * fraction;
                    float currentY = translateY * fraction;
                    imageMatrix.postTranslate(currentX - preValueXY[0], currentY - preValueXY[1]);
                    setImageMatrix(imageMatrix);
                    preValueXY[0] = currentX;
                    preValueXY[1] = currentY;
                }
            });
            transAnimator.start();
        }
    }

    public Bitmap clip() {
        final Drawable drawable = getDrawable();
        final Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();

        final float[] matrixValues = new float[9];
        imageMatrix.getValues(matrixValues);
        final float scale = matrixValues[Matrix.MSCALE_X] * drawable.getIntrinsicWidth() / originalBitmap.getWidth();
        final float transX = matrixValues[Matrix.MTRANS_X];
        final float transY = matrixValues[Matrix.MTRANS_Y];

        final float cropX = (-transX + clipBorderRect.left) / scale;
        final float cropY = (-transY + clipBorderRect.top) / scale;
        final float cropWidth = clipBorderRect.width() / scale;
        final float cropHeight = clipBorderRect.height() / scale;
        return Bitmap.createBitmap(originalBitmap,
                (int) cropX, (int) cropY, (int) cropWidth, (int) cropHeight);
    }

    private int pxToDp(float px) {
        return Math.round(px / getContext().getResources().getDisplayMetrics().density);
    }

    private RectF getDrawableBounds() {
        Matrix matrix = imageMatrix;
        RectF rect = new RectF();
        Drawable d = getDrawable();
        if (null != d) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }
        return rect;
    }

    /**
     * 图片适应裁剪框的宽高比
     */
    private void adjustClipBorderAspect() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        int dWidth = drawable.getIntrinsicWidth();
        int dHeight = drawable.getIntrinsicHeight();

        int cWidth = clipBorderRect.width();
        int cHeight = clipBorderRect.height();

        int vWidth = getWidth();
        int vHeight = getHeight();

        float drawableAspect = (float) dWidth / dHeight;
        float scaleWidth,scaleHeight;
        float scale;
        if (cWidth > cHeight * drawableAspect) {
            scaleWidth = cWidth;
            scale = scaleWidth / dWidth;
            scaleHeight = dHeight * scale;

        }else{
            scaleHeight = cHeight;
            scale = scaleHeight / dHeight;
            scaleWidth = dWidth * scale;
        }

        float xOffset = (vWidth - scaleWidth) / 2;
        float yOffset = (vHeight - scaleHeight) / 2;

        imageMatrix.reset();
        imageMatrix.postScale(scale, scale);
        imageMatrix.postTranslate(xOffset, yOffset);
        setImageMatrix(imageMatrix);

        initScale = scale;
        minScale = scale / 4;
        maxScale = scale * 4;
    }

    private float initScale;
    private float minScale;
    private float maxScale;
    private Matrix imageMatrix = new Matrix();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width= getWidth();
        int height = getHeight();

        canvas.save();
        canvas.saveLayer(0, 0, width, height, paint, Canvas.ALL_SAVE_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(shadowColor);
        canvas.drawRect(0, 0, width, height, paint);

        paint.setXfermode(xfermode);
        paint.setColor(clipBorderColor);
        canvas.drawRect(clipBorderRect, paint);
        canvas.restore();

        paint.setXfermode(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(clipBorderStrokeWidth);
        paint.setColor(clipBorderColor);
        clipBorderRect.inset(-clipBorderStrokeWidth / 2, -clipBorderStrokeWidth / 2);
        canvas.drawRect(clipBorderRect, paint);
        clipBorderRect.inset(clipBorderStrokeWidth / 2,clipBorderStrokeWidth / 2);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        imageMatrix.postScale(scaleFactor, scaleFactor,detector.getFocusX(),detector.getFocusY());
        final float[] matrixValues = new float[9];
        imageMatrix.getValues(matrixValues);
        float scale = matrixValues[Matrix.MSCALE_X];
        if (scale < initScale) {
            //缩放回弹
        }
        float transX = matrixValues[Matrix.MTRANS_X];
        float transY = matrixValues[Matrix.MTRANS_Y];
        Log.d("clipImage", "tranX:" + transX);
        Log.d("clipImage", "transY:" + transY);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }
}
