package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * FoodList
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/2 23:14
 */
public class FoodList extends BaseModel {
    @Expose
    private List<Food> list;

    public List<Food> getList() {
        return list;
    }

    public void setList(List<Food> list) {
        this.list = list;
    }
}
