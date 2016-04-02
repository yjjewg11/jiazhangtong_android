package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tangt on 2016/2/18.
 */
public class PfModeName extends BaseModel {
    @Expose
    private PfModeNameSun list;
    /**
     * pageSize : 20
     * pageNo : 1
     * data : [{"mp3":null,"herald":null,"title":"缺省模版","key":"fp_movie1"}]
     * totalCount : 1
     */



    public PfModeNameSun getList() {
        return list;
    }

    public void setList(PfModeNameSun list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PfModeName{" +
                "list=" + list +
                '}';
    }




}
