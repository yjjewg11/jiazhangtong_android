package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * ChildActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/24 1:03
 */
public class ChildActivity extends BaseActivity implements View.OnClickListener {
    private View childView;
    private View fatherView;
    private View motherView;
    private View yeView;
    private View naiView;
    private View wgView;
    private View wpView;
    private View schoolView;
    private TextView remarkTv;
    private ImageView remarkIv;

    private final int OWN = 1;
    private final int BA = 2;
    private final int MA = 3;
    private final int YE = 4;
    private final int NAI = 5;
    private final int WG = 6;
    private final int WP = 7;
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
        setTitleText("宝贝详情", "提交");
        childInfo = (ChildInfo) getIntent().getSerializableExtra("child");

        initViews();

        bindData();
    }

    private void initViews() {
        childView = findViewById(R.id.child_own);
        fatherView = findViewById(R.id.child_father);
        motherView = findViewById(R.id.child_mother);
        yeView = findViewById(R.id.child_ye);
        naiView = findViewById(R.id.child_nai);
        wgView = findViewById(R.id.child_wg);
        wpView = findViewById(R.id.child_wp);
        schoolView = findViewById(R.id.child_school);

        remarkTv = (TextView) findViewById(R.id.child_remark);
        remarkIv = (ImageView) findViewById(R.id.child_remark_edit);

        childView.setOnClickListener(this);
        fatherView.setOnClickListener(this);
        motherView.setOnClickListener(this);
        yeView.setOnClickListener(this);
        naiView.setOnClickListener(this);
        wgView.setOnClickListener(this);
        wpView.setOnClickListener(this);
        schoolView.setOnClickListener(this);
        remarkIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.child_own:
                Intent ownIntent = new Intent(mContext, EditChildActivity.class);
                ownIntent.putExtra("head", childInfo.getHeadimg());
                ownIntent.putExtra("name", childInfo.getName());
                ownIntent.putExtra("nick", childInfo.getNickname());
                ownIntent.putExtra("sex", childInfo.getSex());
                ownIntent.putExtra("birth", childInfo.getBirthday());
                startActivityForResult(ownIntent, OWN);
                break;
            case R.id.child_father:
                Intent fatherIntent = new Intent(mContext, EditParentsActivity.class);
                fatherIntent.putExtra("name", childInfo.getBa_name());
                fatherIntent.putExtra("tel", childInfo.getBa_tel());
                fatherIntent.putExtra("work", childInfo.getBa_work());
                startActivityForResult(fatherIntent, BA);
                break;
            case R.id.child_mother:
                Intent motherIntent = new Intent(mContext, EditParentsActivity.class);
                motherIntent.putExtra("name", childInfo.getMa_name());
                motherIntent.putExtra("tel", childInfo.getMa_tel());
                motherIntent.putExtra("work", childInfo.getMa_work());
                startActivityForResult(motherIntent, MA);
                break;
            case R.id.child_ye:
                Intent yeIntent = new Intent(mContext, EditParentsActivity.class);
                yeIntent.putExtra("tel", childInfo.getYe_tel());
                startActivityForResult(yeIntent, YE);
                break;
            case R.id.child_nai:
                Intent naiIntent = new Intent(mContext, EditParentsActivity.class);
                naiIntent.putExtra("tel", childInfo.getNai_tel());
                startActivityForResult(naiIntent, NAI);
                break;
            case R.id.child_wg:
                Intent wgIntent = new Intent(mContext, EditParentsActivity.class);
                wgIntent.putExtra("tel", childInfo.getWaigong_tel());
                startActivityForResult(wgIntent, WG);
                break;
            case R.id.child_wp:
                Intent wpIntent = new Intent(mContext, EditParentsActivity.class);
                wpIntent.putExtra("tel", childInfo.getWaipo_tel());
                startActivityForResult(wpIntent, WP);
                break;
            case R.id.child_school:

                break;
            case R.id.child_remark_edit:
                Intent remarkIntent = new Intent(mContext, EditRemarkActivity.class);
                remarkIntent.putExtra("remark", childInfo.getNote());
                startActivityForResult(remarkIntent, REMARK);
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
                case YE:
                    childInfo.setYe_tel(data.getStringExtra("tel"));
                    break;
                case NAI:
                    childInfo.setNai_tel(data.getStringExtra("tel"));
                    break;
                case WG:
                    childInfo.setWaigong_tel(data.getStringExtra("tel"));
                    break;
                case WP:
                    childInfo.setWaipo_tel(data.getStringExtra("tel"));
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

        bindFather(childInfo.getBa_name(), childInfo.getBa_tel(), childInfo.getBa_work());

        bindMother(childInfo.getMa_name(), childInfo.getMa_tel(), childInfo.getMa_work());

        bindYe(""/*爷爷*/, childInfo.getYe_tel());

        bindNai(""/*奶奶*/, childInfo.getNai_tel());

        bindWg(""/*外公*/, childInfo.getWaigong_tel());

        bindWp(""/*外婆*/, childInfo.getWaipo_tel());

        bindSchool(childInfo.getGroupuuid(), childInfo.getClassuuid());

        bindRemark(childInfo.getNote());
    }

    private void bindOwn(String head, String name, String nick, int sex, String birth) {
        CircleImage headCi = (CircleImage) childView.findViewById(R.id.item_mine_list_head);
        TextView nameTv = (TextView) childView.findViewById(R.id.item_mine_list_name);
        TextView nickTv = (TextView) childView.findViewById(R.id.item_mine_list_nike);
        TextView sexBirthTv = (TextView) childView.findViewById(R.id.item_mine_list_sexb);

        headCi.setImageResource(R.drawable.touxiang);

        ImageLoaderUtil.displayImage(head, headCi);
        nameTv.setText(name);
        nickTv.setText(nick);
        sexBirthTv.setText((sex == 0 ? "男" : "女") + "        " + birth);
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
        LinearLayout workLayout = (LinearLayout) fatherView.findViewById(R.id.item_child_work_layout);

        workLayout.setVisibility(View.VISIBLE);

        titleTv.setText("妈妈");
        nameTv.setText("姓名:" + name);
        telTv.setText("电话:" + tel);
        workTv.setText("工作单位:" + work);
    }

    private void bindYe(String name, String tel) {
        TextView titleTv = (TextView) yeView.findViewById(R.id.item_child_title);
        TextView nameTv = (TextView) yeView.findViewById(R.id.item_child_name);
        TextView telTv = (TextView) yeView.findViewById(R.id.item_child_tel);

        titleTv.setText("爷爷");
        nameTv.setText("姓名:" + name);
        telTv.setText("电话:" + tel);
    }

    private void bindNai(String name, String tel) {
        TextView titleTv = (TextView) naiView.findViewById(R.id.item_child_title);
        TextView nameTv = (TextView) naiView.findViewById(R.id.item_child_name);
        TextView telTv = (TextView) naiView.findViewById(R.id.item_child_tel);

        titleTv.setText("奶奶");
        nameTv.setText("姓名:" + name);
        telTv.setText("电话:" + tel);
    }

    private void bindWg(String name, String tel) {
        TextView titleTv = (TextView) wgView.findViewById(R.id.item_child_title);
        TextView nameTv = (TextView) wgView.findViewById(R.id.item_child_name);
        TextView telTv = (TextView) wgView.findViewById(R.id.item_child_tel);

        titleTv.setText("外公");
        nameTv.setText("姓名:" + name);
        telTv.setText("电话:" + tel);
    }

    private void bindWp(String name, String tel) {
        TextView titleTv = (TextView) wpView.findViewById(R.id.item_child_title);
        TextView nameTv = (TextView) wpView.findViewById(R.id.item_child_name);
        TextView telTv = (TextView) wpView.findViewById(R.id.item_child_tel);

        titleTv.setText("外婆");
        nameTv.setText("姓名:" + name);
        telTv.setText("电话:" + tel);
    }

    private void bindSchool(String name, String tel) {
        TextView titleTv = (TextView) schoolView.findViewById(R.id.item_child_title);
        TextView nameTv = (TextView) schoolView.findViewById(R.id.item_child_name);
        TextView telTv = (TextView) schoolView.findViewById(R.id.item_child_tel);

        titleTv.setVisibility(View.GONE);

        nameTv.setText("关联学校:" + name);
        telTv.setText("关联班级:" + tel);
    }

    private void bindRemark(String remark) {
        TextView remarkTv = (TextView) findViewById(R.id.child_remark);

        remarkTv.setText(remark);
    }

    @Override
    protected void titleRightButtonListener() {
        changeInfo();
    }

    private void changeInfo() {
        showProgressDialog("提交信息中...");
        UserRequest.changeChild(mContext, childInfo, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {

                finish();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                hideProgressDialog();
                Utils.showToast(mContext, message);
            }
        });
    }
}
