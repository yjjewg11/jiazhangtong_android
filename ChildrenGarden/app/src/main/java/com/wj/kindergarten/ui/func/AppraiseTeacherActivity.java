package com.wj.kindergarten.ui.func;

import android.widget.ListView;

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

        mListView = (ListView) findViewById(R.id.appraise_teacher_list);
        appraiseTeacherAdapter = new AppraiseTeacherAdapter(mContext);
        appraiseTeacherAdapter.setTeachers(teachers);
        appraiseTeacherAdapter.setAlreadyTeacherMap(alreadyTeacherMap);
        mListView.setAdapter(appraiseTeacherAdapter);
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

    public void appraiseTeacher(String content, final String teacherUUID, int type) {
        showProgressDialog("提交评价中");
        UserRequest.appraiseTeacher(mContext, content, teacherUUID, type, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                hideProgressDialog();

            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                hideProgressDialog();
                Utils.showToast(mContext, message);
                appraiseTeacherAdapter.setTeacherIsEditByKey(teacherUUID);
            }
        });
    }
}
