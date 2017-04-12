package com.example.administrator.myapplication.widget;

/**
 * 坐标系视图
 * Created by cai.jia on 2017/3/19.
 */

public class ViewPort {

    /**
     * x轴间隔
     */
    private int spacingX;

    private String[] textArrayX;

    private int spacingY;

    private String[] textArrayY;

    public ViewPort() {
    }

    public void setSpacingX(int spacingX) {
        this.spacingX = spacingX;
    }

    public void setTextArrayX(String[] textArrayX) {
        this.textArrayX = textArrayX;
    }

    public void setSpacingY(int spacingY) {
        this.spacingY = spacingY;
    }

    public void setTextArrayY(String[] textArrayY) {
        this.textArrayY = textArrayY;
    }

    public int getSpacingX() {
        return spacingX;
    }

    public String[] getTextArrayX() {
        return textArrayX;
    }

    public int getSpacingY() {
        return spacingY;
    }

    public String[] getTextArrayY() {
        return textArrayY;
    }
}
