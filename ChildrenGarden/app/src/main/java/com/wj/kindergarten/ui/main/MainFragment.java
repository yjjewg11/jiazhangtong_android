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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Group;
import com.wj.kindergarten.bean.MainItem;
import com.wj.kindergarten.bean.More;
import com.wj.kindergarten.bean.MoreData;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.AppraiseTeacherActivity;
import com.wj.kindergarten.ui.func.ArticleListActivity;
import com.wj.kindergarten.ui.func.CourseListActivity;
import com.wj.kindergarten.ui.func.FoodListActivity;
import com.wj.kindergarten.ui.func.InteractionListActivity;
import com.wj.kindergarten.ui.func.NoticeListActivity;
import com.wj.kindergarten.ui.func.SignListActivity;
import com.wj.kindergarten.ui.more.MoreUtil;
import com.wj.kindergarten.ui.webview.SchoolIntroduceActivity;
import com.wj.kindergarten.ui.webview.WebviewActivity;
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
    private GridView mainGv = null;

    private Context mContext = null;
    private List<MainItem> mainItems = new ArrayList();
    private GridViewAdapter mainGridAdapter = null;

    private Group chooseTile = null;
    private ArrayList<Group> titles = new ArrayList<Group>();

    private LinearLayout layoutT = null;
    private LinearLayout layoutTitles = null;
    private int height = 0;
    private boolean isShow = false;

    private ArrayList<More> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != chooseTile) {
            ((MainActivity) getActivity()).setTitleText(chooseTile.getBrand_name());
        } else {
            ((MainActivity) getActivity()).setTitleText("首页");
        }
        ((MainActivity) getActivity()).showCenterIcon(BaseActivity.TITLE_CENTER_TYPE_RIGHT, R.drawable.title_down);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, null, false);
            initViews(rootView);
            success();
            queryMore();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    private void queryMore() {
        UserRequest.queryMore(getActivity(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                MoreData data = (MoreData) domain;
                if (null != data) {
                    list.clear();
                    list.addAll(data.getList());
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(CGApplication.getInstance(), message);
                }
            }
        });
    }

    public void loadData() {
        if (titles.size() == 0) {
            success();
        }
    }

    //获取title列表
    private void success() {
        if (CGApplication.getInstance().getLogin() == null) {
            return;
        }
        titles.clear();
        titles.addAll(CGApplication.getInstance().getLogin().getGroup_list());
        String uuid = CGSharedPreference.getTitleUUID();
        chooseTile = getTitleModel(uuid);
        if (null == chooseTile && titles.size() > 0) {
            chooseTile = titles.get(0);
            CGSharedPreference.saveTitle(chooseTile);
        }

        if (null != chooseTile) {
            ((MainActivity) getActivity()).setTitleText(chooseTile.getBrand_name());
        }

        if (titles.size() > 1) {
            ((MainActivity) getActivity()).showCenterIcon(BaseActivity.TITLE_CENTER_TYPE_RIGHT, R.drawable.title_down);
        } else {
            ((MainActivity) getActivity()).showCenterIcon(BaseActivity.TITLE_CENTER_TYPE_RIGHT, null);
        }

        initTitleNames();
    }

    private void initTitleNames() {
        for (int i = 0; i < titles.size(); i++) {
            final Group t = titles.get(i);
            if (null != t) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_title_item, null);
                TextView name = (TextView) view.findViewById(R.id.tv_title_name);
                name.setText(Utils.getText(t.getBrand_name()));
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_title_name);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseTile = t;
                        CGSharedPreference.saveTitle(t);
                        ((MainActivity) getActivity()).setTitleText(chooseTile.getBrand_name());
                        hideLayout();
                    }
                });
                LinearLayout line = (LinearLayout) view.findViewById(R.id.line1);
                if (i + 1 == titles.size()) {
                    line.setVisibility(View.GONE);
                } else {
                    line.setVisibility(View.VISIBLE);
                }
                layoutTitles.addView(view);
            }
        }

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        layoutTitles.measure(w, h);
        height = layoutTitles.getMeasuredHeight();
        ViewGroup.LayoutParams layoutParamsChild = layoutTitles.getLayoutParams();
        layoutParamsChild.height = 0;
        layoutTitles.setLayoutParams(layoutParamsChild);
    }

    private Group getTitleModel(String uuid) {
        for (Group m : titles) {
            if (null != m && uuid.equals(m.getUuid())) {
                return m;
            }
        }
        return null;
    }

    public void changeTitle() {
        if (null != titles && titles.size() > 0) {
            if (!isShow) {
                isShow = true;
                ((MainActivity) getActivity()).showCenterIcon(BaseActivity.TITLE_CENTER_TYPE_RIGHT, R.drawable.title_up);
                layoutT.setVisibility(View.VISIBLE);
                Utils.showLayout(layoutTitles, 0, height, 300);
            } else {
                hideLayout();
            }
        }
    }

    private void hideLayout() {
        isShow = false;
        ((MainActivity) getActivity()).showCenterIcon(BaseActivity.TITLE_CENTER_TYPE_RIGHT, R.drawable.title_down);
        Utils.showLayout(layoutTitles, height, 0, 300, layoutT);
    }

    private void initViews(View rootView) {
        mainGv = (GridView) rootView.findViewById(R.id.main_grid);
        layoutT = (LinearLayout) rootView.findViewById(R.id.list_title_layout);
        layoutTitles = (LinearLayout) rootView.findViewById(R.id.layout_titles);
        layoutT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLayout();
            }
        });


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

        MainItem gardenInteraction = new MainItem(R.drawable.main_hudong, "互动", Constants.GARDEN_INTERACTION);
        MainItem gardenDes = new MainItem(R.drawable.main_item_xiaoyuan, "校园相关", Constants.GARDEN_DES);
        MainItem gardenNotice = new MainItem(R.drawable.main_item_gonggao, "公告", Constants.GARDEN_NOTICE);
        MainItem gardenSign = new MainItem(R.drawable.main_item_qiandao, "签到记录", Constants.GARDEN_SIGN);
        MainItem gardenCourse = new MainItem(R.drawable.main_item_kebiao, "课程表", Constants.GARDEN_COURSE);
        MainItem gardenFoods = new MainItem(R.drawable.main_item_shipu, "食谱", Constants.GARDEN_FOODS);
        MainItem gardenArticle = new MainItem(R.drawable.main_item_jingpin, "精品文章", Constants.GARDEN_ARTICLE);
        MainItem gardenSpecial = new MainItem(R.drawable.main_item_techang, "特长课程", Constants.GARDEN_SPECIAL);
        MainItem gardenTeacher = new MainItem(R.drawable.main_item_pinjia, "评价老师", Constants.GARDEN_TEACHER);
        MainItem gardenMore = new MainItem(R.drawable.main_more_1, "更多", Constants.GARDEN_MORE);

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
        //  Utils.showToast(mContext, mainItem.getText());
        switch (mainItem.getTag()) {
            case Constants.GARDEN_INTERACTION://互动
                startActivity(new Intent(mContext, InteractionListActivity.class));
                break;
            case Constants.GARDEN_DES://校园相关
                if (null != chooseTile) {
                    Intent intent = new Intent(getActivity(), SchoolIntroduceActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("uuid", chooseTile.getUuid());
                    startActivity(intent);
                } else {
                    Utils.showToast(getActivity(), "请选择幼儿园");
                }
                break;
            case Constants.GARDEN_NOTICE://公告
                startActivity(new Intent(mContext, NoticeListActivity.class));
                break;
            case Constants.GARDEN_SIGN://签到记录
                startActivity(new Intent(mContext, SignListActivity.class));
                break;
            case Constants.GARDEN_COURSE://课程表
                startActivity(new Intent(mContext, CourseListActivity.class));
                break;
            case Constants.GARDEN_FOODS://食谱
                startActivity(new Intent(mContext, FoodListActivity.class));
                break;
            case Constants.GARDEN_ARTICLE://精品文章
                startActivity(new Intent(mContext, ArticleListActivity.class));
                break;
            case Constants.GARDEN_SPECIAL:
                Intent intent1 = new Intent(getActivity(), WebviewActivity.class);
                intent1.putExtra("title", "特长课程");
                intent1.putExtra("url", "http://jz.wenjienet.com/px-mobile/px/index.html");
                getActivity().startActivity(intent1);
                break;
            case Constants.GARDEN_TEACHER://评价老师
                startActivity(new Intent(mContext, AppraiseTeacherActivity.class));
                break;
            case Constants.GARDEN_MORE://更多
                if (list.size() > 0) {
                    MoreUtil.more(getActivity(), list, mainGv);
                }
                break;
            default:
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


    public Group getChooseTile() {
        return chooseTile;
    }
}
