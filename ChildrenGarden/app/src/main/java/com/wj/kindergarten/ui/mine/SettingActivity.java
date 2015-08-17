package com.wj.kindergarten.ui.mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ActivityManger;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.TestActivity;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.Utils;

import java.util.Set;

/**
 * SettingActivity
 *
 * @Description: t设置
 * @Author: Wave
 * @CreateDate: 2015/7/17 10:30
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout layout1 = null;
    private RelativeLayout layout2 = null;
    private RelativeLayout layout3 = null;
    private RelativeLayout layout4 = null;
    private RelativeLayout layout5 = null;
    private RelativeLayout layout6 = null;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_setting;
    }

    @Override
    protected void setNeedLoading() {
        isNeedLoading = false;
    }

    @Override
    protected void onCreate() {
        setTitleText(getString(R.string.text_btn_set));
        init();

        ActivityManger.getInstance().addActivity(this);
    }

    private void init() {
        layout1 = (RelativeLayout) findViewById(R.id.layout_1);
        layout2 = (RelativeLayout) findViewById(R.id.layout_2);
        layout3 = (RelativeLayout) findViewById(R.id.layout_3);
        layout4 = (RelativeLayout) findViewById(R.id.layout_4);
        layout5 = (RelativeLayout) findViewById(R.id.layout_5);
        layout6 = (RelativeLayout) findViewById(R.id.layout_6);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_1://修改密码
                startActivity(new Intent(SettingActivity.this, UpdatePasswordActivity.class));
                break;
            case R.id.layout_2://推送通知
                startActivity(new Intent(SettingActivity.this, NoticeActivity.class));
                break;
            case R.id.layout_3://意见反馈
                startActivity(new Intent(SettingActivity.this, FeedBackActivty.class));
                break;
            case R.id.layout_4://关于我们
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                break;
            case R.id.layout_5://检查更新
                // startActivity(new Intent(SettingActivity.this, TestActivity.class));
                checkVersion();
                break;
            case R.id.layout_6://退出
                // MobclickAgent.onKillProcess(SettingActivity.this);
                ActivityManger.getInstance().exit();
                break;
            default:
                break;
        }
    }

    /**
     * 版本检测
     */
    private void checkVersion() {
        UmengUpdateAgent.setUpdateOnlyWifi(true);
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateCheckConfig(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus,
                                         UpdateResponse updateInfo) {
                if (updateStatus == 0 && updateInfo != null) {
                    showUpdateDialog(updateInfo.path, updateInfo.updateLog);
                } else if (updateStatus == 1) {
                    Utils.showToast(SettingActivity.this, "已经是最新版本");
                } else if (updateStatus == 2) {
                    Utils.showToast(SettingActivity.this, "只能在Wifi环境下才可更新");
                } else if (updateStatus == 3) {
                    Utils.showToast(SettingActivity.this, "检查更新超时");
                }

                CGLog.d("status: " + updateStatus);
            }
        });

        UmengUpdateAgent.update(this);
    }

    private void showUpdateDialog(final String downloadUrl, final String message) {
        AlertDialog.Builder updateAlertDialog = new AlertDialog.Builder(this);
        updateAlertDialog.setIcon(R.drawable.ic_launcher);
        updateAlertDialog.setTitle(R.string.app_name);
        updateAlertDialog.setMessage(getString(R.string.update_hint, message));
        updateAlertDialog.setNegativeButton(R.string.update_ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse(downloadUrl)));
                        } catch (Exception ex) {

                        }
                    }
                }).setPositiveButton(R.string.dialog_no, null);
        if (!isFinishing())
            updateAlertDialog.show();
    }
}
