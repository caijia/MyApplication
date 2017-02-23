package com.example.administrator.myapplication.pathdemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.myapplication.R;

/**
 * Created by cai.jia on 2017/1/9 0009
 */

public class SimplePathView extends View {

    float percent = 1 / 720f;
    float[] pos = new float[2];
    float[] tan = new float[2];
    Bitmap arrowBitmap;
    private Path path = new Path();
    private Paint paint = new Paint();
    private PathMeasure pathMeasure = new PathMeasure();
    private float currentValue;
    private int bitmapWidth;
    private int bitmapHeight;
    private Matrix matrix = new Matrix();

    public SimplePathView(Context context) {
        this(context, null);
    }

    public SimplePathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimplePathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(6);
        paint.setStyle(Paint.Style.STROKE);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), R.mipmap.arrow, options);
        bitmapWidth = options.outWidth;
        bitmapHeight = options.outHeight;

        options.inJustDecodeBounds = false;
        arrowBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.arrow, options);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        startWithMove(canvas);
        pathNextCounter(canvas);
//        getPosTan(canvas);
//        getMatrix(canvas);
    }

    private void startWithMove(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        canvas.translate(width / 2, height / 2);

        path.addRect(-200, -200, 200, 200, Path.Direction.CW);

        Path dst = new Path();
        dst.lineTo(-300, -300);
        pathMeasure.setPath(path, false);
        pathMeasure.getSegment(0, pathMeasure.getLength()*currentValue, dst, true);
        canvas.drawPath(dst, paint);
    }

    private void pathNextCounter(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        canvas.translate(width / 2, height / 2);

        path.addRect(-100, -100, 100, 100, Path.Direction.CW);
        path.addRect(-200, -200, 200, 200, Path.Direction.CW);

        pathMeasure.setPath(path, false);

        Path dst = new Path();
        pathMeasure.getSegment(0, pathMeasure.getLength()*currentValue, dst, true);
        canvas.drawPath(dst, paint);
//        pathMeasure.nextContour();
    }

    private int count = 0;

    private void getPosTan(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        canvas.translate(width / 2, height / 2);

        path.addCircle(0, 0, 200, Path.Direction.CW);
        pathMeasure.setPath(path, false);

        pathMeasure.getPosTan(pathMeasure.getLength() * currentValue, pos, tan);

        matrix.reset();
        double degree = Math.toDegrees(Math.atan2(tan[1], tan[0]));
        matrix.postRotate((float) degree, bitmapWidth / 2, bitmapHeight / 2);
        matrix.postTranslate(pos[0] - bitmapWidth / 2, pos[1] - bitmapHeight / 2);

        canvas.drawPath(path, paint);
        canvas.drawBitmap(arrowBitmap, matrix, paint);
    }

    public void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1).setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    private void getMatrix(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        canvas.translate(width / 2, height / 2);

        path.addCircle(0, 0, 200, Path.Direction.CW);
        pathMeasure.setPath(path, false);

        currentValue += 0.01;
        if (currentValue > 1) {
            count++;
            currentValue = 0;
        }
        matrix.reset();
        matrix.postTranslate(- bitmapWidth / 2, - bitmapHeight / 2);
        pathMeasure.getMatrix(pathMeasure.getLength() * currentValue, matrix,
                PathMeasure.TANGENT_MATRIX_FLAG|PathMeasure.POSITION_MATRIX_FLAG);

        canvas.drawPath(path, paint);
        canvas.drawBitmap(arrowBitmap, matrix, paint);

        if (count < 1) {
            invalidate();
        }
    }
}
