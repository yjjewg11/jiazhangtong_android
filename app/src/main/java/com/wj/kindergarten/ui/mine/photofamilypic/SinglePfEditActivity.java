package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalDb;

import java.io.Serializable;
import java.util.List;

public class SinglePfEditActivity extends BaseActivity {


    private AllPfAlbumSunObject object;
    private ImageView single_pf_edit_image;
    private EditText single_pf_edit_edit;
    private TextView single_pf_edit_place, single_pf_edit_time;
    private HintInfoDialog dialog;
    private FinalDb db;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_single_pf_edit;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        db = FinalDb.create(this, GloablUtils.FAMILY_UUID_OBJECT,true);
        Intent intent = getIntent();
        object = (AllPfAlbumSunObject) intent.getSerializableExtra("object");
        setTitleText("编辑","保存");
        initViews();
        initData();
    }

    private void initData() {
        if(object != null){
            ImageLoaderUtil.displayMyImage(object.getPath(),single_pf_edit_image);
            single_pf_edit_edit.setText("" + Utils.isNull(object.getNote()));
            single_pf_edit_place.setText(""+Utils.isNull(object.getAddress()));
            single_pf_edit_time.setText(""+Utils.isNull(object.getCreate_time()));
        }
    }

    private void initViews() {
        dialog = new HintInfoDialog(this,"保存中，请稍候!");
        single_pf_edit_image = (ImageView) findViewById(R.id.single_pf_edit_image);
        single_pf_edit_edit = (EditText) findViewById(R.id.single_pf_edit_edit);
        single_pf_edit_place = (TextView) findViewById(R.id.single_pf_edit_place);
        single_pf_edit_time = (TextView) findViewById(R.id.single_pf_edit_time);
    }

    @Override
    protected void titleRightButtonListener() {
        editPf();
    }

    public void editPf(){
        dialog.show();
        String note = null;
        String address = null;
        if(single_pf_edit_edit.getText() != null){
            note = single_pf_edit_edit.getText().toString();
        }
        if(single_pf_edit_place.getText() != null){
            address = single_pf_edit_place.getText().toString();
        }
        final String finalAddress = address;
        final String finalNote = note;
        UserRequest.editSinglePf(this, object.getUuid(), address, note, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                object.setAddress(finalAddress);
                object.setNote(finalNote);
                if(dialog.isShowing()){
                    dialog.cancel();
                }
                db.update(object);
                Intent intent = new Intent();
                intent.putExtra("object",object);
                setResult(RESULT_OK,intent);
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
}
