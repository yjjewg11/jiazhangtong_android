package com.wj.kindergarten.bean;

/**
 * Created by tangt on 2015/12/8.
 */
public class FoundGridItem {
    public FoundGridItem(String name, int iv, int count) {
        this.name = name;
        this.iv = iv;
        this.count = count;
    }

    String name;
    int iv,count;

    public FoundGridItem( String name,int iv) {
        this.iv = iv;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIv() {
        return iv;
    }

    public void setIv(int iv) {
        this.iv = iv;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
