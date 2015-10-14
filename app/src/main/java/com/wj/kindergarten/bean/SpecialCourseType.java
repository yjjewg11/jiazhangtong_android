package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class SpecialCourseType extends BaseModel {

    @Expose
    private int datakey;
    @Expose
    private String datavalue;
    @Expose
    private String description;

    public int getDatakey() {
        return datakey;
    }

    public void setDatakey(int datakey) {
        this.datakey = datakey;
    }

    public String getDatavalue() {
        return datavalue;
    }

    public void setDatavalue(String datavalue) {
        this.datavalue = datavalue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SpecialCourseType{" +
                "datakey=" + datakey +
                ", datavalue='" + datavalue + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
