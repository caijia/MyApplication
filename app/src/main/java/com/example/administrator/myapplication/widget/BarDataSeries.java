package com.example.administrator.myapplication.widget;

import java.util.ArrayList;
import java.util.List;

/**
 * 线条数据集
 * Created by cai.jia on 2017/3/20 0020
 */

public class BarDataSeries {

    /**
     * 矩形点集合
     */
    private List<DataPoint> lineSeries;

    /**
     * 矩形的颜色
     */
    private int color;

    /**
     * 矩形的宽度
     */
    private int width;

    public BarDataSeries() {
        lineSeries = new ArrayList<>();
    }

    public void addDataPoint(DataPoint dataPoint) {
        if (dataPoint == null) {
            return;
        }
        lineSeries.add(dataPoint);
    }

    public void addDataPointList(List<DataPoint> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        lineSeries.addAll(list);
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

    public List<DataPoint> getLineSeries() {
        return lineSeries;
    }

    public void setLineSeries(List<DataPoint> lineSeries) {
        this.lineSeries = lineSeries;
    }
}
