package com.wj.kindergarten.ui.specialcourse;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.utils.ToastUtils;

public class MineDiscussFragment extends Fragment{

    View view = null;
    private RatingBarView[] ratingBars;
    private Button bt;
    private EditText et;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_mine_discuss,null);
            ratingBars = new RatingBarView[]{
                    (RatingBarView) view.findViewById(R.id.fragment_course_rating_bar),
                    (RatingBarView) view.findViewById(R.id.fragment_school_rating_bar),
                    (RatingBarView) view.findViewById(R.id.fragment_teacher_rating_bar)
            };
            et = (EditText)view.findViewById(R.id.fragment_mine_discuss_edit_text);

            //需要确认是否已经评价

            //如果没有评价
            for(RatingBarView rbv :ratingBars){

                rbv.setClickable(true);
            }
            bt = (Button)view.findViewById(R.id.discuss_submit);
            bt.setClickable(true);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //发送评价
                    String text = et.getText().toString();
                    if(TextUtils.isEmpty(text)){
                        ToastUtils.showMessage("消息可以为空");
                    }
                    
                }
            });
        }
        return view;
    }
}
