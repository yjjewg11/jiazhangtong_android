package com.wj.kindergarten.ui.mine.photofamilypic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.abstractbean.RequestFailedResult;
import com.wj.kindergarten.bean.AddFamilyMemberParams;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.PFAlbumMember;
import com.wj.kindergarten.bean.PfAlbumInfo;
import com.wj.kindergarten.bean.PfAlbumInfoSun;
import com.wj.kindergarten.bean.PfAlbumList;
import com.wj.kindergarten.bean.PfAlbumListSun;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.mine.ChooseImage;
import com.wj.kindergarten.utils.DateTimePickDialogUtil;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.UserHeadImageUtil;
import com.wj.kindergarten.utils.Utils;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tangt on 2016/1/14.
 */
public class PfEditInfoActivity extends BaseActivity {

    private static final String IMAGE_FILE_NAME = "pfeditInfo.jpg";

    private RelativeLayout[] relativeLayouts;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private TextView tv_album_name;
    private ImageView iv_appear_image;
    private TextView pf_tv_birthday;
    String family_uuid;
    private PfAlbumInfoSun info;
    private LinearLayout memer_linear;
    private PfAlbumInfo pfAlbumInfo;
    String title;
    int status = 0;
    String hearld;
    private TextView pf_edit_info_invite;
    private List<PFAlbumMember> memberList;
    private EditText editText;

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.pf_edit_info;
    }

    @Override
    protected void setNeedLoading() {
    }



    @Override
    protected void onCreate() {
        setTitleText("相册信息","保存");
        initViews();
        initClicks();
        initData();
    }


    @Override
    protected void loadData() {
        commonDialog.show();
        UserRequest.getAlbumInfo(this, family_uuid, new RequestFailedResult(commonDialog) {
            @Override
            public void result(BaseModel domain) {
                if (commonDialog.isShowing()) {
                    commonDialog.dismiss();
                }
                pfAlbumInfo = (PfAlbumInfo) domain;
                if (pfAlbumInfo != null) {
                    info = pfAlbumInfo.getData();
                    initHttpData();
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

        });
    }

    String initTitle;
    String initHearld;
    private void initHttpData() {
        title = info.getTitle();
        hearld = info.getHerald();
        initTitle = info.getTitle();
        initHearld = info.getHerald();
        tv_album_name.setText("" + title);
        if(!Utils.stringIsNull(hearld)){
            ImageLoaderUtil.displayImage(hearld, iv_appear_image);
        }else {
            iv_appear_image.setImageDrawable(getResources().getDrawable(R.drawable.family_album_default));
        }
        memer_linear.removeAllViews();
        memberList = pfAlbumInfo.getMembers_list();
        addAllViews();

    }

    private void addAllViews() {
        memer_linear.removeAllViews();
        if (memberList != null && memberList.size() > 0) {
            for (final PFAlbumMember member : memberList) {
                View addView = View.inflate(this, R.layout.pf_album_member_info, null);
                TextView pf_album_member_name = (TextView) addView.findViewById(R.id.pf_album_member_name);
                TextView pf_album_member_phone = (TextView) addView.findViewById(R.id.pf_album_member_phone);
                pf_album_member_name.setText(""+ Utils.isNull(member.getFamily_name()));
                pf_album_member_phone.setText(""+Utils.isNull(member.getTel()));
                addView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ToastUtils.showDialog(PfEditInfoActivity.this, "提示！", "您确认删除此成员吗?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                deleteMember(member);
                            }
                        });
                        return false;
                    }
                });
                pf_album_member_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        editMember(params);弹出框进行编辑
                        View customView = View.inflate(PfEditInfoActivity.this,R.layout.pf_edit_family,null);
                        final EditText pf_edit_family_name = (EditText) customView.findViewById(R.id.pf_edit_family_name);
                        final EditText pf_edit_family_phone = (EditText) customView.findViewById(R.id.pf_edit_family_phone);
                        TextView pf_edit_family_delete = (TextView) customView.findViewById(R.id.pf_edit_family_delete);
                        pf_edit_family_name.setText(""+Utils.isNull(member.getFamily_name()));
                        pf_edit_family_phone.setText("" + Utils.isNull(member.getTel()));
                        final DialogInterface dialogInfo =  ToastUtils.showDialog(PfEditInfoActivity.this, customView, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                member.setFamily_name(pf_edit_family_name.getText().toString());
                                member.setTel(pf_edit_family_phone.getText().toString());
                                editInfo(member);
                            }
                        });
                        pf_edit_family_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtils.showDialog(PfEditInfoActivity.this, "提示!", "您确认删除吗?", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(dialogInfo != null) dialogInfo.cancel();
                                        dialog.dismiss();
                                        deleteMember(member);
                                    }
                                });
                            }
                        });
                    }
                });
                memer_linear.addView(addView);
            }
        }
    }

    public void deleteMember(final PFAlbumMember member){
        commonDialog.show();
        UserRequest.deleteAlbumMember(this, member.getUuid(), new RequestFailedResult(commonDialog) {
            @Override
            public void result(BaseModel domain) {
                if (commonDialog.isShowing()) commonDialog.cancel();
                memberList.remove(member);
                addAllViews();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

        });
    }

    private void initData() {
        family_uuid = getIntent().getStringExtra("uuid");
        if (TextUtils.isEmpty(family_uuid)) return;
        loadData();
    }

    @Override
    protected void titleRightButtonListener() {
        saveAlbum();
    }




    private void saveAlbum() {
        showCommonDialog();
        String uuid = pfAlbumInfo == null ? "" : pfAlbumInfo.getData().getUuid();
        UserRequest.AddOrEditPfAlbum(this, title, uuid, "0", hearld, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                cancleCommonDialog();
                ToastUtils.showMessage("保存成功!");
                editObject();
                setOwnResult();
                finish();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void editObject() {
        List<PfAlbumListSun> objectList = MainActivity.instance.getAlbumList();
        Iterator<PfAlbumListSun> iterator = objectList.iterator();
        while (iterator.hasNext()){
            PfAlbumListSun sun = iterator.next();
            if(sun.getUuid().equals(family_uuid)){
                sun.setTitle(title);
                sun.setHerald(hearld);
                int dex = objectList.indexOf(sun);
                MainActivity.instance.getAlbumList().remove(dex);
                MainActivity.instance.getAlbumList().add(dex, sun);
                setOwnResult();
                finish();
                return;
            }
        }

    }


    private void initClicks() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.album_name:
                        editText.setText("" + Utils.isNull(title));
                        dialog.show();
                        break;
                    case R.id.create_time:
//                        DateTimePickDialogUtil dialogUtil = new DateTimePickDialogUtil(PfEditInfoActivity.this,
//                                pf_tv_birthday.getText().toString(), new DateTimePickDialogUtil.ChooseTime() {
//                            @Override
//                            public void choose(String time) {
//                                pf_tv_birthday.setText(time);
//                            }
//                        });
//                        dialogUtil.dateTimePicKDialog();
                        break;
                    case R.id.appreance_photo:
                        Intent intent = new Intent(PfEditInfoActivity.this,BoutiqueSingleChooseActivity.class);
                        startActivityForResult(intent,GloablUtils.GET_SINGLE_PIC_FROM_BOUTIQUE);

//                        UserHeadImageUtil.showChooseImageDialog(PfEditInfoActivity.this, v, new ChooseImage() {
//                            @Override
//                            public void chooseImage(int type) {
//                                if (type == UserHeadImageUtil.TAKE_PHOTO) {
//                                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    //下面这句指定调用相机拍照后的照片存储的路径
//                                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
//                                    startActivityForResult(takeIntent, GloablUtils.PF_EDIT_TAKE_PHOTO);
//                                } else if (type == UserHeadImageUtil.CHOOSE_IMAGE_FROM_PICTURES) {
//                                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
//                                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
//                                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                                    startActivityForResult(pickIntent, GloablUtils.PF_EDIT_CHOOSE_IMAGE);
//                                }
//                            }
//                        });
                        break;
                }
            }
        };
        for (RelativeLayout relativeLayout : relativeLayouts) {
            relativeLayout.setOnClickListener(listener);
        }
    }

    private void initViews() {
        pf_edit_info_invite = (TextView) findViewById(R.id.pf_edit_info_invite);
        pf_edit_info_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFamilyMemberParams params = new AddFamilyMemberParams();
                params.setFamily_uuid(family_uuid);
                editMember(params);
            }
        });

        memer_linear = (LinearLayout) findViewById(R.id.memer_linear);
        relativeLayouts = new RelativeLayout[]{
                (RelativeLayout) findViewById(R.id.album_name),
                (RelativeLayout) findViewById(R.id.appreance_photo),
                (RelativeLayout) findViewById(R.id.create_time),
        };
        tv_album_name = (TextView) findViewById(R.id.tv_album_name);
        iv_appear_image = (ImageView) findViewById(R.id.iv_appear_image);
        pf_tv_birthday = (TextView) findViewById(R.id.pf_tv_birthday);

        editText = new EditText(this);
        builder = new AlertDialog.Builder(this);
        editText.setText("" + Utils.isNull(title));
        dialog = builder.setTitle("设置标题").setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                title = editText.getText().toString();
                tv_album_name.setText(title);
            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setView(editText).create();
    }

    private void editMember(AddFamilyMemberParams params) {
        Intent intent = new Intent(PfEditInfoActivity.this,AddFamilyMemberActivity.class);
        intent.putExtra("member", params);
        startActivityForResult(intent, GloablUtils.ADD_FAMILY_MEMBER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;
        switch (requestCode) {
            case GloablUtils.GET_SINGLE_PIC_FROM_BOUTIQUE:
                hearld = data.getStringExtra("path");
                ImageLoaderUtil.displayImage(hearld,iv_appear_image);
                break;
            case GloablUtils.ADD_FAMILY_MEMBER:
                loadData();
                break;

        }
    }

    @Override
    protected void titleLeftButtonListener() {
        showSave();
//        super.titleLeftButtonListener();
    }

    private void showSave() {
            if(initTitle != title || initHearld != hearld){
                ToastUtils.showDialog(this, "提示!", "相册信息已修改,是否保存?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        saveAlbum();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
            }else {
                finish();
            }
    }

    private void setOwnResult(){
        setResult(RESULT_OK, new Intent());
    }

    @Override
    public void onBackPressed() {
        showSave();
//        setOwnResult();
//        super.onBackPressed();

    }

    private void editInfo(final PFAlbumMember member){
        UserRequest.addFamilyMember(this, member.getFamily_uuid(), member.getFamily_name(), member.getTel(),
                member.getUuid(), new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        ToastUtils.showMessage("修改成功!");
                        memberList.remove(member);
                        memberList.add(member);
                        addAllViews();
                    }
                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                    @Override
                    public void failure(String message) {
                        ToastUtils.showMessage(message);
                    }
                });
    }
}
