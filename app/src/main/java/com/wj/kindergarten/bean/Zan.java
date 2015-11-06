package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Zan
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-18 14:51
 */
<<<<<<< HEAD
public class Zan extends BaseModel{
=======
public class Zan {
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    @Expose
    private int count;
    @Expose
    private String names;
    @Expose
    private boolean canDianzan;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public boolean isCanDianzan() {
        return canDianzan;
    }

    public void setCanDianzan(boolean canDianzan) {
        this.canDianzan = canDianzan;
    }
}
