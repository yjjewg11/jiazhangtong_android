package com.wj.kindergarten.ui.specialcourse;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllTeacher;
import com.wj.kindergarten.bean.AllTeacherList;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.GetAssessState;
import com.wj.kindergarten.bean.GetAssessStateList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.MineCourseDetailActivity;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MineDiscussFragment extends Fragment{


    private List<AllTeacher> teacher_list = new ArrayList<>();
    View view = null;
    private RatingBarView[] ratingBars;
    private List<GetAssessState> stateList = new ArrayList<>();
    private static final int [] arrays = new int[]{81,82,83};
    private Button bt;
    private EditText et;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    count++;
                    if(count==3){
                        et.setFocusable(false);
                        et.setFocusableInTouchMode(false);
                        bt.setClickable(false);
                        bt.setBackgroundResource(R.color.line);
                        for(RatingBarView rb : ratingBars){
                            rb.setmClickable(false);
                        }
                    }
                    break;

                case 100:
                    if(msg.arg1 == 1){
                        success();
                        et.setVisibility(View.GONE);
                    }else{
                        noAssess();
                        input_content.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };
    private TextView input_content;

    private void noAssess() {
        for(RatingBarView rbv :ratingBars){

            rbv.setmClickable(true);
        }

        bt.setClickable(true);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送评价
                String text = et.getText().toString();
                if(TextUtils.isEmpty(text)){
                    ToastUtils.showMessage("消息可以为空");
                }
                doAfter(text);
            }
        });
    }

    private void success() {
            for(int j = 0;j<stateList.size();j++){
                GetAssessState gs = stateList.get(j);
                if(gs.getType() == 81){
                    ratingBars[0].setStar(Utils.getIntegerFromString(gs.getScore()));
                }else if(gs.getType() == 82){
                    ratingBars[1].setStar(Utils.getIntegerFromString(gs.getScore()));
                }else {
                    //按顺序取出老师集合的所有老师与请求评价成功的老师集合的uuid作比较,如何相等，追加显示
                    String teacherUuid = teacher_list.get(j-2).getUuid();
                    if(stateList.get(j).getExt_uuid().equals(teacherUuid)){
                        View view = teacher_linear.getChildAt(j - 2);
                        RatingBarView barView = (RatingBarView) view.findViewById(R.id.fragment_teacher_rating_bar);
                        barView.setStar(Utils.getIntegerFromString(stateList.get(j).getScore()));
                    }
                }

                if(!TextUtils.isEmpty(input_content.getText().toString())){
                    input_content.append(stateList.get(j).getContent());
                }else{
                    input_content.setText(""+stateList.get(j).getContent());
                }
            }
    }

    private int count = 0;
    boolean isFirst ;
    private LinearLayout teacher_linear;
    private MineCourseDetailActivity mcd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //请求老师列表
        if(view != null) return view;
            mcd = (MineCourseDetailActivity) getActivity();
            requestTeacher();
            getAssess();
            view = inflater.inflate(R.layout.fragment_mine_discuss,null);
            ratingBars = new RatingBarView[]{
                    (RatingBarView) view.findViewById(R.id.fragment_course_rating_bar),
                    (RatingBarView) view.findViewById(R.id.fragment_school_rating_bar),
            };
            et = (EditText)view.findViewById(R.id.fragment_mine_discuss_edit_text);
            teacher_linear = (LinearLayout) view.findViewById(R.id.all_teacher_linear);
            bt = (Button)view.findViewById(R.id.discuss_submit);
            input_content = (TextView)view.findViewById(R.id.input_content);
            //需要确认是否已经评价

        //获取是否有评价

            //如果没有评价

        return view;
    }

    private void getAssess() {
        UserRequest.getAssessState(getActivity(),mcd.getSso().getCourseuuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                Message message = new Message();
                GetAssessStateList gas = (GetAssessStateList) domain;
                if(gas!=null & gas.getList()!=null){
                    stateList.addAll(gas.getList().getData());
                    message.arg1 = 1;
                }else{
                    message.arg1 = 0;
                }
                message.what = 100;

                mHandler.sendMessage(message);

            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void requestTeacher() {

        UserRequest.getAllAssessTeacher(getActivity(),mcd.getSso().getCourseuuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {

                AllTeacherList atl = (AllTeacherList) domain;
                if(atl!=null & atl.getList()!=null){
                    teacher_list.addAll(atl.getList());
                    for(AllTeacher at : teacher_list){
                        View rlView = View.inflate(getActivity(),R.layout.teacher_assess_item,null);
                        TextView tv = (TextView) rlView.findViewById(R.id.signal_teacher);
                        teacher_linear.addView(rlView);
                    }
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

    private void doAfter(String content) {

        //TODO
        int type = 0;
        int score = 0;

        for(int  count= 0;count<(2+teacher_list.size());count++){
            String extend_uuid = null;
        if(count == 0){
            extend_uuid = mcd.getSso().getGroupuuid();
            type = 81;
            score = ratingBars[0].getClickedCount();
        }else if(count == 1){
            extend_uuid = mcd.getSso().getCourseuuid();
            type = 82;
            score = ratingBars[1].getClickedCount();
        }else{
            extend_uuid = teacher_list.get(count).getUuid();
            type = 83;
            View view = teacher_linear.getChildAt(count - 2);
            RatingBarView barView = (RatingBarView) view.findViewById(R.id.fragment_teacher_rating_bar);
            score = barView.getClickedCount();
        }
        if(count != 0) {content = null;}
        UserRequest.sendSpecialCourseAssess(getActivity(),extend_uuid,mcd.getSso().getUuid(),type,score,content,new RequestResultI(){
            @Override
            public void result(BaseModel domain) {
                    mHandler.sendEmptyMessage(1);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });

        }

    }
}
