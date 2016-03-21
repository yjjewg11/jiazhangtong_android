package com.wj.kindergarten.ui.mine.photofamilypic;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ActivityManger;
import com.wj.kindergarten.abstractbean.RequestFailedResult;
import com.wj.kindergarten.bean.AddFamilyMemberParams;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.MyConstact;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class AddFamilyMemberActivity extends BaseActivity {


    private static final int READ_CONSTANTS_PREMISSION = 3060;
    //利用注解初始化view
    @ViewInject(id = R.id.add_family_name)
    EditText editName;
    @ViewInject(id = R.id.add_family_relation)
    EditText editRelation;
    @ViewInject(id = R.id.add_family_phone)
    EditText editPhone;
    @ViewInject(id = R.id.add_family_add_bt,click = "onClick")
    TextView add_family_add_bt;
    @ViewInject(id = R.id.iv_add_member,click = "onClick")
    ImageView iv_add_member;
    @ViewInject(id = R.id.add_family_member_get_contact,click = "onClick")
    TextView add_family_member_get_contact;
    private AddFamilyMemberParams member;
    private boolean allowPutIn;


    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_add_family_member;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        FinalActivity.initInjectedView(this);
        getData();
        initViewData();
        setTitleText("添加家人");

    }
    private void initContactList() {
        List<MyConstact> contactList = new ArrayList<MyConstact>();

        // 查询联系人数据
        Cursor cursor = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            MyConstact contact = new MyConstact();
            // 获取联系人的Id
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // 获取联系人的姓名
            String contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contact.setContantName(contactName);

            // 有联系人姓名得到对应的拼音
//            String pinyin = PinyinUtils.getPinyin(contactName);
//            contact.setPinyin(pinyin);

            Cursor phoneCursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                            + contactId, null, null);

            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor
                        .getString(phoneCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contact.setPhoneNumber(phoneNumber);
            }

            if (null != phoneCursor && !phoneCursor.isClosed()) {
                phoneCursor.close();
            }

            contactList.add(contact);
        }

        if (null != cursor && !cursor.isClosed()) {
            cursor.close();
        }

        CGLog.v(contactList.toString());

        if(contactList == null || contactList.size() == 0){
            allowPutIn = false;
        }else {
            allowPutIn = true;
        }
    }



    private void initViewData() {
        editPhone.setText("" + Utils.isNull(member.getTel()));
        editName.setText("");
        editRelation.setText("" + Utils.isNull(member.getFamily_name()));

    }

    private void getData() {
        member =(AddFamilyMemberParams) getIntent().getSerializableExtra("member");
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.add_family_add_bt:
                if(checkData()){
                    addFamily();
                }
                break;
            case R.id.iv_add_member:

                break;
            case R.id.add_family_member_get_contact:
                addMember();
                break;
        }
    }

    private void addMember() {
        initContactList();
        if(allowPutIn){
            startConstact();
        }else {
            ToastUtils.showMessage("无法获取联系人，请到系统设置里开启联系人权限!");
        }
    }

//    private boolean checkOwnPermersion() {
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
//                != PackageManager.PERMISSION_GRANTED) {
//            //申请WRITE_EXTERNAL_STORAGE权限
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
//                    READ_CONSTANTS_PREMISSION);
//            return false;
//        }
//        return true;
//    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case READ_CONSTANTS_PREMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    addMember();
                }
                break;
        }
    }

    private void addFamily() {
        member.setFamily_name(editRelation.getText().toString());
        String tel = editPhone.getText().toString();
        if(!TextUtils.isEmpty(tel)){
            tel = tel.replaceAll(" ","");
            tel.trim();
        }
        member.setTel(tel);
        UserRequest.addFamilyMember(this, member.getFamily_uuid(), member.getFamily_name(), member.getTel(),
                 member.getUuid(),new RequestFailedResult(){

                    @Override
                    public void result(BaseModel domain) {
                        ToastUtils.showMessage("修改成功!");
                        setResult(RESULT_OK, new Intent());
                        finish();
                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }
                });
    }

    private void startConstact() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, GloablUtils.REQUEST_CONTACT);
    }

    private boolean checkData() {
        if(!Utils.checkEdit(editName)) {
            editName.setError("请输入正确用户名!");
            return false;
        }
        if(!Utils.checkEdit(editRelation)) {
            editRelation.setError("请输入您和宝宝的关系!");
            return false;
        }
        if(!Utils.checkEdit(editPhone)) {
            editPhone.setError("请输入您的手机号码!");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;
        switch (requestCode){
            case GloablUtils.REQUEST_CONTACT :
                Uri result = data.getData();
                String [] constacts =  getPhoneContacts(result);
                if(constacts != null){
                    editPhone.setText(""+constacts[1]);
                    editRelation.setText(""+constacts[0]);
                }
                break;
        }
    }
    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
            //取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);

            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);

            if(phone != null){
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            }

            phone.close();
            cursor.close();
        }else{
            CGLog.v("get Contacts is fail");
        }

        return contact;
    }
}
