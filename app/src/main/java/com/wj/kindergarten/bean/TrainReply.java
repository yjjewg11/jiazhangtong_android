package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

import java.util.List;

public class TrainReply extends BaseModel {@Expose
    private List<Reply> data;

    @Override
    public String toString() {
        return "TrainReply{" +
                "data=" + data +
                '}';
    }

    public List<Reply> getData() {
        return data;
    }

    public void setData(List<Reply> data) {
        this.data = data;
    }
}
