package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tangt on 2016/2/23.
 */
public class PfSingleAssess extends BaseModel {
    @Expose
    private PfSingleAssessSun list;

    public PfSingleAssessSun getList() {
        return list;
    }

    public void setList(PfSingleAssessSun list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PfSingleAssess{" +
                "list=" + list +
                '}';
    }
}


