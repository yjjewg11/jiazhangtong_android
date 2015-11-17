package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

import java.util.List;

public class TrainChildInfoList extends BaseModel{

    //孩子属性集合
    @Expose
    private List<TrainChildInfo> list;

    //孩子班级uuid，和名称
    @Expose
    private List<TrainClass> class_list;

    public List<TrainClass> getClass_list() {
        return class_list;
    }

    public void setClass_list(List<TrainClass> class_list) {
        this.class_list = class_list;
    }

    public List<TrainChildInfo> getList() {
        return list;
    }

    public void setList(List<TrainChildInfo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "TrainChildInfoList{" +
                "class_list=" + class_list +
                ", list=" + list +
                '}';
    }
}
