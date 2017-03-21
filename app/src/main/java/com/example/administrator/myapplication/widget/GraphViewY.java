package com.example.administrator.myapplication.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Locale;

/**
 * Created by cai.jia on 2017/3/20 0020
 */

public class GraphViewY extends View {

    private Rect textRect;
    private Paint textPaint;
    private Paint linePaint;
    private int coordinateTextHeight;
    private ViewPort viewPort;

    public GraphViewY(Context context) {
        this(context, null);
    }

    public GraphViewY(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GraphViewY(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GraphViewY(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(spToPx(10f));
        textPaint.setColor(Color.BLACK);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(1);
        linePaint.setColor(Color.parseColor("#999999"));

        textRect = new Rect();
        coordinateTextHeight = 0;
    }

    private float spToPx(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getContext().getResources().getDisplayMetrics());
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (viewPort != null) {
            int height = viewPort.getyCount() * viewPort.getSpacingY() + coordinateTextHeight;
            textRect.setEmpty();
            int width = computeWidth();
            setMeasuredDimension(
                    resolveSize(0, MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)),
                    resolveSize(0, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)));
        }
    }

    private int computeWidth() {
        int count = viewPort.getyCount();
        float stepY = viewPort.getStepY();
        float minY = viewPort.getMinY();

        int maxWidth = 0;
        for (int i = 0; i <= count; i++) {
            String text = String.format(Locale.CHINESE,"%.2f",(i * stepY + minY));
            textRect.setEmpty();
            textPaint.getTextBounds(text, 0, text.length(), textRect);

            if (maxWidth < textRect.width()) {
                maxWidth = textRect.width();
            }
        }
        return (int) (maxWidth + dpToPx(8));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (viewPort != null) {
            //画y轴上刻度
            int spacingY = viewPort.getSpacingY();
            int count = viewPort.getyCount();
            float stepY = viewPort.getStepY();
            float minY = viewPort.getMinY();

            for (int i = 0; i <= count; i++) {
                String text = String.format(Locale.CHINESE,"%.2f",(i * stepY + minY));
                textRect.setEmpty();
                textPaint.getTextBounds(text, 0, text.length(), textRect);
                int textHeight = textRect.height();

                int x = (getWidth() - textRect.width()) / 2;
                int y;
                if (i == 0) {
                    y = getHeight();

                } else if (i == count) {
                    y = getHeight() - spacingY * i + textHeight;
                } else {
                    y = getHeight() - spacingY * i + textHeight / 2;
                }

                canvas.drawText(text, x, y - coordinateTextHeight, textPaint);
            }

            int lineLeft = (int) (getWidth() - linePaint.getStrokeWidth());
            int lineHeight = getHeight() - coordinateTextHeight;
            canvas.drawLine(lineLeft, 0, lineLeft, lineHeight, linePaint);
        }
    }

    public void setViewPort(ViewPort viewPort) {
        this.viewPort = viewPort;
    }
}
