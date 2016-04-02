package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tangt on 2016/3/1.
 */
public class PfAlbumInfo extends BaseModel {
    @Expose
    private PfAlbumInfoSun data;@Expose
    private List<PFAlbumMember> members_list;

    @Override
    public String toString() {
        return "PfAlbumInfo{" +
                "data=" + data +
                ", members_list=" + members_list +
                '}';
    }

    public List<PFAlbumMember> getMembers_list() {
        return members_list;
    }

    public void setMembers_list(List<PFAlbumMember> members_list) {
        this.members_list = members_list;
    }

    public PfAlbumInfoSun getData() {
        return data;
    }

    public void setData(PfAlbumInfoSun data) {
        this.data = data;
    }
}
