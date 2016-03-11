package com.wj.kindergarten.abstractbean;

import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.utils.ToastUtils;

import java.util.List;

/**
 * Created by tangt on 2016/3/10.
 */
public abstract class RequestFailedResult implements RequestResultI{
    @Override
    public abstract void result(BaseModel domain);

    @Override
    public abstract void result(List<BaseModel> domains, int total);

    @Override
    public void failure(String message) {
        ToastUtils.showMessage(message);
    }
}
