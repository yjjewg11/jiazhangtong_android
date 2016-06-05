package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * AddressBookMessage
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-13 17:02
 */
public class AddressBookMessage extends BaseModel {
    @Expose
    private AddressBookMsg list;
    @Expose
    private int pageSize;
    @Expose
    private int totalCount;
    @Expose
    private int pageNo;

    public AddressBookMsg getList() {
        return list;
    }

    public void setList(AddressBookMsg list) {
        this.list = list;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
