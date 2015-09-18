package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * CourseList
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/2 17:05
 */
public class CourseList extends BaseModel{
//    @Expose
//    private DataCourseList list;
//
//    public DataCourseList getList() {
//        return list;
//    }
//
//    public void setList(DataCourseList list) {
//        this.list = list;
//    }

    @Expose
    private List<Course> list;

    public List<Course> getList() {
        return list;
    }

    public void setList(List<Course> list) {
        this.list = list;
    }
}
