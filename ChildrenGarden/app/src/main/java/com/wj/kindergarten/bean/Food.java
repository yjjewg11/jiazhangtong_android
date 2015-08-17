package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Food
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/2 23:15
 */
public class Food extends BaseModel {
    @Expose
    private String time_1;
    @Expose
    private int count;
    @Expose
    private String plandate;
    @Expose
    private String groupuuid;
    @Expose
    private String analysis;
    @Expose
    private String share_url;
    @Expose
    private String time_4;
    @Expose
    private String time_5;
    @Expose
    private String time_2;
    @Expose
    private String time_3;
    @Expose
    private String uuid;
    @Expose
    private List<CookBook> list_time_1;
    @Expose
    private List<CookBook> list_time_2;
    @Expose
    private List<CookBook> list_time_3;
    @Expose
    private List<CookBook> list_time_4;
    @Expose
    private List<CookBook> list_time_5;
    @Expose
    private DianZan dianzan;
    @Expose
    private ChildReplyList replyPage;


    public String getTime_1() {
        return time_1;
    }

    public void setTime_1(String time_1) {
        this.time_1 = time_1;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPlandate() {
        return plandate;
    }

    public void setPlandate(String plandate) {
        this.plandate = plandate;
    }

    public String getGroupuuid() {
        return groupuuid;
    }

    public void setGroupuuid(String groupuuid) {
        this.groupuuid = groupuuid;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getTime_4() {
        return time_4;
    }

    public void setTime_4(String time_4) {
        this.time_4 = time_4;
    }

    public String getTime_5() {
        return time_5;
    }

    public void setTime_5(String time_5) {
        this.time_5 = time_5;
    }

    public String getTime_2() {
        return time_2;
    }

    public void setTime_2(String time_2) {
        this.time_2 = time_2;
    }

    public String getTime_3() {
        return time_3;
    }

    public void setTime_3(String time_3) {
        this.time_3 = time_3;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<CookBook> getList_time_1() {
        return list_time_1;
    }

    public void setList_time_1(List<CookBook> list_time_1) {
        this.list_time_1 = list_time_1;
    }

    public List<CookBook> getList_time_2() {
        return list_time_2;
    }

    public void setList_time_2(List<CookBook> list_time_2) {
        this.list_time_2 = list_time_2;
    }

    public List<CookBook> getList_time_3() {
        return list_time_3;
    }

    public void setList_time_3(List<CookBook> list_time_3) {
        this.list_time_3 = list_time_3;
    }

    public List<CookBook> getList_time_4() {
        return list_time_4;
    }

    public void setList_time_4(List<CookBook> list_time_4) {
        this.list_time_4 = list_time_4;
    }

    public List<CookBook> getList_time_5() {
        return list_time_5;
    }

    public void setList_time_5(List<CookBook> list_time_5) {
        this.list_time_5 = list_time_5;
    }

    public DianZan getDianzan() {
        return dianzan;
    }

    public void setDianzan(DianZan dianzan) {
        this.dianzan = dianzan;
    }

    public ChildReplyList getReplyPage() {
        return replyPage;
    }

    public void setReplyPage(ChildReplyList replyPage) {
        this.replyPage = replyPage;
    }

    public class CookBook extends BaseModel {
        @Expose
        private String img;
        @Expose
        private String groupuuid;
        @Expose
        private String uuid;
        @Expose
        private int type;
        @Expose
        private String name;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getGroupuuid() {
            return groupuuid;
        }

        public void setGroupuuid(String groupuuid) {
            this.groupuuid = groupuuid;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
