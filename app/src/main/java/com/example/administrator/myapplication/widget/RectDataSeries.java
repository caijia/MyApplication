package com.example.administrator.myapplication.widget;

import java.util.ArrayList;
import java.util.List;

/**
 * 线条数据集
 * Created by cai.jia on 2017/3/20 0020
 */

public class RectDataSeries {

    /**
     * 矩形集合
     */
    private List<RectDataPoint> rectSeries;

    /**
     * 线条的颜色
     */
    private int color;

    /**
     * 线条的宽度,为spacingX的百分比  比如0.2f
     */
    private float widthPercent;

    public RectDataSeries(int color, float widthPercent) {
        rectSeries = new ArrayList<>();
        this.color = color;
        this.widthPercent = widthPercent;
    }

    public void addDataPoint(DataPoint dataPoint) {
        if (dataPoint == null) {
            return;
        }
        RectDataPoint point = new RectDataPoint(dataPoint, color,widthPercent);
        rectSeries.add(point);
    }

    public void addDataPointList(List<DataPoint> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (DataPoint dataPoint : list) {
            addDataPoint(dataPoint);
        }
    }

    public int getColor() {
        return color;
    }

    public List<RectDataPoint> getRectSeries() {
        return rectSeries;
    }

    public void setRectSeries(List<RectDataPoint> rectSeries) {
        this.rectSeries = rectSeries;
    }

    public float getWidthPercent() {
        return widthPercent;
    }

    public static class RectDataPoint implements DataPoint {

        private float x;
        private float y;
        private int color;
        private float widthPercent;

        public RectDataPoint() {
        }

        public RectDataPoint(DataPoint point, int color,float widthPercent) {
            this.x = point.getX();
            this.y = point.getY();
            this.color = color;
            this.widthPercent = widthPercent;
        }

        @Override
        public float getX() {
            return x;
        }

        @Override
        public void setX(float x) {
            this.x = x;
        }

        @Override
        public float getY() {
            return y;
        }

        @Override
        public void setY(float y) {
            this.y = y;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public float getWidthPercent() {
            return widthPercent;
        }

        public void setWidthPercent(float widthPercent) {
            this.widthPercent = widthPercent;
        }
    }
}
