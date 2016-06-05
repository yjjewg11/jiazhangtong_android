package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class GetAssessStateList extends BaseModel{
    @Expose
    private GetAssessStateListSun list;

    public GetAssessStateListSun getList() {
        return list;
    }

    public void setList(GetAssessStateListSun list) {
        this.list = list;
    }
}
