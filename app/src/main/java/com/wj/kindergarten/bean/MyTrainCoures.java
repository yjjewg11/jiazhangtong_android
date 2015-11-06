package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

<<<<<<< HEAD
public class MyTrainCoures {
    @Expose
    private String uuid;
    @Expose
    private String classuuid;
    @Expose
    private String student_headimg;
    @Expose
    private String group_name;
    @Expose
    private String class_name;
    @Expose
    private String plandate;
    @Expose
    private String name;
    @Expose
    private String address;
    @Expose
    private String readyfor;
    @Expose
    private int count;
    @Expose
    private DianZan dianzan;
    @Expose
    private MyReplyPage replyPage;

    public DianZan getDianZan() {
        return dianzan;
    }

    public void setDianZan(DianZan dianZan) {
        this.dianzan = dianZan;
    }

    public MyReplyPage getReplyPage() {
        return replyPage;
    }

    public void setReplyPage(MyReplyPage replyPage) {
        this.replyPage = replyPage;
    }

    @Override
    public String toString() {
        return "MyTrainCoures{" +
                "address='" + address + '\'' +
                ", uuid='" + uuid + '\'' +
                ", classuuid='" + classuuid + '\'' +
                ", student_headimg='" + student_headimg + '\'' +
                ", group_name='" + group_name + '\'' +
                ", class_name='" + class_name + '\'' +
                ", plandate='" + plandate + '\'' +
                ", name='" + name + '\'' +
                ", readyfor='" + readyfor + '\'' +
                ", count=" + count +
                ", dianzan=" + dianzan +
                ", replyPage=" + replyPage +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClassuuid() {
        return classuuid;
    }

    public void setClassuuid(String classuuid) {
        this.classuuid = classuuid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }



    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlandate() {
        return plandate;
    }

    public void setPlandate(String plandate) {
        this.plandate = plandate;
    }

    public String getReadyfor() {
        return readyfor;
    }

    public void setReadyfor(String readyfor) {
        this.readyfor = readyfor;
    }

    public String getStudent_headimg() {
        return student_headimg;
    }

    public void setStudent_headimg(String student_headimg) {
        this.student_headimg = student_headimg;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
=======
import java.util.List;

public class MyTrainCoures {
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Expose
    private int totalCount;
    @Expose
    private int pageSize;
    @Expose
    private int pageNo;
    @Expose
    private List<TrainCourseContent> data;

    public List<TrainCourseContent> getData() {
        return data;
    }

    public void setDate(List<TrainCourseContent> data) {
        this.data = data;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "MyTrainCoures{" +
                "data=" + data +
                ", totalCount=" + totalCount +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                '}';
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    }
}
