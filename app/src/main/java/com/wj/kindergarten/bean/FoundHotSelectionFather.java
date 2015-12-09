package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2015/12/9.
 */
public class FoundHotSelectionFather extends BaseModel {
    @Expose
    private FoundHotSelection list;

    public FoundHotSelection getList() {
        return list;
    }

    public void setList(FoundHotSelection list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "FoundHotSelectionFather{" +
                "list=" + list +
                '}';
    }
}
