package com.example.administrator.myapplication.rx.retrofit;

/**
 * Created by cai.jia on 2017/4/14 0014
 */

public class Result {


    /**
     * IsShow : false
     * IsShowActivity : false
     * persons : 100
     * pictureBox :
     * theme :
     * lotteryTicketsUrl :
     * defaultChannel : 1
     */

    private boolean IsShow;
    private boolean IsShowActivity;
    private int persons;
    private String pictureBox;
    private String theme;
    private String lotteryTicketsUrl;
    private int defaultChannel;

    public boolean isIsShow() {
        return IsShow;
    }

    public void setIsShow(boolean IsShow) {
        this.IsShow = IsShow;
    }

    public boolean isIsShowActivity() {
        return IsShowActivity;
    }

    public void setIsShowActivity(boolean IsShowActivity) {
        this.IsShowActivity = IsShowActivity;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public String getPictureBox() {
        return pictureBox;
    }

    public void setPictureBox(String pictureBox) {
        this.pictureBox = pictureBox;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLotteryTicketsUrl() {
        return lotteryTicketsUrl;
    }

    public void setLotteryTicketsUrl(String lotteryTicketsUrl) {
        this.lotteryTicketsUrl = lotteryTicketsUrl;
    }

    public int getDefaultChannel() {
        return defaultChannel;
    }

    public void setDefaultChannel(int defaultChannel) {
        this.defaultChannel = defaultChannel;
    }

    @Override
    public String toString() {
        return "Result{" +
                "IsShow=" + IsShow +
                ", IsShowActivity=" + IsShowActivity +
                ", persons=" + persons +
                ", pictureBox='" + pictureBox + '\'' +
                ", theme='" + theme + '\'' +
                ", lotteryTicketsUrl='" + lotteryTicketsUrl + '\'' +
                ", defaultChannel=" + defaultChannel +
                '}';
    }
}
