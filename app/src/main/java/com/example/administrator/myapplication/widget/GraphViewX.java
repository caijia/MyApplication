package com.example.administrator.myapplication.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by cai.jia on 2017/3/19.
 */

public class GraphViewX extends View {

    private ViewPort viewPort;
    private Paint textPaint;
    private Rect textRect;
    private Paint linePaint;
    private int coordinateTextHeight;
    private Path linePath = new Path();
    private List<LineDataSeries> lineSeriesList;

    public GraphViewX(Context context) {
        this(context, null);
    }

    public GraphViewX(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GraphViewX(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GraphViewX(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(spToPx(14f));
        textPaint.setColor(Color.BLACK);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(1);
        linePaint.setColor(Color.parseColor("#999999"));

        textRect = new Rect();
        coordinateTextHeight = 0;

        lineSeriesList = new ArrayList<>();
    }

    private float spToPx(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getContext().getResources().getDisplayMetrics());
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    public ViewPort getViewPort() {
        return viewPort;
    }

    public void setViewPort(ViewPort viewPort) {
        this.viewPort = viewPort;
    }

    public Paint getTextPaint() {
        return textPaint;
    }

    public int getCoordinateTextHeight() {
        return coordinateTextHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (viewPort != null) {
            int width = viewPort.getxCount() * viewPort.getSpacingX();
            int height = viewPort.getyCount() * viewPort.getSpacingY() + coordinateTextHeight;
            setMeasuredDimension(
                    resolveSize(0, MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)),
                    resolveSize(0, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (viewPort == null) {
            return;
        }

        int width = getWidth();
        int height = getHeight();

        //画x轴的刻度
        if (coordinateTextHeight != 0) {

            float xCount = viewPort.getxCount();
            int spacingX = viewPort.getSpacingX();
            for (int i = 0; i <= xCount; i++) {
                textRect.setEmpty();
                String text = String.valueOf(i);
                textPaint.getTextBounds(text, 0, text.length(), textRect);
                int textWidth = textRect.width();

                int left;
                int lineLeft;
                if (i == 0) {
                    left = 0;
                    lineLeft = Math.round(linePaint.getStrokeWidth());

                } else if (i == xCount) {
                    left = spacingX * i - textWidth - Math.round(dpToPx(1f));
                    lineLeft = spacingX * i - Math.round(linePaint.getStrokeWidth());

                } else {
                    left = spacingX * i - textWidth / 2;
                    lineLeft = spacingX * i;
                }
                canvas.drawText(String.valueOf(i), left, height, textPaint);

                //画竖线
                if (i > 0) {
                    canvas.drawLine(lineLeft, 0, lineLeft, height - coordinateTextHeight, linePaint);
                }
            }
        }

        //画横线
        float yCount = viewPort.getyCount();
        int spacingY = viewPort.getSpacingY();
        for (int i = 0; i <= yCount; i++) {
            int lineBottom;
            if (i == 0) {
                lineBottom = Math.round(linePaint.getStrokeWidth());

            } else if (i == yCount) {
                lineBottom = spacingY * i - Math.round(linePaint.getStrokeWidth());

            } else {
                lineBottom = spacingY * i;
            }
            //画横线
            if (i > 0) {
                canvas.drawLine(0, lineBottom, width, lineBottom, linePaint);
            }
        }

        //画线条数据集
        if (lineSeriesList != null && !lineSeriesList.isEmpty()) {
            for (LineDataSeries lineDataSeries : lineSeriesList) {

                linePath.reset();
                //画数据线
                List<DataPoint> lineSeries = lineDataSeries.getLineSeries();
                int index = 0;
                for (DataPoint linePoint : lineSeries) {
                    int y = linePoint.getY();
                    float actualY = viewPort.getyCount() - y;

                    int xDistance = linePoint.getX() * viewPort.getSpacingX();
                    float yDistance = actualY * spacingY;
                    if (index == 0) {
                        linePath.moveTo(xDistance, yDistance);

                    } else {
                        linePath.lineTo(xDistance, yDistance);
                    }
                    index++;
                }

                int color = lineDataSeries.getColor();
                int paintWidth = lineDataSeries.getWidth();
                changeTextPaint(color, paintWidth);
                canvas.drawPath(linePath, textPaint);
            }
        }
    }

    private void changeTextPaint(int color, int width) {
        textPaint.reset();
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(width);
        textPaint.setColor(color);
        textPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 加入一条线
     */
    public void addLineSeries(LineDataSeries lineSeries) {
        if (lineSeries == null) {
            return;
        }
        List<DataPoint> dataPointList = lineSeries.getLineSeries();
        Collections.sort(dataPointList, new Comparator<DataPoint>() {
            @Override
            public int compare(DataPoint lhs, DataPoint rhs) {
                return lhs.getX() - rhs.getX();
            }
        });
        this.lineSeriesList.add(lineSeries);
    }

}
