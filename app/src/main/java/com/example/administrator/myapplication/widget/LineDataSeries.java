package com.example.administrator.myapplication.widget;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 线条数据集
 * Created by cai.jia on 2017/3/20 0020
 */

public class LineDataSeries {

    /**
     * 线上面的点集合
     */
    private List<LineDataPoint> lineSeries;

    /**
     * 线条的颜色
     */
    private int color;

    /**
     * 线条的宽度
     */
    private int width;

    public LineDataSeries() {
        lineSeries = new ArrayList<>();
    }

    public LineDataSeries(int color, int width) {
        this.color = color;
        this.width = width;
        lineSeries = new ArrayList<>();
    }

    public void addDataPoint(DataPoint dataPoint) {
        if (dataPoint == null) {
            return;
        }
        LineDataPoint lineDataPoint = new LineDataPoint(dataPoint);
        lineSeries.add(lineDataPoint);
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

    public void setColor(int color) {
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<LineDataPoint> getLineSeries() {
        return lineSeries;
    }

    public static class LineDataPoint implements DataPoint,Comparable<LineDataPoint>{

        private DataPoint dataPoint;

        public LineDataPoint(DataPoint dataPoint) {
            this.dataPoint = dataPoint;
        }

        @Override
        public float getX() {
            return dataPoint.getX();
        }

        @Override
        public float getY() {
            return dataPoint.getY();
        }

        @Override
        public int compareTo(@NonNull LineDataPoint another) {
            return ((Float)getX()).compareTo(another.getX());
        }
    }
}
