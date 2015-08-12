package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AppraiseTeacher;
import com.wj.kindergarten.bean.AppraiseTeacherOver;
import com.wj.kindergarten.ui.func.AppraiseTeacherActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AppraiseTeacherAdapter
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/5 23:19
 */
public class AppraiseTeacherAdapter extends BaseAdapter {
    private Context context;
    private List<AppraiseTeacher> teachers = new ArrayList<>();
    private Map<String, AppraiseTeacherOver> alreadyTeacherMap = new HashMap<>();

    public AppraiseTeacherAdapter(Context context) {
        this.context = context;
    }

    public List<AppraiseTeacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<AppraiseTeacher> teachers) {
        this.teachers = teachers;
    }

    public Map<String, AppraiseTeacherOver> getAlreadyTeacherMap() {
        return alreadyTeacherMap;
    }

    public void setAlreadyTeacherMap(Map<String, AppraiseTeacherOver> alreadyTeacherMap) {
        this.alreadyTeacherMap = alreadyTeacherMap;
    }

    public void setTeacherIsEditByKey(String key) {
        if (alreadyTeacherMap.containsKey(key)) {
            alreadyTeacherMap.get(key).setIsEditState(false);
        }
    }

    @Override
    public int getCount() {
        return teachers.size();
    }

    @Override
    public Object getItem(int i) {
        return teachers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View cView = View.inflate(context, R.layout.item_appraise_teacher_list, null);
        TextView nameTv = (TextView) cView.findViewById(R.id.item_appraise_name);
        final TextView goodTv = (TextView) cView.findViewById(R.id.item_appraise_good);
        final TextView normalTv = (TextView) cView.findViewById(R.id.item_appraise_normal);
        final TextView badTv = (TextView) cView.findViewById(R.id.item_appraise_bad);
        final EditText contentTv = (EditText) cView.findViewById(R.id.item_appraise_edit);
        ImageView submitIv = (ImageView) cView.findViewById(R.id.item_appraise_submit);

        final AppraiseTeacher appraiseTeacher = teachers.get(i);
        if (alreadyTeacherMap.containsKey(appraiseTeacher.getTeacher_uuid())
                && /*!*/alreadyTeacherMap.get(appraiseTeacher.getTeacher_uuid()).isEditState()) {
            contentTv.setEnabled(false);
            AppraiseTeacherOver appraiseTeacherOver = alreadyTeacherMap.get(appraiseTeacher.getTeacher_uuid());
            if (appraiseTeacherOver != null) {
                nameTv.setText(appraiseTeacher.getName());
                if ("1".equals(appraiseTeacherOver.getType())) {
                    Drawable drawable = context.getResources().getDrawable(R.drawable.appraise_good_after);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                    goodTv.setCompoundDrawables(null, drawable, null, null);
                } else if ("2".equals(appraiseTeacherOver.getType())) {
                    Drawable drawable = context.getResources().getDrawable(R.drawable.appraise_n_after);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    normalTv.setCompoundDrawables(null, drawable, null, null);
                } else if ("3".equals(appraiseTeacherOver.getType())) {
                    Drawable drawable = context.getResources().getDrawable(R.drawable.appraise_un_after);
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
                    if (context.getResources().getDrawable(R.drawable.appraise_good_after).getConstantState()
                            .equals(drawableGood.getConstantState())) {
                        type = 1;
                    } else if (context.getResources().getDrawable(R.drawable.appraise_n_after).getConstantState()
                            .equals(drawableNormal.getConstantState())) {
                        type = 2;
                    } else if (context.getResources().getDrawable(R.drawable.appraise_un_after).getConstantState()
                            .equals(drawableBad.getConstantState())) {
                        type = 3;
                    }
                    AppraiseTeacherOver appraiseTeacherOverTemp = new AppraiseTeacherOver();
                    appraiseTeacherOverTemp.setType(type + "");
                    appraiseTeacherOverTemp.setContent(contentTv.getText().toString());
                    appraiseTeacherOverTemp.setTeacheruuid(appraiseTeacher.getTeacher_uuid());
                    appraiseTeacherOverTemp.setIsEditState(true);
                    alreadyTeacherMap.put(appraiseTeacher.getTeacher_uuid(), appraiseTeacherOverTemp);
                    ((AppraiseTeacherActivity) context).appraiseTeacher(contentTv.getText().toString()
                            , appraiseTeacher.getTeacher_uuid(), type);
                }
            });
            goodTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Drawable drawable1 = context.getResources().getDrawable(R.drawable.appraise_good_after);
                    drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                    Drawable drawable2 = context.getResources().getDrawable(R.drawable.appraise_n_normal);
                    drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                    Drawable drawable3 = context.getResources().getDrawable(R.drawable.appraise_un_before);
                    drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
                    goodTv.setCompoundDrawables(null, drawable1, null, null);
                    normalTv.setCompoundDrawables(null, drawable2, null, null);
                    badTv.setCompoundDrawables(null, drawable3, null, null);
                }
            });
            normalTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Drawable drawable1 = context.getResources().getDrawable(R.drawable.appraise_good_before);
                    drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                    Drawable drawable2 = context.getResources().getDrawable(R.drawable.appraise_n_after);
                    drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                    Drawable drawable3 = context.getResources().getDrawable(R.drawable.appraise_un_before);
                    drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
                    goodTv.setCompoundDrawables(null, drawable1, null, null);
                    normalTv.setCompoundDrawables(null, drawable2, null, null);
                    badTv.setCompoundDrawables(null, drawable3, null, null);
                }
            });
            badTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Drawable drawable1 = context.getResources().getDrawable(R.drawable.appraise_good_before);
                    drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                    Drawable drawable2 = context.getResources().getDrawable(R.drawable.appraise_n_normal);
                    drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                    Drawable drawable3 = context.getResources().getDrawable(R.drawable.appraise_un_after);
                    drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
                    goodTv.setCompoundDrawables(null, drawable1, null, null);
                    normalTv.setCompoundDrawables(null, drawable2, null, null);
                    badTv.setCompoundDrawables(null, drawable3, null, null);
                }
            });
        }

        return cView;
    }
}
