package com.wj.kindergarten.bean;

import java.io.Serializable;

/**
 * Created by tangt on 2016/1/27.
 */
public class QueryGroupCount  implements Serializable{
    private String date;
    private int count;
    private String hearld;


    public String getHearld() {
        return hearld;
    }

    public void setHearld(String hearld) {
        this.hearld = hearld;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryGroupCount that = (QueryGroupCount) o;

        return date.equals(that.date);

    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }
}
