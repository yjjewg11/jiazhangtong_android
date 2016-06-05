package com.wj.kindergarten.net.upload;

import com.google.gson.annotations.Expose;

/**
 * Image
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-25 16:35
 */
public class Image {
    @Expose
    private String create_time;
    @Expose
    private String user_uuid;
    @Expose
    private String uuid;
    @Expose
    private int file_size;
    @Expose
    private int type;
    @Expose
    private String file_path;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUser_uuid() {
        return user_uuid;
    }

    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getFile_size() {
        return file_size;
    }

    public void setFile_size(int file_size) {
        this.file_size = file_size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
}
