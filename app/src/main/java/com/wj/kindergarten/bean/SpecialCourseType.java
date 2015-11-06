package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class SpecialCourseType extends BaseModel {

    @Expose
    private int datakey;
    @Expose
    private String datavalue;
    @Expose
<<<<<<< HEAD
    private String img;
=======
    private String description;
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

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

<<<<<<< HEAD
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
=======
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    }

    @Override
    public String toString() {
        return "SpecialCourseType{" +
                "datakey=" + datakey +
                ", datavalue='" + datavalue + '\'' +
<<<<<<< HEAD
                ", img='" + img + '\'' +
=======
                ", description='" + description + '\'' +
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
                '}';
    }
}
