package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.TeacherDetail;

/**
 * Created by Administrator on 2015/10/28.
 */
public class TeacherDetailInfo extends BaseModel {
    @Expose
    private TeacherDetail data;

    public TeacherDetail getData() {
        return data;
    }

    public void setData(TeacherDetail data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TeacherDetailInfo{" +
                "data=" + data +
                '}';
    }
}
