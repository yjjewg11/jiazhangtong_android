package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;
import java.util.List;

public class TrainCourse extends BaseModel{

    @Expose
    private List<MyTrainCoures> list;

    public List<MyTrainCoures> getList() {
        return list;
    }

    public void setList(List<MyTrainCoures> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "TrainCourse{" +
                "list=" + list +
                '}';
    }

    //    @Expose
//    private String uuid;
//    @Expose
//    private String plandate;// 时间.年月日,时分秒.
//    @Expose
//    private String context;// 课程详细内容.不限字数
//    @Expose
//    private String readyfor;// 需要学生准备的工具.不限字数
//    @Expose
//    private String classuuid;// 关联班级uuid(显示班级名)
//    @Expose
//    private String create_useruuid;
//    @Expose
//    private String duration;// 课程时长
//    @Expose
//    private String name;// 课程名称
////    @Expose
////    private String address;// 上课地点
//
//
//    public String getClassuuid() {
//        return classuuid;
//    }
//
//    public void setClassuuid(String classuuid) {
//        this.classuuid = classuuid;
//    }
//
//
//    public String getCreate_useruuid() {
//        return create_useruuid;
//    }
//
//    public void setCreate_useruuid(String create_useruuid) {
//        this.create_useruuid = create_useruuid;
//    }
//
//    public String getDuration() {
//        return duration;
//    }
//
//    public void setDuration(String duration) {
//        this.duration = duration;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//
//    @Override
//    public String toString() {
//        return "TrainCourse{" +
//                ", classuuid='" + classuuid + '\'' +
//                ", create_useruuid='" + create_useruuid + '\'' +
//                ", duration='" + duration + '\'' +
//                ", name='" + name + '\'' +
//                '}';
//    }
}
