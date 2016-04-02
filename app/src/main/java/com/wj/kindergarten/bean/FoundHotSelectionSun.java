package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tangt on 2015/12/9.
 */
public class FoundHotSelectionSun extends BaseModel{
    @Expose
    private String summary;@Expose
    private String title;@Expose
    private int level;@Expose
    private int reply_count;@Expose
    private int yes_count;@Expose
    private int status;@Expose
    private String create_time;@Expose
    private String uuid;@Expose
    private int no_count;@Expose
    private String imguuids;@Expose
    private String webview_url;@Expose
    private String create_useruuid;@Expose
    private List<String> imgList;

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setReply_count(int reply_count) {
        this.reply_count = reply_count;
    }

    public void setYes_count(int yes_count) {
        this.yes_count = yes_count;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setNo_count(int no_count) {
        this.no_count = no_count;
    }

    public void setImguuids(String imguuids) {
        this.imguuids = imguuids;
    }

    public void setWebview_url(String webview_url) {
        this.webview_url = webview_url;
    }

    public void setCreate_useruuid(String create_useruuid) {
        this.create_useruuid = create_useruuid;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getSummary() {
        return summary;
    }

    public String getTitle() {
        return title;
    }

    public int getLevel() {
        return level;
    }

    public int getReply_count() {
        return reply_count;
    }

    public int getYes_count() {
        return yes_count;
    }

    public int getStatus() {
        return status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getUuid() {
        return uuid;
    }

    public int getNo_count() {
        return no_count;
    }

    public String getImguuids() {
        return imguuids;
    }

    public String getWebview_url() {
        return webview_url;
    }

    public String getCreate_useruuid() {
        return create_useruuid;
    }

    public List<String> getImgList() {
        return imgList;
    }

    @Override
    public String toString() {
        return "FoundHotSelectionSun{" +
                "summary='" + summary + '\'' +
                ", title='" + title + '\'' +
                ", level=" + level +
                ", reply_count=" + reply_count +
                ", yes_count=" + yes_count +
                ", status=" + status +
                ", create_time='" + create_time + '\'' +
                ", uuid='" + uuid + '\'' +
                ", no_count=" + no_count +
                ", imguuids='" + imguuids + '\'' +
                ", webview_url='" + webview_url + '\'' +
                ", create_useruuid='" + create_useruuid + '\'' +
                ", imgList=" + imgList +
                '}';
    }
}
