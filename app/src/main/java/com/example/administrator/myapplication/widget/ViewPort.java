package com.example.administrator.myapplication.widget;

/**
 * 坐标系视图
 * Created by cai.jia on 2017/3/19.
 */

public class ViewPort {

    private float minX;

    /**
     * x坐标最大值
     */
    private float maxX;

    /**
     * x轴间隔
     */
    private int spacingX;

    private int xCount;

    private int yCount;

    private float minY;
    /**
     * y坐标最大值
     */
    private float maxY;

    /**
     * y坐标间隔
     */
    private int spacingY;

    public ViewPort() {
    }

    public ViewPort(float minX, float maxX, int spacingX, int xCount,
                    float minY, float maxY, int spacingY,int yCount) {

        this.minX = minX;
        this.maxX = maxX;
        this.spacingX = spacingX;
        this.xCount = xCount;
        this.yCount = yCount;
        this.minY = minY;
        this.maxY = maxY;
        this.spacingY = spacingY;
    }

    public float getMinX() {

        return minX;
    }

    public void setMinX(float minX) {
        this.minX = minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    public int getSpacingX() {
        return spacingX;
    }

    public void setSpacingX(int spacingX) {
        this.spacingX = spacingX;
    }

    public int getxCount() {
        return xCount;
    }

    public void setxCount(int xCount) {
        this.xCount = xCount;
    }

    public int getyCount() {
        return yCount;
    }

    public void setyCount(int yCount) {
        this.yCount = yCount;
    }

    public float getMinY() {
        return minY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public int getSpacingY() {
        return spacingY;
    }

    public void setSpacingY(int spacingY) {
        this.spacingY = spacingY;
    }

    public float getStepY() {
        float v = (maxY - minY) / yCount;
        return v;
    }

    public float getStepX(){
        float v = (maxX - minX) / xCount;
        return v;
    }
}
