package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

public class OnceSpecialCourseList extends BaseModel{
    @Expose
    private OnceSpecialCourse data;

    public OnceSpecialCourse getData() {
        return data;
    }

    public void setData(OnceSpecialCourse data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OnceSpecialCourseList{" +
                "data=" + data +
                '}';
    }
}
