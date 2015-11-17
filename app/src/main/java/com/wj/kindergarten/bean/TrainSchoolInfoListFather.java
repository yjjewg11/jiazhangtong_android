package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class TrainSchoolInfoListFather extends BaseModel{
    @Expose
    private TrainSchoolInfoList list;

    public TrainSchoolInfoList getList() {
        return list;
    }

    public void setList(TrainSchoolInfoList list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "TrainSchoolInfoListFather{" +
                "list=" + list +
                '}';
    }

}
