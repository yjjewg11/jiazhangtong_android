package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.EditTextCleanWatcher;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * EditParentsActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/26 1:38
 */
public class EditOthersActivity extends BaseActivity {
    private EditText telEt1;
    private EditText telEt2;
    private EditText telEt3;
    private EditText telEt4;
    private ChildInfo childInfo = null;
    private RelativeLayout layoutClean1 = null;
    private RelativeLayout layoutClean2 = null;
    private RelativeLayout layoutClean3 = null;
    private RelativeLayout layoutClean4 = null;
    private ImageView ivClean1 = null;
    private ImageView ivClean2 = null;
    private ImageView ivClean3 = null;
    private ImageView ivClean4 = null;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_edit_others;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("修改信息", "保存");

        initViews();

        childInfo = (ChildInfo) getIntent().getSerializableExtra("childInfo");
        telEt1.setText(childInfo.getYe_tel());
        telEt2.setText(childInfo.getNai_tel());
        telEt3.setText(childInfo.getWaigong_tel());
        telEt4.setText(childInfo.getWaipo_tel());
    }

    private void initViews() {
        telEt1 = (EditText) findViewById(R.id.child_tel_1);
        telEt2 = (EditText) findViewById(R.id.child_tel_2);
        telEt3 = (EditText) findViewById(R.id.child_tel_3);
        telEt4 = (EditText) findViewById(R.id.child_tel_4);
        layoutClean1 = (RelativeLayout) findViewById(R.id.ll_clean_1);
        layoutClean2 = (RelativeLayout) findViewById(R.id.ll_clean_2);
        layoutClean3 = (RelativeLayout) findViewById(R.id.ll_clean_3);
        layoutClean4 = (RelativeLayout) findViewById(R.id.ll_clean_4);
        ivClean1 = (ImageView) findViewById(R.id.iv_clean_1);
        ivClean2 = (ImageView) findViewById(R.id.iv_clean_2);
        ivClean3 = (ImageView) findViewById(R.id.iv_clean_3);
        ivClean4 = (ImageView) findViewById(R.id.iv_clean_4);

        telEt1.addTextChangedListener(new EditTextCleanWatcher(ivClean1, telEt1));
        telEt2.addTextChangedListener(new EditTextCleanWatcher(ivClean2, telEt2));
        telEt3.addTextChangedListener(new EditTextCleanWatcher(ivClean3, telEt3));
        telEt4.addTextChangedListener(new EditTextCleanWatcher(ivClean4, telEt4));

        layoutClean1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telEt1.setText("");
            }
        });

        layoutClean2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telEt2.setText("");
            }
        });

        layoutClean3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telEt3.setText("");
            }
        });

        layoutClean4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telEt4.setText("");
            }
        });
    }

    @Override
    protected void titleRightButtonListener() {
        saveInfo();
    }

    private void saveInfo() {
        if (check()) {
            final HintInfoDialog dialog = new HintInfoDialog(EditOthersActivity.this, "信息保存中，请稍后...");
            dialog.show();
            childInfo.setYe_tel(telEt1.getText().toString().trim());
            childInfo.setNai_tel(telEt2.getText().toString().trim());
            childInfo.setWaigong_tel(telEt3.getText().toString().trim());
            childInfo.setWaipo_tel(telEt4.getText().toString().trim());

            UserRequest.changeChild(mContext, childInfo, "save",new RequestResultI() {
                @Override
                public void result(BaseModel domain) {
                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra("tel1", telEt1.getText().toString());
                    intent.putExtra("tel2", telEt2.getText().toString());
                    intent.putExtra("tel3", telEt3.getText().toString());
                    intent.putExtra("tel4", telEt4.getText().toString());
                    intent.putExtra("uuid",childInfo.getUuid());
                    setResult(RESULT_OK, intent);
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
        }
    }

    private boolean check() {
        if (!Utils.stringIsNull(telEt1.getText().toString())) {
            //   Utils.showToast(EditOthersActivity.this, "爷爷的电话号码不能为空");
            if (!Utils.isMobiPhoneNum(telEt1.getText().toString())) {
                Utils.showToast(EditOthersActivity.this, "爷爷的电话号码不合法");
                return false;
            }
        }

        if (!Utils.stringIsNull(telEt2.getText().toString())) {
            //Utils.showToast(EditOthersActivity.this, "奶奶的电话号码不能为空");
            if (!Utils.isMobiPhoneNum(telEt2.getText().toString())) {
                Utils.showToast(EditOthersActivity.this, "奶奶的电话号码不合法");
                return false;
            }
        }

        if (!Utils.stringIsNull(telEt3.getText().toString())) {
            // Utils.showToast(EditOthersActivity.this, "外公的电话号码不能为空");
            if (!Utils.isMobiPhoneNum(telEt3.getText().toString())) {
                Utils.showToast(EditOthersActivity.this, "外公的电话号码不合法");
                return false;
            }
        }


        if (!Utils.stringIsNull(telEt4.getText().toString())) {
            // Utils.showToast(EditOthersActivity.this, "工作单位不能为空");
            if (!Utils.isMobiPhoneNum(telEt4.getText().toString())) {
                Utils.showToast(EditOthersActivity.this, "外婆的电话号码不合法");
                return false;
            }
        }
        return true;
    }
}
