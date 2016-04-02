package com.wj.kindergarten.ui.mine.store;

import android.content.Intent;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.InteractionFragment;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.Utils;

/**
 * StoreActivity
 *
 * @Description:收藏列表
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-18 10:40
 */
public class StoreActivity extends BaseActivity {
    private StoreFragment storeFragment = null;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_interaction_list;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("我的收藏");
        storeFragment = new StoreFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.interaciton_list_content, storeFragment).commit();
    }

    @Override
    protected void onResume() {
        if (!Constants.isStore) {
            storeFragment.removeItem();
        }
        super.onResume();
    }
}
