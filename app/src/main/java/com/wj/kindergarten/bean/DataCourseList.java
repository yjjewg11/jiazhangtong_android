package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * DataCourseList
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/2 17:04
 */
public class DataCourseList extends BaseListModel{
    @Expose
    private List<Course> data;

    public List<Course> getData() {
        return data;
    }

    public void setData(List<Course> data) {
        this.data = data;
    }
}
