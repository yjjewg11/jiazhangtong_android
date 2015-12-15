package com.wj.kindergarten.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.wj.kindergarten.ui.mine.ChildActivity;
import com.wj.kindergarten.ui.mine.EditChildActivity;
import com.wj.kindergarten.ui.mine.SettingActivity;
import com.wj.kindergarten.ui.mine.store.StoreActivity;
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
    private LinearLayout childContent;
    private LinearLayout llSetting;
    private Login login;
    private LinearLayout mine_collect;
    private LinearLayout mine_course;
    private LinearLayout recruit_school;
    private ImageView mine_add_child;
    private View.OnClickListener addListeners;
    private HorizontalScrollView mine_head_horizontal_scroll;
    public static MineFragment instance;


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
        addChildren();
        return rootView;
    }

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
        mine_head_horizontal_scroll = (HorizontalScrollView)rootView.findViewById(R.id.mine_head_horizontal_scroll);
        mine_head_horizontal_scroll.setOnTouchListener(null);
        mine_add_child = (ImageView) rootView.findViewById(R.id.mine_add_child);
        mine_add_child.setOnClickListener(addListeners);
        childContent = (LinearLayout) rootView.findViewById(R.id.mine_content);
        mine_collect = (LinearLayout) rootView.findViewById(R.id.ll_store);
        mine_course = (LinearLayout) rootView.findViewById(R.id.ll_special_course);
        recruit_school = (LinearLayout) rootView.findViewById(R.id.ll_mine_school);

        llSetting = (LinearLayout) rootView.findViewById(R.id.ll_setting);
        llSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });

        mine_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), StoreActivity.class));
            }
        });
        mine_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击启动我的课程页面
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

    public void addChildren() {
        Login loginNew = CGApplication.getInstance().getLogin();
        childContent.removeAllViews();
        if (loginNew != null && loginNew.getList() != null) {
            int i = 0;
            for (final ChildInfo childInfo : loginNew.getList()) {
                View view = View.inflate(getActivity(), R.layout.mine_children_head, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.
                        LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //动态设置判断添加
                int size = loginNew.getList().size();
                if (size == 1) {
                    setMargin(layoutParams, WindowUtils.dm.widthPixels / 2);
                } else if (size == 2) {
                    setMargin(layoutParams, WindowUtils.dm.widthPixels / 4);
                } else if (size == 3) {
                    setMargin(layoutParams, WindowUtils.dm.widthPixels / 6);
                } else if (size > 3) {
                    setMargin(layoutParams, WindowUtils.dm.widthPixels / 7);
                    if (i == 0) {
                        layoutParams.leftMargin = WindowUtils.dm.widthPixels / 7 / 2;
                    }
                    if (i == loginNew.getList().size() - 1) {
                        layoutParams.rightMargin = WindowUtils.dm.widthPixels / 7 / 2;
                    }
                }

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

                childContent.addView(view, layoutParams);
                i++;
            }

            if (loginNew.getList().size() == 0) {
                View view = View.inflate(getActivity(), R.layout.mine_children_head, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.
                        LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CircleImage headIv = (CircleImage) view.findViewById(R.id.circle_mine_image);
                TextView nameTv = (TextView) view.findViewById(R.id.tv_children_name);
                headIv.setImageResource(R.drawable.tianjiaxiaohai);
                nameTv.setText("添加宝宝");
                setMargin(layoutParams, WindowUtils.dm.widthPixels / 2);
                view.setOnClickListener(addListeners);
                childContent.addView(view, layoutParams);
            }
        }
    }

}
