package com.wj.kindergarten.ui.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;

import java.util.ArrayList;
import java.util.List;


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
    private LinearLayout ll_mine_child;
    private TextView fragment_mine_child_tv;
    private LinearLayout fragment_mine_child_linear;


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

    public void initHeadData() {
        Login login = CGApplication.getInstance().getLogin();
        if (login == null || login.getUserinfo() == null) return;
        tv_my_name.setText("" + Utils.isNull(CGApplication.getInstance().getLogin().getUserinfo().getName()));
        String head = login.getUserinfo().getImg();
        if (!Utils.stringIsNull(head)) {
            ImageLoaderUtil.displayImage(head, circle_mine_image);
        }
    }

    View.OnClickListener myListener = new View.OnClickListener() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), EditMyInfoActivity.class);
            getActivity().startActivityForResult(intent, GloablUtils.UPDATE_MY_INFO, null);
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

        fragment_mine_child_linear = (LinearLayout) rootView.findViewById(R.id.fragment_mine_child_linear);
        fragment_mine_child_tv = (TextView) rootView.findViewById(R.id.fragment_mine_child_tv);
        ll_mine_child = (LinearLayout) rootView.findViewById(R.id.ll_mine_child);
        circle_mine_image = (CircleImage) rootView.findViewById(R.id.circle_mine_image);
        tv_my_name = (TextView) rootView.findViewById(R.id.tv_children_name);
        circle_mine_image.setOnClickListener(myListener);
        tv_my_name.setOnClickListener(myListener);
        mine_add_child = (ImageView) rootView.findViewById(R.id.mine_add_child);
        mine_add_child.setOnClickListener(addListeners);
        mine_collect = (LinearLayout) rootView.findViewById(R.id.ll_store);
        mine_course = (LinearLayout) rootView.findViewById(R.id.ll_special_course);
        recruit_school = (LinearLayout) rootView.findViewById(R.id.ll_mine_school);

        llSetting = (LinearLayout) rootView.findViewById(R.id.ll_setting);

        initChildCount();
        ll_mine_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login login = CGApplication.getInstance().getLogin();
                if (login == null || login.getList() == null) {
                    ToastUtils.showMessage("暂无孩子信息!");
                    return;
                }
                Intent intent = new Intent(getActivity(), ChildActivity.class);
                intent.putExtra("childInfo", (ArrayList) login.getList());
                getActivity().startActivity(intent);
            }
        });

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

    public void initChildCount() {

        if (login != null) {
            if (login.getList() != null && login.getList().size() > 0) {
                List<ChildInfo> infoList = login.getList();
                int size = infoList.size() >= 3 ? 3 : infoList.size();
                for (int k = 0; k < size ; k++){
                    ChildInfo childInfo = infoList.get(k);
                    if(childInfo == null) continue;
                    addChildView(childInfo);
                }

            }
        }

    }

    private void addChildView(ChildInfo childInfo) {
        if(Utils.stringIsNull(childInfo.getHeadimg())){
            return;
        }
        View childHead = View.inflate(getActivity(),R.layout.add_more_head_img,null);
        CircleImage add_more_head_iv = (CircleImage) childHead.findViewById(R.id.add_more_head_iv);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 15;
        ImageLoaderUtil.displayImage(childInfo.getHeadimg(),add_more_head_iv);
        fragment_mine_child_linear.addView(childHead,params);
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

    private void addSmallChild(Login loginNew, LinearLayout contain, LinearLayout.LayoutParams layoutParams) {
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
