package com.wj.kindergarten.ui.recuitstudents.fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.other.RatingBarView;

/**
 * Created by tangt on 2015/12/2.
 */
public class AssessSchoolFragment extends Fragment {
    private View view;
    private ImageView iv_school_head;
    private RatingBarView mime_school_rating_bar;
    private ImageView iv_niming;
    private EditText et_mine_school;
    private Button tv_mine_submit;
    private TextView tv_other_name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) return view;
        view = inflater.inflate(R.layout.assess_school_fragment,null);
        iv_school_head = (ImageView)view.findViewById(R.id.iv_school_head);
        mime_school_rating_bar = (RatingBarView)view.findViewById(R.id.mime_school_rating_bar);
        mime_school_rating_bar.setmClickable(true);
        et_mine_school = (EditText)view.findViewById(R.id.et_mine_school);
        iv_niming = (ImageView)view.findViewById(R.id.iv_niming);
        tv_other_name = (TextView)view.findViewById(R.id.tv_other_name);
        tv_mine_submit = (Button)view.findViewById(R.id.tv_mine_submit);
        Paint paint = new Paint();
        paint.setShadowLayer(10,0,10, Color.parseColor("#e5e5e5"));
        tv_mine_submit.setLayerPaint(paint);

        return view;
    }
}
