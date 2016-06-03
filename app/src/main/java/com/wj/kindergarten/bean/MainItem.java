package com.wj.kindergarten.bean;

/**
 * MainItem
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/19 21:24
 */
public class MainItem extends BaseModel {
    private int imageResource;
    private String tag;
    private String text;

    public MainItem() {
    }

    public MainItem(int imageResource, String text, String tag) {
        this.imageResource = imageResource;
        this.text = text;
        this.tag = tag;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
