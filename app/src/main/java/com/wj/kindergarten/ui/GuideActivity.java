package com.wj.kindergarten.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.imagescan.HackyViewPager;
import com.wj.kindergarten.ui.viewpager.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity {

    private HackyViewPager mPager;
    private ArrayList<View> viewList;
    int [] guide = {R.drawable.page1,R.drawable.ppage2,R.drawable.page3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initList();
        mPager = (HackyViewPager) findViewById(R.id.guide_pager);
        mPager.setAdapter(new ViewPagerAdapter(viewList));
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
                        startActivity(new Intent(GuideActivity.this,SplashActivity.class));
                        finish();
                    }
                });
                viewList.add(view);
            }else {
                viewList.add(getView(guide[i]));
            }

        }

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
