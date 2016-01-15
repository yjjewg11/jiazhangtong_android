package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/14.
 */
public class PfGalleryActivity extends BaseActivity{
    private List<AllPfAlbumSunObject> list;
    private ViewPager viewPager;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.pf_gallery_activity;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void titleRightButtonListener() {
        Intent intent = new Intent(this,PfEditInfoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate() {

       Intent intent =   getIntent();
       list = (ArrayList) intent.getSerializableExtra("list");
       setTitleRightImage(R.drawable.modification_pf,0);
       initViews();
        changeTitle();
    }

    private void changeTitle() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initViews() {
        viewPager = (ViewPager)findViewById(R.id.pf_edit_viewPager);
        viewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = View.inflate(PfGalleryActivity.this,R.layout.pf_gallery_fragment,null);
                ImageView pf_gallery_image = (ImageView) view.findViewById(R.id.pf_gallery_image);
                TextView pf_gallery_annotation = (TextView) view.findViewById(R.id.pf_gallery_annotation);
                TextView pf_gallery_location = (TextView) view.findViewById(R.id.pf_gallery_location);
                TextView pf_gallery_people = (TextView) view.findViewById(R.id.pf_gallery_people);
                AllPfAlbumSunObject object = null;
                if(object != null){
                    ImageLoaderUtil.displayMyImage(object.getPath(),pf_gallery_image);
                    pf_gallery_annotation.setText("" + Utils.isNull(object.getNote()));
                    pf_gallery_location.setText(""+ Utils.isNull(object.getAddress()));
                    pf_gallery_people.setText(""+ Utils.isNull(object.getPhoto_time()));
                }else{
                    pf_gallery_image.setImageResource(R.drawable.load1);
                }
                container.addView(view,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return view;
            }

            @Override
            public int getCount() {
                return 30;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View)object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }
}
