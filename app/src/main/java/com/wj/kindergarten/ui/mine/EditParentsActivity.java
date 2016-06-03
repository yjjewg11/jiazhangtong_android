package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class EditParentsActivity extends BaseActivity {
    private EditText nameEt;
    private EditText telEt;
    private EditText workEt;
    private ChildInfo childInfo = null;
    private boolean isFather = false;
    private RelativeLayout layoutClean1 = null;
    private RelativeLayout layoutClean2 = null;
    private RelativeLayout layoutClean3 = null;
    private ImageView ivClean1 = null;
    private ImageView ivClean2 = null;
    private ImageView ivClean3 = null;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_edit_parent;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("修改信息", "保存");

        initViews();

        childInfo = (ChildInfo) getIntent().getSerializableExtra("childInfo");
        isFather = getIntent().getBooleanExtra("isFather", false);
        if (isFather) {
            nameEt.setText(Utils.getText(childInfo.getBa_name()));
            telEt.setText(Utils.getText(childInfo.getBa_tel()));
            workEt.setText(Utils.getText(childInfo.getBa_work()));
        } else {
            nameEt.setText(Utils.getText(childInfo.getMa_name()));
            telEt.setText(Utils.getText(childInfo.getMa_tel()));
            workEt.setText(Utils.getText(childInfo.getMa_work()));
        }
    }

    private void initViews() {
        nameEt = (EditText) findViewById(R.id.child_edit_name);
        telEt = (EditText) findViewById(R.id.child_edit_tel);
        workEt = (EditText) findViewById(R.id.child_edit_work);
        layoutClean1 = (RelativeLayout) findViewById(R.id.ll_clean_1);
        layoutClean2 = (RelativeLayout) findViewById(R.id.ll_clean_2);
        layoutClean3 = (RelativeLayout) findViewById(R.id.ll_clean_3);
        ivClean1 = (ImageView) findViewById(R.id.iv_clean_1);
        ivClean2 = (ImageView) findViewById(R.id.iv_clean_2);
        ivClean3 = (ImageView) findViewById(R.id.iv_clean_3);

        nameEt.addTextChangedListener(new EditTextCleanWatcher(ivClean1, nameEt));
        telEt.addTextChangedListener(new EditTextCleanWatcher(ivClean2, telEt));
        workEt.addTextChangedListener(new EditTextCleanWatcher(ivClean3, workEt));

        layoutClean1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEt.setText("");
            }
        });

        layoutClean2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telEt.setText("");
            }
        });

        layoutClean3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workEt.setText("");
            }
        });
    }

    @Override
    protected void titleRightButtonListener() {
        saveInfo();
    }

    private void saveInfo() {
//        if (check()) {
            final HintInfoDialog dialog = new HintInfoDialog(EditParentsActivity.this, "信息保存中，请稍后...");
            dialog.show();
            if (isFather) {
                childInfo.setBa_name(nameEt.getText().toString().trim());
                childInfo.setBa_tel(telEt.getText().toString().trim());
                childInfo.setBa_work(workEt.getText().toString().trim());
            } else {
                childInfo.setMa_name(nameEt.getText().toString().trim());
                childInfo.setMa_tel(telEt.getText().toString().trim());
                childInfo.setMa_work(workEt.getText().toString().trim());
            }

            UserRequest.changeChild(mContext, childInfo,"save", new RequestResultI() {
                @Override
                public void result(BaseModel domain) {
                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra("name", nameEt.getText().toString());
                    intent.putExtra("tel", telEt.getText().toString());
                    intent.putExtra("work", workEt.getText().toString());
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
//        }
    }

    private boolean check() {
        if (Utils.stringIsNull(nameEt.getText().toString())) {
            Utils.showToast(EditParentsActivity.this, "姓名不能为空");
            return false;
        }

        if (Utils.stringIsNull(telEt.getText().toString())) {
            Utils.showToast(EditParentsActivity.this, "电话号码不能为空");
            return false;
        }

        if (!Utils.isMobiPhoneNum(telEt.getText().toString())) {
            Utils.showToast(EditParentsActivity.this, "您输入的电话号码不合法");
            return false;
        }

//        if (Utils.stringIsNull(workEt.getText().toString())) {
//            Utils.showToast(EditParentsActivity.this, "工作单位不能为空");
//            return false;
//        }
        return true;
    }
}
