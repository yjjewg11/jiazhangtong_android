package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.widget.EditText;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * EditRemarkActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/26 1:38
 */
public class EditRemarkActivity extends BaseActivity {
    private EditText remarkEt;
    private ChildInfo childInfo = null;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_edit_remark;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("修改备注", "保存");

        remarkEt = (EditText) findViewById(R.id.child_remark);
        childInfo = (ChildInfo) getIntent().getSerializableExtra("childInfo");
        if (null != childInfo) {
            remarkEt.setText(childInfo.getNote());
        }
        remarkEt.setSelection(remarkEt.getText().length());
    }

    @Override
    protected void titleRightButtonListener() {
        saveInfo();
    }

    private void saveInfo() {
        if (!Utils.stringIsNull(remarkEt.getText().toString())) {
            final HintInfoDialog dialog = new HintInfoDialog(EditRemarkActivity.this, "信息保存中，请稍后...");
            dialog.show();
            childInfo.setNote(remarkEt.getText().toString().trim());
            UserRequest.changeChild(mContext, childInfo,"save", new RequestResultI() {
                @Override
                public void result(BaseModel domain) {
                    dialog.dismiss();
                    Intent parentIntent = new Intent();
                    parentIntent.putExtra("remark", remarkEt.getText().toString());
                    parentIntent.putExtra("uuid",childInfo.getUuid());
                    setResult(RESULT_OK, parentIntent);
                    finish();
                }

                @Override
                public void result(List<BaseModel> domains, int total) {

                }

                @Override
                public void failure(String message) {
                    dialog.dismiss();
                    if (!Utils.stringIsNull(message)) {
                        Utils.showToast(mContext, message);
                    }
                }
            });
        } else {
            Utils.showToast(EditRemarkActivity.this, "备注信息不能为空");
        }
    }
}
