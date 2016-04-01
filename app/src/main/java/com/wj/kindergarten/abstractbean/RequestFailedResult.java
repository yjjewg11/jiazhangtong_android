package com.wj.kindergarten.abstractbean;

import android.app.Dialog;

import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.utils.ToastUtils;

import java.util.List;

/**
 * Created by tangt on 2016/3/10.
 */
public abstract class RequestFailedResult implements RequestResultI{
    public RequestFailedResult() {
    }
    private Dialog dialog;

    public RequestFailedResult(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public abstract void result(BaseModel domain);

    @Override
    public abstract void result(List<BaseModel> domains, int total);

    @Override
    public void failure(String message) {
        ToastUtils.showMessage(message);
        if(dialog != null && dialog.isShowing()) dialog.cancel();
    }
}
