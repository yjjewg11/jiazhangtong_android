package com.wj.kindergarten.ui.recuitstudents.fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.GetAssessState;
import com.wj.kindergarten.bean.GetAssessStateList;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.MineSchoolActivity;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.List;
import java.util.logging.LogRecord;

/**
 * Created by tangt on 2015/12/2.
 */
public class AssessSchoolFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView iv_school_head;
    private RatingBarView mime_school_rating_bar;
    private ImageView iv_niming;
    private EditText et_mine_school;
    private Button tv_mine_submit;
    private TextView tv_other_name;
    private MineSchoolActivity activity;
    private GetAssessState schoolState;
    private static final int HAVE_ASSESS = 50;
    private static final int NO_ASSESS  = 51;
    private HintInfoDialog dialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HAVE_ASSESS:
                    float star = Float.valueOf(schoolState.getScore());
                    et_mine_school.setText(schoolState.getContent());
                    modifaFiled(new ModificationFiled(star,false,schoolState.getContent(), R.color.white,"已评价(点击修改)",R.drawable.submit_mine_white,R.color.gray_red,null,"匿名评论"));
                    break;
                case NO_ASSESS:

                    break;
            }
        }
    };



    int niming = 1;
    private boolean isFirst;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) return view;
        activity = (MineSchoolActivity) getActivity();
        dialog  = new HintInfoDialog(getActivity());
        view = inflater.inflate(R.layout.assess_school_fragment, null);
        iv_school_head = (ImageView) view.findViewById(R.id.iv_school_head);
        mime_school_rating_bar = (RatingBarView) view.findViewById(R.id.mime_school_rating_bar);
        mime_school_rating_bar.setmClickable(true);
        mime_school_rating_bar.setFloatStar(50,true);
        et_mine_school = (EditText) view.findViewById(R.id.et_mine_school);
        iv_niming = (ImageView) view.findViewById(R.id.iv_niming);
        tv_other_name = (TextView) view.findViewById(R.id.tv_other_name);
        tv_mine_submit = (Button) view.findViewById(R.id.tv_mine_submit);
        Paint paint = new Paint();
        paint.setShadowLayer(10, 0, 10, Color.parseColor("#e5e5e5"));
        tv_mine_submit.setLayerPaint(paint);
        tv_mine_submit.setOnClickListener(this);
        iv_niming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(niming == 1){
                    niming = 0;
                    iv_niming.setImageResource(R.drawable.nimingwhite);
                    tv_other_name.setTextColor(Color.parseColor("#8e8e8e"));
                }else{
                    niming = 1;
                    iv_niming.setImageResource(R.drawable.niming1red);
                    tv_other_name.setTextColor(Color.parseColor("#ff4966"));
                }

            }
        });

        return view;
    }

    public void queryAssess() {
        dialog.show();
        UserRequest.getSchoolAssessState(getActivity(), activity.getGroupuuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if(dialog.isShowing()){dialog.cancel();}
                GetAssessStateList gsl = (GetAssessStateList) domain;
                if (gsl != null && gsl.getList() != null && gsl.getList().getData() != null
                        && gsl.getList().getData().size() > 0) {
                    schoolState = gsl.getList().getData().get(0);
                    CGSharedPreference.setMineSchoolAssessState(schoolState.getUuid());
                    handler.sendEmptyMessage(HAVE_ASSESS);
                }else{
                    handler.sendEmptyMessage(NO_ASSESS);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //首次评价,保存评价
            case R.id.tv_mine_submit:
                if (tv_mine_submit.getText().toString().equals("提交")) {
                    sendAssess();
                } else if (tv_mine_submit.getText().toString().equals("已评价(点击修改)")) {
                    //修改评价
                    tv_mine_submit.setText("修改评价");
                    return;
                } else if (tv_mine_submit.getText().toString().equals("修改评价")) {
                    modifaFiled(new ModificationFiled(mime_school_rating_bar.getClickedCount(), true, schoolState.getContent(),
                            R.color.gray_red, "修改评价", R.drawable.submit_mine, R.color.gray_red,getResources().getDrawable(R.drawable.edit_mine_school),"匿名评论"));
                    tv_mine_submit.setText("提交");
                    return ;

                }
        }
    }

    private void sendAssess() {
        dialog.show();
        UserRequest.sendSpecialCourseAssess(getActivity(), CGSharedPreference.getMineSchoolAssessState(), activity.getGroupuuid(), "", 4, mime_school_rating_bar.getClickedCount(),
                et_mine_school.getText().toString(),0, new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        if(dialog.isShowing()){dialog.cancel();}
                        String text = null;
                        String content = null;
                        if(niming == 1){
                            text = "匿名";
                        }else{
                            text = "匿名评论";
                        }
//                        if(!isFirst){
//                            isFirst = true;
//                            et_mine_school.setText(schoolState.getContent());
//                        }
                        modifaFiled(new ModificationFiled(mime_school_rating_bar.getClickedCount(), false, schoolState.getContent(),
                                R.color.white, "已评价(点击修改)", R.drawable.submit_mine_white,R.color.white,null,text));
                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                    @Override
                    public void failure(String message) {

                    }
                });
    }

    private void modifaFiled(ModificationFiled modificationFiled) {
        mime_school_rating_bar.setFloatStar(modificationFiled.getStar(), true);
        mime_school_rating_bar.setmClickable(modificationFiled.isClicked);
        et_mine_school.setClickable(modificationFiled.isClicked);
        et_mine_school.setFocusable(false);
        et_mine_school.setFocusableInTouchMode(modificationFiled.isClicked);
//        tv_other_name.setTextColor(modificationFiled.getTextColor());
        tv_mine_submit.setTextColor(modificationFiled.getTextColor());
        tv_mine_submit.setText(modificationFiled.getTv_submit());
        tv_mine_submit.setBackgroundResource(modificationFiled.getSubmit_drawable());
        et_mine_school.setBackground(modificationFiled.getEt_backgroud());
        tv_other_name.setText(modificationFiled.getNiming_name());
    }

    class ModificationFiled {
        float star;
        boolean isClicked;
        String etText;
        int textColor;
        String tv_submit;
        int submit_drawable;
        int niming_color;
        Drawable et_backgroud;
        String niming_name;


        public ModificationFiled(float star, boolean isClicked, String etText, int textColor, String tv_submit, int submit_drawable, int niming_color, Drawable et_backgroud, String niming_name) {
            this.star = star;
            this.isClicked = isClicked;
            this.etText = etText;
            this.textColor = textColor;
            this.tv_submit = tv_submit;
            this.submit_drawable = submit_drawable;
            this.niming_color = niming_color;
            this.et_backgroud = et_backgroud;
            this.niming_name = niming_name;
        }

        public String getNiming_name() {
            return niming_name;
        }

        public void setNiming_name(String niming_name) {
            this.niming_name = niming_name;
        }

        public Drawable getEt_backgroud() {
            return et_backgroud;
        }

        public void setEt_backgroud(Drawable et_backgroud) {
            this.et_backgroud = et_backgroud;
        }

        public float getStar() {
            return star;
        }

        public void setStar(float star) {
            this.star = star;
        }

        public boolean isClicked() {
            return isClicked;
        }

        public void setIsClicked(boolean isClicked) {
            this.isClicked = isClicked;
        }

        public String getEtText() {
            return etText;
        }

        public void setEtText(String etText) {
            this.etText = etText;
        }


        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public String getTv_submit() {
            return tv_submit;
        }

        public void setTv_submit(String tv_submit) {
            this.tv_submit = tv_submit;
        }

        public int getSubmit_drawable() {
            return submit_drawable;
        }

        public void setSubmit_drawable(int submit_drawable) {
            this.submit_drawable = submit_drawable;
        }

        public int getNiming_color() {
            return niming_color;
        }

        public void setNiming_color(int niming_color) {
            this.niming_color = niming_color;
        }
    }
}