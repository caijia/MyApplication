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
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * Created by cai.jia on 2017/7/21 0021
 */

public class ClipImageView extends AppCompatImageView implements
        ScaleGestureDetector.OnScaleGestureListener, MoveGestureDetector.OnMoveGestureListener {

    private Xfermode xfermode;
    private Paint paint;
    private int shadowColor;
    private int clipBorderColor;
    private int clipBorderStrokeWidth;
    private int clipBorderMargin;
    private float clipBorderAspect;
    private Rect clipBorderRect;
    private ScaleGestureDetector scaleGestureDetector;
    private MoveGestureDetector moveGestureDetector;
    private ValueAnimator transAnimator;
    private float initScale;
    private float minScale;
    private float maxScale;
    private Matrix imageMatrix = new Matrix();
    private int duration = 180;
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
        setScaleType(ScaleType.MATRIX);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);

        shadowColor = Color.parseColor("#bb000000");
        clipBorderColor = Color.WHITE;
        clipBorderStrokeWidth = 3;
        clipBorderMargin = 90;
        clipBorderAspect = 1;

        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        clipBorderRect = new Rect();

        moveGestureDetector = new MoveGestureDetector(context, this);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
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

    private int pointerCount;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pointerCount = event.getPointerCount();
        scaleGestureDetector.onTouchEvent(event);
        moveGestureDetector.onTouchEvent(event);
        return true;
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
     * 图片经过缩放后,如果图片没有包含裁剪框,缩放到初始值
     * @param scaleToSmall  true表示缩小到缩放初始值,false表示放大到初始值
     */
    private void adjustTranslateAndScale(boolean scaleToSmall) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        final float[] matrixValues = new float[9];
        imageMatrix.getValues(matrixValues);
        float scale = matrixValues[Matrix.MSCALE_X];
        float postScale = 0;
        RectF drawableBounds;
        if (scaleToSmall ? scale > initScale :scale < initScale) {
            postScale = initScale / scale;
            Matrix matrix = new Matrix();
            matrix.setValues(matrixValues);
            RectF bounds = getDrawableBounds(imageMatrix);
            matrix.postScale(postScale, postScale, bounds.left, bounds.top);//图片左上角为锚点缩放
            matrix.getValues(matrixValues);
            drawableBounds = getDrawableBounds(matrix);
        }else{
            drawableBounds = getDrawableBounds(imageMatrix);
        }
        float[] transXY = computeBorderXY(matrixValues, drawableBounds);
        smoothScaleAndTranslate(transXY[0], transXY[1],postScale);
    }

    private float[] computeBorderXY(float[] matrixValues,RectF drawableBounds) {
        float transX = matrixValues[Matrix.MTRANS_X];
        float transY = matrixValues[Matrix.MTRANS_Y];
        float needTransX = 0;
        float needTransY = 0;
        if (transX > clipBorderRect.left) {
            //需要向左平移
            needTransX = clipBorderRect.left - transX;
        }

        if (transX + drawableBounds.width() < clipBorderRect.right) {
            //需要向右平移
            needTransX = clipBorderRect.right - (transX + drawableBounds.width());
        }

        if (transY > clipBorderRect.top) {
            //需要向上平移
            needTransY = clipBorderRect.top - transY;
        }

        if (transY + drawableBounds.height() < clipBorderRect.bottom) {
            //需要向下平移
            needTransY = clipBorderRect.bottom - (transY + drawableBounds.height());
        }
        return new float[]{needTransX,needTransY};
    }

    private void smoothScale(float sourceScale, float targetScale, final float x, final float y) {
        transAnimator = ValueAnimator.ofFloat(sourceScale, targetScale);
        transAnimator.setDuration(duration);
        final float[] preValue = {sourceScale};
        transAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) animation.getAnimatedValue();
                float postScale = scale / preValue[0];
                imageMatrix.postScale(postScale, postScale, x, y);
                setImageMatrix(imageMatrix);
                preValue[0] = scale;
            }
        });
        transAnimator.start();
    }

    private void smoothScaleAndTranslate(final float translateX, final float translateY,final float scale) {
        if (translateX != 0 || translateY != 0) {
            final float[] preValue = {0,0,1}; //0 transX,1 transY,2 scale
            float[] startEnd = computeStartEnd(translateX, translateY,scale);
            transAnimator = ValueAnimator.ofFloat(startEnd[0], startEnd[1]);
            transAnimator.setDuration(duration);
            transAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = animation.getAnimatedFraction();
                    float currentX = translateX * fraction;
                    float currentY = translateY * fraction;
                    float dx = currentX - preValue[0];
                    float dy = currentY - preValue[1];
                    imageMatrix.postTranslate(dx,dy);
                    if (scale > 0) {
                        float currentScale = 1 + (scale - 1) * fraction; //start + (end - start)*fraction
                        float dScale = currentScale / preValue[2];
                        RectF bounds = getDrawableBounds(imageMatrix);
                        imageMatrix.postScale(dScale, dScale, bounds.left, bounds.top);
                        preValue[2] = currentScale;
                    }
                    setImageMatrix(imageMatrix);
                    preValue[0] = currentX;
                    preValue[1] = currentY;
                }
            });
            transAnimator.start();
        }
    }

    private float[] computeStartEnd(float translateX, float translateY, float scale) {
        float start = scale > 0 ? 1 : 0;
        float end = scale > 0 ? scale : (translateX !=0 ? translateX : translateY);
        return new float[]{start, end};
    }

    public
    @Nullable
    Bitmap clip() {
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
        if (cropX < 0 || cropY < 0) {
            return null;
        }
        return Bitmap.createBitmap(originalBitmap,
                (int) cropX, (int) cropY, (int) cropWidth, (int) cropHeight);
    }

    private RectF getDrawableBounds(Matrix matrix) {
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
        float scaleWidth, scaleHeight;
        float scale;
        if (cWidth > cHeight * drawableAspect) {
            scaleWidth = cWidth;
            scale = scaleWidth / dWidth;
            scaleHeight = dHeight * scale;

        } else {
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
        minScale = scale / 2;
        maxScale = scale * 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
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
        clipBorderRect.inset(clipBorderStrokeWidth / 2, clipBorderStrokeWidth / 2);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        final float[] matrixValues = new float[9];
        imageMatrix.getValues(matrixValues);
        float scale = matrixValues[Matrix.MSCALE_X];
        float willScale = scale * scaleFactor;
        if (willScale > maxScale) {
            scaleFactor = maxScale / scale;
        }

        if (willScale < minScale) {
            scaleFactor = minScale / scale;
        }
        imageMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
        setImageMatrix(imageMatrix);
        return true;
    }

    private boolean isRunning() {
        return transAnimator != null && (transAnimator.isRunning() || transAnimator.isStarted());
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        boolean isRunning = isRunning();
        return !isRunning && pointerCount > 1;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }

    @Override
    public void onMoveGestureScroll(float dx, float dy, float distanceX, float distanceY) {
        boolean isRunning = isRunning();
        if (isRunning || scaleGestureDetector.isInProgress()) {
            return;
        }
        imageMatrix.postTranslate(dx, dy);
        setImageMatrix(imageMatrix);
    }

    @Override
    public void onMoveGestureUpOrCancel(MotionEvent e) {
        adjustTranslateAndScale(false);
    }

    @Override
    public void onMoveGestureDoubleTap(MotionEvent e) {
        //大于原始缩放值的2倍,自动缩放到初始值,反之,缩放到初始值的2倍
        final float[] matrixValues = new float[9];
        imageMatrix.getValues(matrixValues);
        float scale = matrixValues[Matrix.MSCALE_X];
        if (scale >= initScale * 2 - 0.05f) {
            adjustTranslateAndScale(true);
        }else{
            smoothScale(scale, initScale * 2, e.getX(), e.getY());
        }
    }
}
