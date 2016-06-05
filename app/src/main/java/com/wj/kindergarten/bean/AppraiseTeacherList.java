package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * AppraiseTeacherList
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/6 10:25
 */
public class AppraiseTeacherList extends BaseModel {
    @Expose
    private List<AppraiseTeacher> list;
    @Expose
    private List<AppraiseTeacherOver> list_judge;

    public List<AppraiseTeacher> getList() {
        return list;
    }

    public void setList(List<AppraiseTeacher> list) {
        this.list = list;
    }

    public List<AppraiseTeacherOver> getList_judge() {
        return list_judge;
    }

    public void setList_judge(List<AppraiseTeacherOver> list_judge) {
        this.list_judge = list_judge;
    }
}
