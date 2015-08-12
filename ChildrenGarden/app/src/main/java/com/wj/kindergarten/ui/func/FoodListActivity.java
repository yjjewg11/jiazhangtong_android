package com.wj.kindergarten.ui.func;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Food;
import com.wj.kindergarten.bean.FoodList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * FoodListActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/2 21:03
 */
public class FoodListActivity extends BaseActivity {
    private List<Food> foods = new ArrayList<>();

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_food_list;
    }

    @Override
    protected void setNeedLoading() {
        isNeedLoading = true;
    }

    @Override
    protected void loadData() {
        setTitleText("每日食谱");

        getFoodList();
    }

    @Override
    protected void onCreate() {
        setTitleText("每日食谱");

    }

    private void getFoodList() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(calendar.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
//        String date = mYear + "-" + mMonth + "-" + mDay;
        String date = "2015-8-1";//TODO  test data
        String uuid = "";
        if (CGApplication.getInstance().getLogin() != null && CGApplication.getInstance().getLogin().getList() != null
                && CGApplication.getInstance().getLogin().getList().size() > 0) {
            uuid = CGApplication.getInstance().getLogin().getList().get(0).getGroupuuid();
        }
        UserRequest.getFoodList(mContext, date, date, uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                FoodList foodList = (FoodList) domain;
                if (foodList != null && foodList.getList() != null && foodList.getList().size() > 0) {
                    foods.addAll(foodList.getList());
                    loadSuc();
                } else {
                    loadEmpty();
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                loadFailed();
            }
        });
    }
}
