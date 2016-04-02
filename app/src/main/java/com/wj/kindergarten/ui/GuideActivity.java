package com.wj.kindergarten.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.ui.imagescan.HackyViewPager;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.more.MyCircleView;
import com.wj.kindergarten.ui.viewpager.ViewPagerAdapter;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;

public class GuideActivity extends Activity {

    private HackyViewPager mPager;
    private ArrayList<View> viewList;
    int [] guide = {R.drawable.android_yindaoye1,R.drawable.android_yindaoye2,R.drawable.android_yindaoye3};
    private ImageView activity_guide_tiaoguo;
    private MyCircleView activity_guide_mycircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断是否显示,如果登录过，则直接跳入主页
        if(CGSharedPreference.getLoginOnce()){
            startAnother(0);
            return;
        }
        setContentView(R.layout.activity_guide);
        initList();
        mPager = (HackyViewPager) findViewById(R.id.guide_pager);
        mPager.setAdapter(new ViewPagerAdapter(viewList));
        activity_guide_tiaoguo = (ImageView) findViewById(R.id.activity_guide_tiaoguo);
        activity_guide_mycircle = (MyCircleView) findViewById(R.id.activity_guide_mycircle);
        activity_guide_tiaoguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnother(0, "later");
            }
        });
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                activity_guide_mycircle.setScale(position+positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initList() {
        viewList = new ArrayList<>();
        for(int i = 0 ; i< 3 ; i++){
            if(i == 2){
                View view = View.inflate(this,R.layout.guide_page_three,null);
                ImageButton image_bt_tiyan = (ImageButton) view.findViewById(R.id.image_bt_tiyan);
                image_bt_tiyan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startAnother(0,"now");
                    }
                });
                viewList.add(view);
            }else {
                viewList.add(getView(guide[i]));
            }

        }

    }

    private void startAnother(int seconds, String now) {
        Intent intent = new Intent(GuideActivity.this,MainActivity.class);
        if(!Utils.stringIsNull(getIntent().getStringExtra("from"))){
            intent.putExtra("from","splash");
        }
        intent.putExtra("tiaozhuan",now);
        startActivity(intent);
        CGSharedPreference.setLoginOnce();
        finish();
    }
    private void startAnother(int seconds) {
        startAnother(seconds,"");
    }


    private RelativeLayout getView(int drawableId){
        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setBackground(getResources().getDrawable(drawableId));
        return relativeLayout;
    }
}
