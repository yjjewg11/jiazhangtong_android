package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AddressBook;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Teacher;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.AddressBookRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.addressbook.EmotManager;
import com.wj.kindergarten.ui.addressbook.LeaderMessageActivty;
import com.wj.kindergarten.ui.addressbook.TeacherMessageActivty;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * MainFragment
 *
 * @Description: 通讯录
 * @Author: Wave
 * @CreateDate: 2015/7/17 11:34
 */
public class TeachersActivity extends BaseActivity {
    private LinearLayout layoutTeachers = null;

    private RelativeLayout layoutLoad = null;
    private ImageView ivLoadFailure = null;
    private TextView tvLoadInfo = null;
    private ProgressBar progressBar = null;
    private ScrollView scrollView = null;

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.fragment_teachers;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("通讯录");
        initViews();
    }


    private void initViews() {
        layoutTeachers = (LinearLayout) findViewById(R.id.layout_teachers);
        scrollView = (ScrollView) findViewById(R.id.sv_address_book);
        scrollView.setVisibility(View.GONE);
        ivLoadFailure = (ImageView) findViewById(R.id.iv_load_failure);
        progressBar = (ProgressBar) findViewById(R.id.info_loading_progress);
        tvLoadInfo = (TextView) findViewById(R.id.info_loading_load);
        layoutLoad = (RelativeLayout) findViewById(R.id.layout_reload_2);
        queryTeachers();

        if (EmotManager.getEmots().size() == 0) {
            AddressBookRequest.getEmot(this);
        }
    }

    private void queryTeachers() {
        AddressBookRequest.getTeachers(this, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                AddressBook addressBook = (AddressBook) domain;
                if (null != addressBook) {
                    layoutLoad.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    success(addressBook);
                } else {
                    empty();
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(TeachersActivity.this, message);
                }

                loadFailure();
            }
        });
    }

    private void success(AddressBook book) {
        layoutTeachers.removeAllViews();
        ArrayList<Teacher> t1 = book.getListKD();//园长
        if (null != t1 && t1.size() > 0) {
            int count = t1.size();
            for (int i = 0; i < count; i++) {
                addLeader(i, t1.get(i), count);
            }

            View view = LayoutInflater.from(this).inflate(R.layout.fragment_teacher_item_2, null);
            layoutTeachers.addView(view);
        }

        ArrayList<Teacher> teachers = book.getList();//教师
        if (null != teachers && teachers.size() > 0) {
            int count = teachers.size();
            for (int i = 0; i < count; i++) {
                addTeacher(i, teachers.get(i), count);
            }
        }
    }

    private void addLeader(int i, final Teacher teacher, int count) {
        if (null != teacher) {
            View view = LayoutInflater.from(this).inflate(R.layout.fragment_teacher_item, null);
            CircleImage head = (CircleImage) view.findViewById(R.id.iv_head);
            if (!Utils.stringIsNull(teacher.getImg())) {
                ImageLoaderUtil.displayImage(teacher.getImg(), head);
            } else {
                head.setImageDrawable(getResources().getDrawable(R.drawable.ty));
            }
            TextView name = (TextView) view.findViewById(R.id.tv_name);
            name.setText(Utils.getText(teacher.getName()));
            ImageView call = (ImageView) view.findViewById(R.id.iv_call);
            call.setVisibility(View.GONE);
            ImageView message = (ImageView) view.findViewById(R.id.iv_message);
            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TeachersActivity.this, LeaderMessageActivty.class);
                    intent.putExtra("teacher", teacher);
                    startActivity(intent);
                }
            });
            LinearLayout line = (LinearLayout) view.findViewById(R.id.layout_line);
            if (i + 1 == count) {
                line.setVisibility(View.GONE);
            }

            layoutTeachers.addView(view);
        }
    }

    private void addTeacher(int i, final Teacher teacher, int count) {
        if (null != teacher) {
            View view = LayoutInflater.from(this).inflate(R.layout.fragment_teacher_item, null);
            CircleImage head = (CircleImage) view.findViewById(R.id.iv_head);
            ImageLoaderUtil.displayImage(teacher.getImg(), head);
            TextView name = (TextView) view.findViewById(R.id.tv_name);
            name.setText(Utils.getText(teacher.getName()));
            ImageView call = (ImageView) view.findViewById(R.id.iv_call);
            if (!Utils.stringIsNull(teacher.getTel())) {
                call.setVisibility(View.VISIBLE);
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + teacher.getTel()));
                        startActivity(phoneIntent);
                    }
                });
            } else {
                call.setVisibility(View.INVISIBLE);
            }
            ImageView message = (ImageView) view.findViewById(R.id.iv_message);
            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TeachersActivity.this, TeacherMessageActivty.class);
                    intent.putExtra("teacher", teacher);
                    startActivity(intent);
                }
            });
            LinearLayout line = (LinearLayout) view.findViewById(R.id.layout_line);
            if (i + 1 == count) {
                line.setVisibility(View.GONE);
            }

            layoutTeachers.addView(view);
        }
    }


    private void empty() {
        progressBar.setVisibility(View.GONE);
        ivLoadFailure.setVisibility(View.GONE);
        tvLoadInfo.setText(getString(R.string.loading_empty));
    }

    private void loadFailure() {
        progressBar.setVisibility(View.GONE);
        ivLoadFailure.setVisibility(View.VISIBLE);
        tvLoadInfo.setText(getString(R.string.loading_failed));
        layoutLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                ivLoadFailure.setVisibility(View.GONE);
                tvLoadInfo.setText(getString(R.string.loading_content));
                queryTeachers();
            }
        });
    }


}
