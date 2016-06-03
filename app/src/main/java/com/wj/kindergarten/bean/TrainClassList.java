package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

import java.util.List;

public class TrainClassList extends BaseModel{
    @Expose
    private List<TrainClass> trainClass;

    public List<TrainClass> getTrainClass() {
        return trainClass;
    }

    public void setTrainClass(List<TrainClass> trainClass) {
        this.trainClass = trainClass;
    }
}
