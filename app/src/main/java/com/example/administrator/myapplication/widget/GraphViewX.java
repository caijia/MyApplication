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
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.STROKE;

/**
 * Created by cai.jia on 2017/3/19.
 */

public class GraphViewX extends View {

    private ViewPort viewPort;
    private Paint paint;
    private Rect textRect;
    private Path linePath = new Path();
    private List<LineDataSeries> lineSeriesList;

    private PaintAttribute textAttribute;
    private PaintAttribute lineAttribute;
    /**
     * 保存所有x轴相等的点的集合
     */
    private ArrayMap<Float, List<RectDataSeries.RectDataPoint>> rectSeriesMap;
    private List<RectDataSeries> rectSeriesList;
    private DataPoint minDistanceItem;

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
        paint = new Paint();
        paint.setAntiAlias(true);
        textAttribute = new PaintAttribute(Color.BLACK, dpToPx(1f), FILL, spToPx(10f));
        lineAttribute = new PaintAttribute(Color.parseColor("#bbbbbb"), dpToPx(0.5f), STROKE, 0);

        textRect = new Rect();

        lineSeriesList = new ArrayList<>();
        rectSeriesList = new ArrayList<>();
        rectSeriesMap = new ArrayMap<>();
    }

    private float spToPx(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getContext().getResources().getDisplayMetrics());
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    public void setViewPort(ViewPort viewPort) {
        this.viewPort = viewPort;
    }

    private int getCountY(){
        String[] textArray = viewPort.getTextArrayY();
        return textArray != null ? textArray.length : 0;
    }

    private int getCountX(){
        String[] textArray = viewPort.getTextArrayX();
        return textArray != null ? textArray.length : 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (viewPort != null) {
            bottomTextHeight = computeHeight();
            int countX = getCountX();
            int countY = getCountY();
            int width = countX * viewPort.getSpacingX();
            int height = countY * viewPort.getSpacingY() + bottomTextHeight;
            setMeasuredDimension(
                    resolveSize(0, MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)),
                    resolveSize(0, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)));
        }
    }

    private int bottomTextHeight;

    private int computeHeight() {
        String[] textArray = viewPort.getTextArrayY();
        int count = textArray != null ? textArray.length : 0;

        setPaintAttribute(textAttribute);
        int maxHeight = 0;
        for (int i = 0; i < count; i++) {
            String text = textArray[i];
            textRect.setEmpty();
            paint.getTextBounds(text, 0, text.length(), textRect);

            if (maxHeight < textRect.height()) {
                maxHeight = textRect.height();
            }
        }
        return (int) (maxHeight + dpToPx(4));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            minDistanceItem = null;
        }

        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            int countY = getCountY();

            //遍历线上的点,找到与点击最近的点
            if (lineSeriesList == null || lineSeriesList.isEmpty()) {
                return super.onTouchEvent(event);
            }

            float x = event.getX();
            float y = event.getY();

            double totalMinDistance = Double.MAX_VALUE;
            for (LineDataSeries lineSeries : lineSeriesList) {
                List<LineDataSeries.LineDataPoint> pointList = lineSeries.getLineSeries();
                if (pointList == null || pointList.isEmpty()) {
                    continue;
                }

                double minDistance = Double.MAX_VALUE;
                int minDistanceItemIndex = 0;
                int index = 0;
                for (DataPoint point : pointList) {
                    float pointY = point.getY();
                    float actualY = countY - pointY;
                    float xDistance = point.getX() * viewPort.getSpacingX();
                    float yDistance = actualY * viewPort.getSpacingY();

                    double distance = Math.hypot(xDistance - x, yDistance - y);
                    if (distance < minDistance) {
                        minDistance = distance;
                        minDistanceItemIndex = index;
                    }
                    index++;
                }
                DataPoint childMinDistancePoint = pointList.get(minDistanceItemIndex);
                if (minDistance < totalMinDistance
                        && minDistance < dpToPx(20)) {
                    minDistanceItem = childMinDistancePoint;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public DataPoint getClickDataPoint() {
        return minDistanceItem;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (viewPort == null) {
            return;
        }

        //画x轴的刻度
        drawAxisXText(canvas);

        //画竖线
        drawVerticalLine(canvas);

        //画横线
        drawHorizontalLine(canvas);

        //画线条数据集
        drawLineSeries(canvas);

        //画矩形数据集
        drawRectSeries(canvas);
    }

    /**
     * 画x轴上的刻度
     */
    private void drawAxisXText(Canvas canvas) {
        int height = getHeight();
        float xCount = getCountX();
        int spacingX = viewPort.getSpacingX();
        String[] textArray = viewPort.getTextArrayX();
        for (int i = 1; i < xCount; i++) {
            textRect.setEmpty();
            String text = textArray[i];
            setPaintAttribute(textAttribute);
            paint.getTextBounds(text, 0, text.length(), textRect);
            int textWidth = textRect.width();

            int left = spacingX * i - textWidth / 2;
            setPaintAttribute(textAttribute);
            canvas.drawText(text, left, height, paint);
        }
    }

    private void drawVerticalLine(Canvas canvas) {
        int height = getHeight();
        float xCount = getCountX();
        int spacingX = viewPort.getSpacingX();
        for (int i = 0; i <= xCount; i++) {
            setPaintAttribute(lineAttribute);

            int lineLeft;
            if (i == 0) {
                lineLeft = Math.round(paint.getStrokeWidth());

            } else if (i == xCount) {
                lineLeft = spacingX * i - Math.round(paint.getStrokeWidth());

            } else {
                lineLeft = spacingX * i;
            }
            //画竖线
            if (i > 0) {
                setPaintAttribute(lineAttribute);
                canvas.drawLine(lineLeft, 0, lineLeft, height - bottomTextHeight, paint);
            }
        }
    }

    private void drawHorizontalLine(Canvas canvas) {
        float yCount = getCountY();
        int spacingY = viewPort.getSpacingY();
        setPaintAttribute(lineAttribute);
        for (int i = 0; i <= yCount; i++) {
            int lineBottom;
            if (i == 0) {
                lineBottom = Math.round(paint.getStrokeWidth());

            } else if (i == yCount) {
                lineBottom = spacingY * i - Math.round(paint.getStrokeWidth());

            } else {
                lineBottom = spacingY * i;
            }

            canvas.drawLine(0, lineBottom, getWidth(), lineBottom, paint);
        }
    }

    /**
     * 画线条数据集
     *
     * @param canvas
     */
    private void drawLineSeries(Canvas canvas) {
        if (lineSeriesList != null && !lineSeriesList.isEmpty()) {
            int countY = getCountY();
            for (LineDataSeries lineDataSeries : lineSeriesList) {
                linePath.reset();
                //画数据线
                List<LineDataSeries.LineDataPoint> lineSeries = lineDataSeries.getLineSeries();
                int index = 0;
                for (DataPoint linePoint : lineSeries) {
                    float y = linePoint.getY();
                    float actualY = countY - y;

                    float xDistance = linePoint.getX() * viewPort.getSpacingX();
                    float yDistance = actualY * viewPort.getSpacingY();

                    if (index == 0) {
                        linePath.moveTo(xDistance, yDistance);

                    } else {
                        linePath.lineTo(xDistance, yDistance);
                    }

                    int radius = 10;
                    paint.setStyle(FILL);
                    paint.setColor(lineDataSeries.getColor());
                    canvas.drawCircle(xDistance, yDistance, radius, paint);
                    index++;
                }

                int color = lineDataSeries.getColor();
                int strokeWidth = lineDataSeries.getWidth();
                setPaintAttribute(new PaintAttribute(color, strokeWidth, STROKE, 0));
                canvas.drawPath(linePath, paint);
            }
        }
    }

    /**
     * 画矩形数据集
     * 找出x坐标相等的点,
     *
     * @param canvas
     */
    private void drawRectSeries(Canvas canvas) {
        if (rectSeriesList != null && !rectSeriesList.isEmpty()) {
            for (RectDataSeries rectDataSeries : rectSeriesList) {
                //画数据线
                List<RectDataSeries.RectDataPoint> pointList = rectDataSeries.getRectSeries();
                for (RectDataSeries.RectDataPoint point : pointList) {
                    float x = point.getX();
                    List<RectDataSeries.RectDataPoint> dataPoints = rectSeriesMap.get(x);
                    if (dataPoints == null) {
                        dataPoints = new ArrayList<>();
                        rectSeriesMap.put(x, dataPoints);
                    }
                    if (!dataPoints.contains(point)) {
                        dataPoints.add(point);
                    }
                }
            }

            //重新计算x的值
            int size = rectSeriesMap.size();
            for (int i = 0; i < size; i++) {
                Float x = rectSeriesMap.keyAt(i);
                List<RectDataSeries.RectDataPoint> pointList = rectSeriesMap.valueAt(i);
                if (pointList != null && !pointList.isEmpty()) {
                    int index = 0;
                    int pointSize = pointList.size();
                    for (RectDataSeries.RectDataPoint point : pointList) {
                        float rectWidth = point.getWidthPercent();
                        float changedX = x - pointSize * rectWidth * 0.5f + index * rectWidth;
                        drawRect(canvas, point, changedX);
                        index++;
                    }
                }
            }

        }
    }

    private void drawRect(Canvas canvas, RectDataSeries.RectDataPoint point, float changedX) {
        float y = point.getY();
        final float x = changedX;
        int countY = getCountY();
        float actualY = countY - y;

        float left = x * viewPort.getSpacingX();
        float top = actualY * viewPort.getSpacingY();
        float right = viewPort.getSpacingX() * point.getWidthPercent() + left;
        float bottom = getHeight() - bottomTextHeight;

        paint.setColor(point.getColor());
        paint.setStyle(FILL);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    private void setPaintAttribute(PaintAttribute attribute) {
        if (attribute == null) {
            return;
        }

        paint.setAntiAlias(true);
        paint.setStrokeWidth(attribute.strokeWidth);
        paint.setColor(attribute.getColor());
        paint.setStyle(attribute.getStyle());
        paint.setTextSize(attribute.getTextSize());
    }

    /**
     * 加入一条线
     */
    public void addLineSeries(LineDataSeries lineSeries) {
        if (lineSeries == null) {
            return;
        }
        List<LineDataSeries.LineDataPoint> dataPointList = lineSeries.getLineSeries();
        Collections.sort(dataPointList);

        for (LineDataSeries.LineDataPoint lineDataPoint : dataPointList) {
            System.out.println("x="+lineDataPoint.getX());
        }
        this.lineSeriesList.add(lineSeries);
        invalidate();
    }


    public void addRectSeries(RectDataSeries rectDataSeries) {
        if (rectDataSeries == null) {
            return;
        }
        rectSeriesList.add(rectDataSeries);
        invalidate();
    }

    /**
     * 画笔属性
     */
    public static class PaintAttribute {

        private int color;

        private float strokeWidth;

        private Paint.Style style;

        private float textSize;

        public PaintAttribute(int color, float strokeWidth, Paint.Style style, float textSize) {
            this.color = color;
            this.strokeWidth = strokeWidth;
            this.style = style;
            this.textSize = textSize;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public float getStrokeWidth() {
            return strokeWidth;
        }

        public void setStrokeWidth(float strokeWidth) {
            this.strokeWidth = strokeWidth;
        }

        public Paint.Style getStyle() {
            return style;
        }

        public void setStyle(Paint.Style style) {
            this.style = style;
        }

        public float getTextSize() {
            return textSize;
        }

        public void setTextSize(float textSize) {
            this.textSize = textSize;
        }
    }
}
