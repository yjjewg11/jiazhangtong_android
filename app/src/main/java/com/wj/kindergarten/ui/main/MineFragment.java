package com.wj.kindergarten.ui.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.ui.func.MineSchoolActivity;
import com.wj.kindergarten.ui.func.MineSpecialCourseActivity;
import com.wj.kindergarten.ui.messagepag.EditMyInfoActivity;
import com.wj.kindergarten.ui.mine.ChildActivity;
import com.wj.kindergarten.ui.mine.EditChildActivity;
import com.wj.kindergarten.ui.mine.SettingActivity;
import com.wj.kindergarten.ui.mine.store.StoreActivity;
import com.wj.kindergarten.utils.Constant.MessageConstant;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;


/**
 * MainFragment
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/17 11:34
 */
public class MineFragment extends Fragment {
    private View rootView;
    private LinearLayout llSetting;
    private Login login;
    private LinearLayout mine_collect;
    private LinearLayout mine_course;
    private LinearLayout recruit_school;
    private ImageView mine_add_child;
    private View.OnClickListener addListeners;
    public static MineFragment instance;
    private CircleImage circle_mine_image;
    private TextView tv_my_name;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).clearCenterIcon();
        ((MainActivity) getActivity()).setText("");

        login = ((CGApplication) CGApplication.getInstance()).getLogin();
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, null, false);
            initViews(rootView);
            instance = this;
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initHeadData();

//        addChildren();
        return rootView;
    }

    private void initHeadData() {
        Login login = CGApplication.getInstance().getLogin();
        if(login == null || login.getUserinfo() == null)return;
        tv_my_name.setText(""+Utils.isNull(CGApplication.getInstance().getLogin().getUserinfo().getName()));
        String head = login.getUserinfo().getImg();
        if(!Utils.stringIsNull(head)){
            ImageLoaderUtil.displayImage(head,circle_mine_image);
        }
    }

    View.OnClickListener myListener = new View.OnClickListener() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),EditMyInfoActivity.class);
            getActivity().startActivityForResult(intent, GloablUtils.UPDATE_MY_INFO,null);
        }
    };

    private void initViews(View rootView) {
        addListeners = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转添加页面
                Intent intent = new Intent(getActivity(), EditChildActivity.class);
//                intent.putExtra("handler",handler);
                startActivity(intent);
            }
        };
        circle_mine_image = (CircleImage)rootView.findViewById(R.id.circle_mine_image);
        tv_my_name = (TextView)rootView.findViewById(R.id.tv_children_name);
        circle_mine_image.setOnClickListener(myListener);
        tv_my_name.setOnClickListener(myListener);
        mine_add_child = (ImageView) rootView.findViewById(R.id.mine_add_child);
        mine_add_child.setOnClickListener(addListeners);
        mine_collect = (LinearLayout) rootView.findViewById(R.id.ll_store);
        mine_course = (LinearLayout) rootView.findViewById(R.id.ll_special_course);
        recruit_school = (LinearLayout) rootView.findViewById(R.id.ll_mine_school);

        llSetting = (LinearLayout) rootView.findViewById(R.id.ll_setting);
        llSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.registerUmengClickEvent(MessageConstant.SETTING);
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });

        mine_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.registerUmengClickEvent(MessageConstant.STORE_CLICK);
                getActivity().startActivity(new Intent(getActivity(), StoreActivity.class));
            }
        });
        mine_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击启动我的课程页面
                Utils.registerUmengClickEvent(MessageConstant.MINE_COURSE);
                Intent intent = new Intent(getActivity(), MineSpecialCourseActivity.class);
                startActivity(intent);
            }
        });
        recruit_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动招生学校界面.
                Intent intent = new Intent(getActivity(), MineSchoolActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setMargin(LinearLayout.LayoutParams params, int margin) {
        params.leftMargin = margin;

    }

//    public void addChildren() {
//        Login loginNew = CGApplication.getInstance().getLogin();
//        childContent.removeAllViews();
//        if (loginNew != null && loginNew.getList() != null) {
//            //如果小于等于3，均分
//            if(loginNew.getList().size() <= 3 && loginNew.getList().size() != 0){
//                hs_container_mine.removeAllViews();
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.
//                        LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                layoutParams.weight = 1;
//                addSmallChild(loginNew,hs_container_mine,layoutParams);
//            }else if(loginNew.getList().size() > 3){
//                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.
//                        LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                layoutParams2.leftMargin = 80;
//                addSmallChild(loginNew,childContent,layoutParams2);
//            }
//
//
//
//            if (loginNew.getList().size() == 0) {
//                View view = View.inflate(getActivity(), R.layout.mine_children_head, null);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.
//                        LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                CircleImage headIv = (CircleImage) view.findViewById(R.id.circle_mine_image);
//                TextView nameTv = (TextView) view.findViewById(R.id.tv_children_name);
//                headIv.setImageResource(R.drawable.xiaohai_head);
//                nameTv.setText("添加宝宝");
//                setMargin(layoutParams, WindowUtils.dm.widthPixels / 2);
//                view.setOnClickListener(addListeners);
//                childContent.addView(view, layoutParams);
//            }
//        }
//    }

    private void addSmallChild(Login loginNew, LinearLayout contain,LinearLayout.LayoutParams layoutParams) {
        for (final ChildInfo childInfo : loginNew.getList()) {
            View view = View.inflate(getActivity(), R.layout.mine_children_head, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ChildActivity.class);
                    intent.putExtra("childInfo", childInfo);
                    startActivity(intent);
                }
            });
            CircleImage headIv = (CircleImage) view.findViewById(R.id.circle_mine_image);
            TextView nameTv = (TextView) view.findViewById(R.id.tv_children_name);


            if (!Utils.stringIsNull(childInfo.getHeadimg())) {
                ImageLoaderUtil.displayImage(childInfo.getHeadimg(), headIv);
            } else {
                headIv.setImageResource(R.drawable.touxiang);
            }
            nameTv.setText(childInfo.getName());
            contain.addView(view, layoutParams);
        }
    }

}
