package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2015/12/9.
 */
public class FoundTypeCountSun extends BaseModel{
    @Expose
    private int today_goodArticle;@Expose
    private int today_snsTopic;@Expose
    private int today_pxbenefit;@Expose
    private int today_unreadPushMsg;

    public int getToday_goodArticle() {
        return today_goodArticle;
    }

    public void setToday_goodArticle(int today_goodArticle) {
        this.today_goodArticle = today_goodArticle;
    }

    public int getToday_snsTopic() {
        return today_snsTopic;
    }

    public void setToday_snsTopic(int today_snsTopic) {
        this.today_snsTopic = today_snsTopic;
    }

    public int getToday_pxbenefit() {
        return today_pxbenefit;
    }

    public void setToday_pxbenefit(int today_pxbenefit) {
        this.today_pxbenefit = today_pxbenefit;
    }

    public int getToday_unreadPushMsg() {
        return today_unreadPushMsg;
    }

    public void setToday_unreadPushMsg(int today_unreadPushMsg) {
        this.today_unreadPushMsg = today_unreadPushMsg;
    }

    @Override
    public String toString() {
        return "FoundTypeCountSun{" +
                "today_goodArticle=" + today_goodArticle +
                ", today_snsTopic=" + today_snsTopic +
                ", today_pxbenefit=" + today_pxbenefit +
                ", today_unreadPushMsg=" + today_unreadPushMsg +
                '}';
    }
}
