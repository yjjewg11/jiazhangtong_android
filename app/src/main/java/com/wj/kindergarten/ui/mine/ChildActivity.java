package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * ChildActivity
 *
 * @Description:宝贝详情
 * @Author: Wave
 * @CreateDate: 2015/7/24 1:03
 */
public class ChildActivity extends BaseActivity implements View.OnClickListener {
    private View childView;
    private View schoolView;
    private View fatherView;
    private View motherView;
    private View otherView;
    private View kaView;
    private TextView remarkTv;
    private ImageView remarkIv;

    private final int OWN = 1;
    private final int BA = 2;
    private final int MA = 3;
    private final int OTHER = 4;
    private final int SCHOOL = 8;
    private final int REMARK = 9;

    private ChildInfo childInfo;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_child;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("详细信息");
        childInfo = getChildInfoByUUID(getIntent().getStringExtra("uuid"));

        initViews();

        bindData();
    }

    private ChildInfo getChildInfoByUUID(String uuid) {
        if (CGApplication.getInstance().getLogin() != null && CGApplication.getInstance().getLogin().getList() != null) {
            for (ChildInfo childInfo : CGApplication.getInstance().getLogin().getList()) {
                if (null != childInfo && childInfo.getUuid().equals(uuid)) {
                    return childInfo;
                }
            }
        }
        return new ChildInfo();
    }

    private void initViews() {
        childView = findViewById(R.id.child_own);
        fatherView = findViewById(R.id.child_father);
        motherView = findViewById(R.id.child_mother);
        otherView = findViewById(R.id.child_other);
        schoolView = findViewById(R.id.child_school);
        kaView = findViewById(R.id.child_ka);

        remarkTv = (TextView) findViewById(R.id.child_remark);
        remarkIv = (ImageView) findViewById(R.id.child_remark_edit);

        childView.setOnClickListener(this);
        fatherView.setOnClickListener(this);
        motherView.setOnClickListener(this);
        otherView.setOnClickListener(this);
        remarkIv.setOnClickListener(this);
        remarkTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.child_own:
                Intent ownIntent = new Intent(mContext, EditChildActivity.class);
                ownIntent.putExtra("childInfo", childInfo);
                startActivityForResult(ownIntent, OWN);
                break;
            case R.id.child_father:
                Intent fatherIntent = new Intent(mContext, EditParentsActivity.class);
                fatherIntent.putExtra("isFather", true);
                fatherIntent.putExtra("childInfo", childInfo);
                startActivityForResult(fatherIntent, BA);
                break;
            case R.id.child_mother:
                Intent motherIntent = new Intent(mContext, EditParentsActivity.class);
                motherIntent.putExtra("isFather", false);
                motherIntent.putExtra("childInfo", childInfo);
                startActivityForResult(motherIntent, MA);
                break;
            case R.id.child_other:
                Intent otherIntent = new Intent(mContext, EditOthersActivity.class);
                otherIntent.putExtra("childInfo", childInfo);
                startActivityForResult(otherIntent, OTHER);
                break;
            case R.id.child_school:

                break;
            case R.id.child_remark_edit:
            case R.id.child_remark:
                Intent remarkIntent = new Intent(mContext, EditRemarkActivity.class);
                remarkIntent.putExtra("childInfo", childInfo);
                startActivityForResult(remarkIntent, REMARK);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case OWN:
                    childInfo.setHeadimg(data.getStringExtra("head"));
                    childInfo.setName(data.getStringExtra("name"));
                    childInfo.setNickname(data.getStringExtra("nick"));
                    childInfo.setSex(data.getIntExtra("sex", 0));
                    childInfo.setBirthday(data.getStringExtra("birth"));
                    childInfo.setIdcard(data.getStringExtra("idCard"));
                    break;
                case BA:
                    childInfo.setBa_name(data.getStringExtra("name"));
                    childInfo.setBa_tel(data.getStringExtra("tel"));
                    childInfo.setBa_work(data.getStringExtra("work"));
                    break;
                case MA:
                    childInfo.setMa_name(data.getStringExtra("name"));
                    childInfo.setMa_tel(data.getStringExtra("tel"));
                    childInfo.setMa_work(data.getStringExtra("work"));
                    break;
                case OTHER:
                    childInfo.setYe_tel(data.getStringExtra("tel1"));
                    childInfo.setNai_tel(data.getStringExtra("tel2"));
                    childInfo.setWaigong_tel(data.getStringExtra("tel3"));
                    childInfo.setWaipo_tel(data.getStringExtra("tel4"));
                    break;
                case SCHOOL:
                    break;
                case REMARK:
                    childInfo.setNote(data.getStringExtra("remark"));
                    break;
            }
            bindData();
        }
    }

    private void bindData() {
        bindOwn(childInfo.getHeadimg(), childInfo.getName(), childInfo.getNickname(), childInfo.getSex(),
                childInfo.getBirthday());

        bindSchool(childInfo.getGroupuuid(), childInfo.getClassuuid());

        bindFather(childInfo.getBa_name(), childInfo.getBa_tel(), childInfo.getBa_work());

        bindMother(childInfo.getMa_name(), childInfo.getMa_tel(), childInfo.getMa_work());
        bindOther(childInfo.getYe_tel(), childInfo.getNai_tel(), childInfo.getWaigong_tel(), childInfo.getWaipo_tel());
        bindRemark(childInfo.getNote());
        queryKaInfo();
    }

    private void queryKaInfo() {
        UserRequest.queryKaInfo(ChildActivity.this, childInfo.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                Ka ka = (Ka)domain;
                if (null != ka && ka.getList() != null && ka.getList().size() > 0) {
                    kaView.setVisibility(View.VISIBLE);
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
        LinearLayout layout = (LinearLayout) kaView.findViewById(R.id.layout_ka);
        ArrayList<KaInfo> infos = ka.getList();
        for (int i = 0; i < infos.size(); i++) {
            KaInfo info = infos.get(i);
            if (null != info) {
                View view = LayoutInflater.from(ChildActivity.this).inflate(R.layout.item_child_ka_info_1, null);
                TextView textView = (TextView) view.findViewById(R.id.item_ka);
                LinearLayout line = (LinearLayout) view.findViewById(R.id.view_ka);
                textView.setText(info.getName() + ":" + info.getCardid());
                if (i + 1 == infos.size()) {
                    line.setVisibility(View.GONE);
                } else {
                    line.setVisibility(View.VISIBLE);
                }
                layout.addView(view);
            }
        }
    }

    private void bindOwn(String head, String name, String nick, int sex, String birth) {
        CircleImage headCi = (CircleImage) childView.findViewById(R.id.item_mine_list_head);
        TextView nameTv = (TextView) childView.findViewById(R.id.item_mine_list_name);
        TextView nickTv = (TextView) childView.findViewById(R.id.item_mine_list_nike);
        TextView sexBirthTv = (TextView) childView.findViewById(R.id.item_mine_list_sexb);

        if (!Utils.stringIsNull(head)) {
            ImageLoaderUtil.displayImage(head, headCi);
        } else {
            headCi.setImageResource(R.drawable.touxiang);
        }

        nameTv.setText(name);
        nickTv.setText("昵称：" + nick);
        sexBirthTv.setText((sex == 0 ? "男" : "女") + "     " + birth);
    }

    private void bindSchool(final String sid, String cid) {
        TextView titleTv = (TextView) schoolView.findViewById(R.id.item_child_title);
        TextView nameTv = (TextView) schoolView.findViewById(R.id.item_child_name);
        TextView telTv = (TextView) schoolView.findViewById(R.id.item_child_tel);

        titleTv.setVisibility(View.GONE);

        nameTv.setText("关联学校:" + getSchool(sid));
        telTv.setText("关联班级:" + getClass(cid));
        nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动相关联的学校
                if(TextUtils.isEmpty(sid)) return;
                Intent intent = new Intent(ChildActivity.this, MineSchoolActivity.class);
                intent.putExtra("groupuuid", sid);
                startActivity(intent);
            }
        });
        telTv.setCompoundDrawables(null,null,null,null);
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
        TextView remarkTv = (TextView) findViewById(R.id.child_remark);
        remarkTv.setText(remark);
    }
}
