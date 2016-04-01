package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/3/26.
 */
public class BoutiqueDianzanListFather extends BaseModel {
    @Expose
    private BoutiqueDianzanList dianZanNameList;

    @Override
    public String toString() {
        return "BoutiqueDianzanListFather{" +
                "dianZanNameList=" + dianZanNameList +
                '}';
    }

    public BoutiqueDianzanList getDianZanNameList() {
        return dianZanNameList;
    }

    public void setDianZanNameList(BoutiqueDianzanList dianZanNameList) {
        this.dianZanNameList = dianZanNameList;
    }
}
