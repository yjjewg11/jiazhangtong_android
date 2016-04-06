package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.*;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.MineSchoolActivity;
import com.wj.kindergarten.ui.func.adapter.FragmentChildStateAdapter;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ChildActivity
 *
 * @Description:宝贝详情
 * @Author: Wave
 * @CreateDate: 2015/7/24 1:03
 */
public class ChildActivity extends BaseActivity {
    public static ChildActivity instance;
    private FragmentChildStateAdapter pagerAdapter;

    private ViewPager activity_child_pager;
    private List<ChildInfo> childInfos;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_child_viewpager;
//        layoutId = R.layout.activity_child;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {

        childInfos = CGApplication.getInstance().getLogin().getList();
        activity_child_pager = (ViewPager) findViewById(R.id.activity_child_pager);
        pagerAdapter = new FragmentChildStateAdapter(getSupportFragmentManager(),childInfos);
        activity_child_pager.setAdapter(pagerAdapter);
//        instance = this;
//        childInfo = (ChildInfo)getIntent().getSerializableExtra("childInfo");
//

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if(data.getBooleanExtra("newData", false)){
                pagerAdapter.notifyDataSetChanged();
                return;
            }
            ChildInfo childInfo =  lookForChildInfo(data.getStringExtra("uuid"));
            switch (requestCode) {
                case GloablUtils.OWN:
                    childInfo.setHeadimg(data.getStringExtra("head"));
                    childInfo.setName(data.getStringExtra("name"));
                    childInfo.setNickname(data.getStringExtra("nick"));
                    childInfo.setSex(data.getIntExtra("sex", 0));
                    childInfo.setBirthday(data.getStringExtra("birth"));
                    childInfo.setIdcard(data.getStringExtra("idCard"));
                    break;
                case GloablUtils.BA:
                    childInfo.setBa_name(data.getStringExtra("name"));
                    childInfo.setBa_tel(data.getStringExtra("tel"));
                    childInfo.setBa_work(data.getStringExtra("work"));
                    break;
                case GloablUtils.MA:
                    childInfo.setMa_name(data.getStringExtra("name"));
                    childInfo.setMa_tel(data.getStringExtra("tel"));
                    childInfo.setMa_work(data.getStringExtra("work"));
                    break;
                case GloablUtils.OTHER:
                    childInfo.setYe_tel(data.getStringExtra("tel1"));
                    childInfo.setNai_tel(data.getStringExtra("tel2"));
                    childInfo.setWaigong_tel(data.getStringExtra("tel3"));
                    childInfo.setWaipo_tel(data.getStringExtra("tel4"));
                    break;
                case GloablUtils.SCHOOL:
                    break;
                case GloablUtils.REMARK:
                    childInfo.setNote(data.getStringExtra("remark"));
                    break;
            }
            notifyData(childInfo);
        }
    }

    private void notifyData(ChildInfo childInfo) {
       int index =  childInfos.indexOf(childInfo);
        childInfos.set(index,childInfo);
        pagerAdapter.notifyDataSetChanged();
    }

    private ChildInfo lookForChildInfo(String uuid) {
        Iterator<ChildInfo> iterator = childInfos.iterator();
        while (iterator.hasNext()){
            ChildInfo i = iterator.next();
            if(uuid.equals(i.getUuid())){
                return i;
            }
        }
        return null;
    }


}
