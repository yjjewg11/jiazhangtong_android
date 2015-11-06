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
<<<<<<< HEAD

    @Override
    public String toString() {
        return "TrainSchoolInfoListFather{" +
                "list=" + list +
                '}';
    }
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
}
