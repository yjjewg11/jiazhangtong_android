package com.wj.kindergarten.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.MainItem;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.compounets.SlideShowView;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.AppraiseTeacherActivity;
import com.wj.kindergarten.ui.func.ArticleListActivity;
import com.wj.kindergarten.ui.func.CourseListActivity;
import com.wj.kindergarten.ui.func.FoodListActivity;
import com.wj.kindergarten.ui.func.InteractionListActivity;
import com.wj.kindergarten.ui.func.NoticeListActivity;
import com.wj.kindergarten.ui.func.SignListActivity;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * MainFragment
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/17 11:34
 */
public class MainFragment extends Fragment {
    public static int GRID_ITEM_HW = 240;
    private View rootView;
    private SlideShowView adSSV = null;
    private ImageView adLeftIv = null;
    private ImageView adRightIv = null;
    private GridView mainGv = null;

    private Context mContext = null;
    private List<MainItem> mainItems = new ArrayList();
    private GridViewAdapter mainGridAdapter = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setTitleText("金太阳幼儿园");
        ((MainActivity) getActivity()).showCenterIcon(BaseActivity.TITLE_CENTER_TYPE_RIGHT, R.drawable.title_down);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, null, false);
            initViews(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    private void initViews(View rootView) {
        adSSV = (SlideShowView) rootView.findViewById(R.id.main_ad);
        adLeftIv = (ImageView) rootView.findViewById(R.id.main_ad_left);
        adRightIv = (ImageView) rootView.findViewById(R.id.main_ad_right);
        mainGv = (GridView) rootView.findViewById(R.id.main_grid);

        initMainItem();
        mainGridAdapter = new GridViewAdapter();
        mainGv.setAdapter(mainGridAdapter);
        mainGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainItemsClick(mainItems.get(position));
            }
        });
    }

    private void initMainItem() {
        mainItems.clear();

        MainItem gardenInteraction = new MainItem(R.drawable.main_item, "互动", Constants.GARDEN_INTERACTION);
        MainItem gardenDes = new MainItem(R.drawable.main_item_xiaoyuan, "园区介绍", Constants.GARDEN_DES);
        MainItem gardenNotice = new MainItem(R.drawable.main_item_gonggao, "公告", Constants.GARDEN_NOTICE);
        MainItem gardenSign = new MainItem(R.drawable.main_item_qiandao, "签到记录", Constants.GARDEN_SIGN);
        MainItem gardenCourse = new MainItem(R.drawable.main_item_kebiao, "课程表", Constants.GARDEN_COURSE);
        MainItem gardenFoods = new MainItem(R.drawable.main_item_shipu, "食谱", Constants.GARDEN_FOODS);
        MainItem gardenArticle = new MainItem(R.drawable.main_item_jingpin, "精品文章", Constants.GARDEN_ARTICLE);
        MainItem gardenSpecial = new MainItem(R.drawable.main_item_techang, "特长课程", Constants.GARDEN_SPECIAL);
        MainItem gardenTeacher = new MainItem(R.drawable.main_item_pinjia, "评价老师", Constants.GARDEN_TEACHER);
        MainItem gardenMore = new MainItem(R.drawable.main_item, "更多", Constants.GARDEN_MORE);

        mainItems.add(gardenInteraction);
        mainItems.add(gardenDes);
        mainItems.add(gardenNotice);
        mainItems.add(gardenSign);
        mainItems.add(gardenCourse);
        mainItems.add(gardenFoods);
        mainItems.add(gardenArticle);
        mainItems.add(gardenSpecial);
        mainItems.add(gardenTeacher);
        mainItems.add(gardenMore);
    }

    private void mainItemsClick(MainItem mainItem) {
        Utils.showToast(mContext, mainItem.getText());
        switch (mainItem.getTag()) {
            case Constants.GARDEN_INTERACTION:
                startActivity(new Intent(mContext, InteractionListActivity.class));
                break;
            case Constants.GARDEN_DES:

                break;
            case Constants.GARDEN_NOTICE:
                startActivity(new Intent(mContext, NoticeListActivity.class));
                break;
            case Constants.GARDEN_SIGN:
                startActivity(new Intent(mContext, SignListActivity.class));
                break;
            case Constants.GARDEN_COURSE:
                startActivity(new Intent(mContext, CourseListActivity.class));
                break;
            case Constants.GARDEN_FOODS:
                startActivity(new Intent(mContext, FoodListActivity.class));
                break;
            case Constants.GARDEN_ARTICLE:
                startActivity(new Intent(mContext, ArticleListActivity.class));
                break;
            case Constants.GARDEN_SPECIAL:

                break;
            case Constants.GARDEN_TEACHER:
                startActivity(new Intent(mContext, AppraiseTeacherActivity.class));
                break;
            case Constants.GARDEN_MORE:

                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    private class GridViewAdapter extends BaseAdapter {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mContext, R.layout.item_main_grid, null);

            TextView textView = (TextView) view.findViewById(R.id.main_item_text);
            ImageView imageView = (ImageView) view.findViewById(R.id.main_item_image);

            MainItem mainItem = mainItems.get(position);
            textView.setText(mainItem.getText());
            imageView.setImageResource(mainItem.getImageResource());

            return view; // 返回ImageView
        }

        /*
         * 功能：获得当前选项的ID
         *
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /*
         * 功能：获得当前选项
         *
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Object getItem(int position) {
            return mainItems.get(position);
        }

        /*
         * 获得数量
         *
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() {
            return mainItems.size();
        }
    }
}
