package com.wj.kindergarten.ui.mine;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.abstractbean.RequestFailedResult;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.bean.Group;
import com.wj.kindergarten.bean.Ka;
import com.wj.kindergarten.bean.KaInfo;
import com.wj.kindergarten.bean.MineChildTeacherObj;
import com.wj.kindergarten.bean.MineChildTeachers;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.MineSchoolActivity;
import com.wj.kindergarten.ui.func.adapter.FragmentChildStateAdapter;
import com.wj.kindergarten.ui.more.MyCircleView;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildInfoFragment extends Fragment implements View.OnClickListener {


    private View view;
    //    private View childView;
    private View schoolView;
    private View fatherView;
    private View motherView;
    private View otherView;
    private View kaView;
    private TextView remarkTv;
    private ImageView remarkIv;


    int position;
    FragmentChildStateAdapter pagerAdapter;
    private ChildInfo childInfo;
    private ImageView mine_child_new_head_back;
    private ImageView mine_child_new_head_add_child;
    private TextView mine_child_new_head_circle_age;
    private CircleImage mine_child_new_head_circle_img;
    private TextView mine_child_new_head_circle_name;
    private PullToRefreshScrollView activity_child_scroll;
    private MyCircleView mine_child_new_head_circle_fragment;


    public ChildInfoFragment() {
    }

    public ChildInfoFragment(FragmentChildStateAdapter pagerAdapter, int position) {
        this.position = position;
        this.pagerAdapter = pagerAdapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            //解析view
            view = inflater.inflate(R.layout.activity_child, null);
            initViews(view);
        }


        mine_child_new_head_circle_fragment.setScale(position);
        initData();
        bindData();
        return view;
    }

    private void initData() {
        childInfo = pagerAdapter.getChildInfo(position);
    }


    private void bindOwn(String head, String name, String nick, int sex, String birth) {
//        CircleImage headCi = (CircleImage) childView.findViewById(R.id.item_mine_list_head);
//        TextView nameTv = (TextView) childView.findViewById(R.id.item_mine_list_name);
//        TextView nickTv = (TextView) childView.findViewById(R.id.item_mine_list_nike);
//        TextView sexBirthTv = (TextView) childView.findViewById(R.id.item_mine_list_sexb);
//
        if (!Utils.stringIsNull(head)) {
            ImageLoaderUtil.displayImage(head, mine_child_new_head_circle_img);
        } else {
            mine_child_new_head_circle_img.setImageResource(R.drawable.touxiang);
        }

        mine_child_new_head_circle_name.setText(name);
//        nickTv.setText("昵称：" + nick);
//        sexBirthTv.setText((sex == 0 ? "男" : "女") + "     " + birth);
        if (!Utils.stringIsNull(birth)) {
            mine_child_new_head_circle_age.setVisibility(View.VISIBLE);
            mine_child_new_head_circle_age.setText(computeAge(birth) + "岁");
        } else {
            mine_child_new_head_circle_age.setVisibility(View.GONE);
        }
        Drawable drawable;
        if (sex == 0) {
            drawable = getResources().getDrawable(R.drawable.boy_child);
        } else {
            drawable = getResources().getDrawable(R.drawable.girl_child);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mine_child_new_head_circle_name.setCompoundDrawables(null, null, drawable, null);

    }

    private int computeAge(String birth) {
        long cha = TimeUtil.getYMDnowTime() - TimeUtil.getMillionFromYMD(birth);
        cha = cha / (1000 * 60 * 60 * 24);
        CGLog.v("打印多少天 : " + cha);
        return (int) (cha < 0 ? 0 : cha / 365);
    }


    private void initViews(View rootView) {

        activity_child_scroll = (PullToRefreshScrollView) rootView.findViewById(R.id.activity_child_scroll);
        mine_child_new_head_back = (ImageView) rootView.findViewById(R.id.mine_child_new_head_back);
        mine_child_new_head_add_child = (ImageView) rootView.findViewById(R.id.mine_child_new_head_add_child);
        mine_child_new_head_circle_img = (CircleImage) rootView.findViewById(R.id.mine_child_new_head_circle_img);
        mine_child_new_head_circle_age = (TextView) rootView.findViewById(R.id.mine_child_new_head_circle_age);
        mine_child_new_head_circle_name = (TextView) rootView.findViewById(R.id.mine_child_new_head_circle_name);
        mine_child_new_head_circle_fragment = (MyCircleView) rootView.findViewById(R.id.mine_child_new_head_circle_fragment);
        mine_child_new_head_circle_fragment.setRadius(10);
        mine_child_new_head_circle_fragment.setDistance(40);
        mine_child_new_head_circle_fragment.setCount(pagerAdapter.getCount());

        activity_child_scroll.setMode(PullToRefreshBase.Mode.DISABLED);
//        activity_child_scroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                ((ChildActivity) getActivity()).scrollPoint(activity_child_scroll.getScrollY());
//            }
//        });
        mine_child_new_head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mine_child_new_head_add_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ownIntent = new Intent(getActivity(), EditChildActivity.class);
                getActivity().startActivityForResult(ownIntent, GloablUtils.OWN);
            }
        });

