package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class TrainCourseContent {
    @Expose
    private String uuid;
    @Expose
    private int count;
    @Expose
    private String plandate;
    @Expose
    private String context;
    @Expose
    private String readyfor;
    @Expose
    private String classuuid;
    @Expose
    private String create_useruuid;
    @Expose
    private String duration;
    @Expose
    private String name;
    @Expose
    private String address;
    @Expose
    private TrainReplyPage replyPage;
    @Expose
    private DianZan dianzan;
    @Override
    public String toString() {
        return "TrainCourseContent{" +
                "address='" + address + '\'' +
                ", uuid='" + uuid + '\'' +
                ", count='" + count + '\'' +
                ", plandate='" + plandate + '\'' +
                ", context='" + context + '\'' +
                ", readyfor='" + readyfor + '\'' +
                ", classuuid='" + classuuid + '\'' +
                ", create_useruuid='" + create_useruuid + '\'' +
                ", duration='" + duration + '\'' +
                ", name='" + name + '\'' +
                ", replyPage=" + replyPage +
                ", dianzan=" + dianzan +
                '}';
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClassuuid() {
        return classuuid;
    }

    public void setClassuuid(String classuuid) {
        this.classuuid = classuuid;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }



    public String getCreate_useruuid() {
        return create_useruuid;
    }

    public void setCreate_useruuid(String create_useruuid) {
        this.create_useruuid = create_useruuid;
    }

    public DianZan getDianzan() {
        return dianzan;
    }

    public void setDianzan(DianZan dianzan) {
        this.dianzan = dianzan;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    public TrainReplyPage getReplyPage() {
        return replyPage;
    }

    public void setReplyPage(TrainReplyPage replyPage) {
        this.replyPage = replyPage;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
