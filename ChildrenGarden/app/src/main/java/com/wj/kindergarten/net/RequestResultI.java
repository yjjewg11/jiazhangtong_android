package com.wj.kindergarten.net;


import com.wj.kindergarten.bean.BaseModel;

import java.util.List;

/**
 * RequestResultI
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/5/21 17:00
 */
public interface RequestResultI {
    void result(BaseModel domain);

    void result(List<BaseModel> domains, int total);

    void failure(String message);
}