//        childView = rootView.findViewById(R.id.child_own);
        fatherView = rootView.findViewById(R.id.child_father);
        motherView = rootView.findViewById(R.id.child_mother);
        otherView = rootView.findViewById(R.id.child_other);
        schoolView = rootView.findViewById(R.id.child_school);
        kaView = rootView.findViewById(R.id.child_ka);
        //隐藏关联卡号
        kaView.setVisibility(View.GONE);

        remarkTv = (TextView) rootView.findViewById(R.id.child_remark);
        remarkIv = (ImageView) rootView.findViewById(R.id.child_remark_edit);

        mine_child_new_head_circle_img.setOnClickListener(this);
        fatherView.setOnClickListener(this);
        motherView.setOnClickListener(this);
        otherView.setOnClickListener(this);
        remarkIv.setOnClickListener(this);
        remarkTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_child_new_head_circle_img:
                Intent ownIntent = new Intent(getActivity(), EditChildActivity.class);
                ownIntent.putExtra("childInfo", childInfo);
                getActivity().startActivityForResult(ownIntent, GloablUtils.OWN);
                break;
            case R.id.child_father:
                Intent fatherIntent = new Intent(getActivity(), EditParentsActivity.class);
                fatherIntent.putExtra("isFather", true);
                fatherIntent.putExtra("childInfo", childInfo);
                getActivity().startActivityForResult(fatherIntent, GloablUtils.BA);
                break;
            case R.id.child_mother:
                Intent motherIntent = new Intent(getActivity(), EditParentsActivity.class);
                motherIntent.putExtra("isFather", false);
                motherIntent.putExtra("childInfo", childInfo);
                getActivity().startActivityForResult(motherIntent, GloablUtils.MA);
                break;
            case R.id.child_other:
                Intent otherIntent = new Intent(getActivity(), EditOthersActivity.class);
                otherIntent.putExtra("childInfo", childInfo);
                getActivity().startActivityForResult(otherIntent, GloablUtils.OTHER);
                break;
            case R.id.child_school:

                break;
            case R.id.child_remark_edit:
            case R.id.child_remark:
                Intent remarkIntent = new Intent(getActivity(), EditRemarkActivity.class);
                remarkIntent.putExtra("childInfo", childInfo);
                getActivity().startActivityForResult(remarkIntent, GloablUtils.REMARK);
                break;
            default:
                break;
        }
    }


    private void bindData() {
        bindOwn(checkIsNull(childInfo.getHeadimg()), checkIsNull(childInfo.getName()),
                checkIsNull(childInfo.getNickname()), childInfo.getSex(),
                checkIsNull(childInfo.getBirthday()));

        bindSchool(childInfo.getGroupuuid(), childInfo.getClassuuid());

        bindFather(checkIsNull(childInfo.getBa_name()), checkIsNull(childInfo.getBa_tel()), checkIsNull(childInfo.getBa_work()));

        bindMother(checkIsNull(childInfo.getMa_name()), checkIsNull(childInfo.getMa_tel()), checkIsNull(childInfo.getMa_work()));
        bindOther(checkIsNull(childInfo.getYe_tel()), checkIsNull(childInfo.getNai_tel()), checkIsNull(childInfo.getWaigong_tel()), checkIsNull(childInfo.getWaipo_tel()));
        bindRemark(childInfo.getNote());
        queryKaInfo();
        queryTeachers();

    }

    private void queryTeachers() {
        if (Utils.stringIsNull(childInfo.getClassuuid())) return;
        UserRequest.getMineChildTeacher(getActivity(), childInfo.getClassuuid(), new RequestFailedResult() {
            @Override
            public void result(BaseModel domain) {
                MineChildTeachers mineChildTeachers = (MineChildTeachers) domain;
                if (mineChildTeachers.getList() != null && mineChildTeachers.getList().size() > 0) {
                    addTeachers(mineChildTeachers.getList());
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }
        });
    }

    private void addTeachers(List<MineChildTeacherObj> list) {
        LinearLayout item_child_parent_teachers = (LinearLayout) schoolView.findViewById(R.id.item_child_parent_teachers);
        for (int i = 0; i < list.size(); i++) {
            MineChildTeacherObj info = list.get(i);
            if (null != info) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_child_ka_info_1, null);
                TextView textView = (TextView) view.findViewById(R.id.item_ka);
                Drawable drawable = getResources().getDrawable(R.drawable.youjiantou);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(null, null, drawable, null);
                LinearLayout line = (LinearLayout) view.findViewById(R.id.view_ka);
                textView.setText("班主任" + (i + 1) + ":" + Utils.isNull(info.getName()));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                if (i + 1 == list.size()) {
                    line.setVisibility(View.GONE);
                } else {
                    line.setVisibility(View.VISIBLE);
                }
                item_child_parent_teachers.addView(view);
            }

        }
    }

    private String checkIsNull(String str) {
        return Utils.isNull(str);
    }

    private void queryKaInfo() {
        UserRequest.queryKaInfo(getActivity(), childInfo.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                Ka ka = (Ka) domain;
                if (null != ka && ka.getList() != null && ka.getList().size() > 0) {
                    kaView.setVisibility(View.GONE);
                    bindKa(ka);
                } else {
                    kaView.setVisibility(View.GONE);
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void bindKa(Ka ka) {
        LinearLayout layout = (LinearLayout) schoolView.findViewById(R.id.item_child_parent_relative_card);
        layout.removeAllViews();
        ArrayList<KaInfo> infos = ka.getList();
        if (infos.size() > 0) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
        }
        for (int i = 0; i < infos.size(); i++) {
            KaInfo info = infos.get(i);
            if (null != info) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_child_ka_info_1, null);
                TextView textView = (TextView) view.findViewById(R.id.item_ka);
                LinearLayout line = (LinearLayout) view.findViewById(R.id.view_ka);
                textView.setText(info.getName() + ":" + Utils.isNull(info.getCardid()));
                if (i + 1 == infos.size()) {
                    line.setVisibility(View.GONE);
                } else {
                    line.setVisibility(View.VISIBLE);
                }
                layout.addView(view);
            }
        }
    }


    private void bindSchool(final String sid, String cid) {
        TextView titleTv = (TextView) schoolView.findViewById(R.id.item_child_title);
        TextView nameTv = (TextView) schoolView.findViewById(R.id.item_child_name);
        TextView telTv = (TextView) schoolView.findViewById(R.id.item_child_tel);
        LinearLayout item_child_parent_relative_card = (LinearLayout) schoolView.findViewById(R.id.item_child_parent_relative_card);
        titleTv.setVisibility(View.GONE);
        telTv.setVisibility(View.GONE);
        item_child_parent_relative_card.setVisibility(View.VISIBLE);

        String schoolName = getSchool(sid);
        if (!Utils.stringIsNull(getClass(cid))) {
            schoolName = schoolName + "-" + getClass(cid);
        }
        nameTv.setText("学校-班级:" + schoolName);
        telTv.setText("班主任:" + "暂未填");
        nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动相关联的学校
                if (TextUtils.isEmpty(sid) || sid.equals("0")) return;
                Intent intent = new Intent(getActivity(), MineSchoolActivity.class);
                intent.putExtra("groupuuid", sid);
                startActivity(intent);
            }
        });
        telTv.setCompoundDrawables(null, null, null, null);

    }

    private String getSchool(String id) {
        if (null != CGApplication.getInstance().getLogin() && CGApplication.getInstance().getLogin().getGroup_list() != null) {
            for (Group group : CGApplication.getInstance().getLogin().getGroup_list()) {
                if (null != group && group.getUuid().equals(id)) {
                    return group.getBrand_name();
                }
            }
        }
        return "";
    }

    private String getClass(String id) {
        if (null != CGApplication.getInstance().getLogin() && CGApplication.getInstance().getLogin().getClass_list() != null) {
            for (com.wj.kindergarten.bean.Class c : CGApplication.getInstance().getLogin().getClass_list()) {
                if (null != c && c.getUuid().equals(id)) {
                    return c.getName();
                }
            }
        }
        return "";
    }


    private void bindFather(String name, String tel, String work) {
        TextView titleTv = (TextView) fatherView.findViewById(R.id.item_child_title);
        TextView nameTv = (TextView) fatherView.findViewById(R.id.item_child_name);
        TextView telTv = (TextView) fatherView.findViewById(R.id.item_child_tel);
        TextView workTv = (TextView) fatherView.findViewById(R.id.item_child_work);
        LinearLayout workLayout = (LinearLayout) fatherView.findViewById(R.id.item_child_work_layout);

        workLayout.setVisibility(View.VISIBLE);

        titleTv.setText("爸爸");
        nameTv.setText("姓名:" + name);
        telTv.setText("电话:" + tel);
        workTv.setText("工作单位:" + work);
    }

    private void bindMother(String name, String tel, String work) {
        TextView titleTv = (TextView) motherView.findViewById(R.id.item_child_title);
        TextView nameTv = (TextView) motherView.findViewById(R.id.item_child_name);
        TextView telTv = (TextView) motherView.findViewById(R.id.item_child_tel);
        TextView workTv = (TextView) motherView.findViewById(R.id.item_child_work);
        LinearLayout workLayout = (LinearLayout) motherView.findViewById(R.id.item_child_work_layout);

        workLayout.setVisibility(View.VISIBLE);

        titleTv.setText("妈妈");
        nameTv.setText("姓名:" + name);
        telTv.setText("电话:" + tel);
        workTv.setText("工作单位:" + work);
    }

    private void bindOther(String tel1, String tel2, String tel3, String tel4) {
        TextView textView1 = (TextView) otherView.findViewById(R.id.item_child_1);
        TextView textView2 = (TextView) otherView.findViewById(R.id.item_child_2);
        TextView textView3 = (TextView) otherView.findViewById(R.id.item_child_3);
        TextView textView4 = (TextView) otherView.findViewById(R.id.item_child_4);

        textView1.setText("爷爷:" + tel1);
        textView2.setText("奶奶:" + tel2);
        textView3.setText("外公:" + tel3);
        textView4.setText("外婆:" + tel4);
    }

//    private void bindKa(String ka, String tel2, String tel3, String tel4) {
//        TextView textView1 = (TextView) otherView.findViewById(R.id.item_child_1);
//        TextView textView2 = (TextView) otherView.findViewById(R.id.item_child_2);
//        TextView textView3 = (TextView) otherView.findViewById(R.id.item_child_3);
//        TextView textView4 = (TextView) otherView.findViewById(R.id.item_child_4);
//
//        textView1.setText("爷爷:" + tel1);
//        textView2.setText("奶奶:" + tel2);
//        textView3.setText("外公:" + tel3);
//        textView4.setText("外婆:" + tel4);
//    }

    private void bindRemark(String remark) {
        TextView remarkTv = (TextView) view.findViewById(R.id.child_remark);
        remarkTv.setText(remark);
    }

}
