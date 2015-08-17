package com.wj.kindergarten.ui.func;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AppraiseTeacher;
import com.wj.kindergarten.bean.AppraiseTeacherList;
import com.wj.kindergarten.bean.AppraiseTeacherOver;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.AppraiseTeacherAdapter;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AppraiseTeacher
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/5 23:01
 */
public class AppraiseTeacherActivity extends BaseActivity {
    private ListView mListView;
    private LinearLayout contentLayout;

    private AppraiseTeacherAdapter appraiseTeacherAdapter;
    private Map<String, AppraiseTeacherOver> alreadyTeacherMap = new HashMap<>();
    private List<AppraiseTeacher> teachers = new ArrayList<>();

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_appraise_teacher;
    }

    @Override
    protected void setNeedLoading() {
        isNeedLoading = true;
    }

    @Override
    protected void loadData() {
        setTitleText("评价老师");
        hideTitleLine();
        getAppraiseTeacher();
    }

    @Override
    protected void onCreate() {
        setTitleText("评价老师");
        hideTitleLine();

        contentLayout = (LinearLayout) findViewById(R.id.appraise_teacher_content);
        mListView = (ListView) findViewById(R.id.appraise_teacher_list);
//        appraiseTeacherAdapter = new AppraiseTeacherAdapter(mContext);
//        appraiseTeacherAdapter.setTeachers(teachers);
//        appraiseTeacherAdapter.setAlreadyTeacherMap(alreadyTeacherMap);
//        mListView.setAdapter(appraiseTeacherAdapter);
        addTeachers();
    }

    private void addTeachers() {
        contentLayout.removeAllViews();
        for (int i = 0; i < teachers.size(); i++) {
            View cView = View.inflate(mContext, R.layout.item_appraise_teacher_list, null);
            TextView nameTv = (TextView) cView.findViewById(R.id.item_appraise_name);
            final TextView goodTv = (TextView) cView.findViewById(R.id.item_appraise_good);
            final TextView normalTv = (TextView) cView.findViewById(R.id.item_appraise_normal);
            final TextView badTv = (TextView) cView.findViewById(R.id.item_appraise_bad);
            final EditText contentTv = (EditText) cView.findViewById(R.id.item_appraise_edit);
            final ImageView submitIv = (ImageView) cView.findViewById(R.id.item_appraise_submit);

            final AppraiseTeacher appraiseTeacher = teachers.get(i);
            if (alreadyTeacherMap.containsKey(appraiseTeacher.getTeacher_uuid())
                    && !alreadyTeacherMap.get(appraiseTeacher.getTeacher_uuid()).isEditState()) {
                contentTv.setEnabled(false);
                AppraiseTeacherOver appraiseTeacherOver = alreadyTeacherMap.get(appraiseTeacher.getTeacher_uuid());
                if (appraiseTeacherOver != null) {
                    nameTv.setText(appraiseTeacher.getName());
                    if ("1".equals(appraiseTeacherOver.getType())) {
                        Drawable drawable = getResources().getDrawable(R.drawable.appraise_good_after);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                        goodTv.setCompoundDrawables(null, drawable, null, null);
                    } else if ("2".equals(appraiseTeacherOver.getType())) {
                        Drawable drawable = getResources().getDrawable(R.drawable.appraise_n_after);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        normalTv.setCompoundDrawables(null, drawable, null, null);
                    } else if ("3".equals(appraiseTeacherOver.getType())) {
                        Drawable drawable = getResources().getDrawable(R.drawable.appraise_un_after);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        badTv.setCompoundDrawables(null, drawable, null, null);
                    }
                    contentTv.setText(appraiseTeacherOver.getContent());
                    submitIv.setImageResource(R.drawable.appraise_commit_no);
                }
            } else {
                nameTv.setText(appraiseTeacher.getName());
                submitIv.setImageResource(R.drawable.appraise_commit_yes);
                submitIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int type = 1;
                        Drawable drawableGood = goodTv.getCompoundDrawables()[1];
                        Drawable drawableNormal = normalTv.getCompoundDrawables()[1];
                        Drawable drawableBad = badTv.getCompoundDrawables()[1];
                        if (getResources().getDrawable(R.drawable.appraise_good_after).getConstantState()
                                .equals(drawableGood.getConstantState())) {
                            type = 1;
                        } else if (getResources().getDrawable(R.drawable.appraise_n_after).getConstantState()
                                .equals(drawableNormal.getConstantState())) {
                            type = 2;
                        } else if (getResources().getDrawable(R.drawable.appraise_un_after).getConstantState()
                                .equals(drawableBad.getConstantState())) {
                            type = 3;
                        }
                        AppraiseTeacherOver appraiseTeacherOverTemp = new AppraiseTeacherOver();
                        appraiseTeacherOverTemp.setType(type + "");
                        appraiseTeacherOverTemp.setContent(contentTv.getText().toString());
                        appraiseTeacherOverTemp.setTeacheruuid(appraiseTeacher.getTeacher_uuid());
                        appraiseTeacherOverTemp.setIsEditState(true);
                        alreadyTeacherMap.put(appraiseTeacher.getTeacher_uuid(), appraiseTeacherOverTemp);

                        appraiseTeacher(contentTv.getText().toString(), appraiseTeacher.getTeacher_uuid(),
                                type, goodTv, normalTv, badTv, contentTv, submitIv);
                    }
                });
                goodTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Drawable drawable1 = getResources().getDrawable(R.drawable.appraise_good_after);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        Drawable drawable2 = getResources().getDrawable(R.drawable.appraise_n_normal);
                        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                        Drawable drawable3 = getResources().getDrawable(R.drawable.appraise_un_before);
                        drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
                        goodTv.setCompoundDrawables(null, drawable1, null, null);
                        normalTv.setCompoundDrawables(null, drawable2, null, null);
                        badTv.setCompoundDrawables(null, drawable3, null, null);
                    }
                });
                normalTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Drawable drawable1 = getResources().getDrawable(R.drawable.appraise_good_before);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        Drawable drawable2 = getResources().getDrawable(R.drawable.appraise_n_after);
                        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                        Drawable drawable3 = getResources().getDrawable(R.drawable.appraise_un_before);
                        drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
                        goodTv.setCompoundDrawables(null, drawable1, null, null);
                        normalTv.setCompoundDrawables(null, drawable2, null, null);
                        badTv.setCompoundDrawables(null, drawable3, null, null);
                    }
                });
                badTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Drawable drawable1 = getResources().getDrawable(R.drawable.appraise_good_before);
                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                        Drawable drawable2 = getResources().getDrawable(R.drawable.appraise_n_normal);
                        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                        Drawable drawable3 = getResources().getDrawable(R.drawable.appraise_un_after);
                        drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
                        goodTv.setCompoundDrawables(null, drawable1, null, null);
                        normalTv.setCompoundDrawables(null, drawable2, null, null);
                        badTv.setCompoundDrawables(null, drawable3, null, null);
                    }
                });
            }
            contentLayout.addView(cView);
        }
    }

    private void getAppraiseTeacher() {
        UserRequest.getAppraiseTeacherList(mContext, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                AppraiseTeacherList appraiseTeacherList = (AppraiseTeacherList) domain;
                if (appraiseTeacherList != null) {
                    teachers = appraiseTeacherList.getList();
                    if (teachers == null || teachers.size() < 1) {
                        loadEmpty();
                        return;
                    }
                    if (appraiseTeacherList.getList_judge() != null) {
                        for (AppraiseTeacherOver appraiseTeacherOver : appraiseTeacherList.getList_judge()) {
                            alreadyTeacherMap.put(appraiseTeacherOver.getTeacheruuid(), appraiseTeacherOver);
                        }
                    }

                    loadSuc();
                } else {
                    loadFailed();
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                loadFailed();
            }
        });
    }

    public void appraiseTeacher(String content, final String teacherUUID, int type,
                                final TextView goodTv, final TextView normalTv, final TextView noTv, final EditText editText, final ImageView iv) {
        showProgressDialog("提交评价中");
        UserRequest.appraiseTeacher(mContext, content, teacherUUID, type, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                hideProgressDialog();

                goodTv.setEnabled(false);
                normalTv.setEnabled(false);
                noTv.setEnabled(false);
                editText.setEnabled(false);
                iv.setEnabled(false);
                iv.setImageResource(R.drawable.appraise_commit_no);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                hideProgressDialog();
                Utils.showToast(mContext, message);
//                appraiseTeacherAdapter.setTeacherIsEditByKey(teacherUUID);
            }
        });
    }
}
