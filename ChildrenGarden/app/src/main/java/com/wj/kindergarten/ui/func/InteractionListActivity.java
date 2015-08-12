package com.wj.kindergarten.ui.func;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;

/**
 * InteractionListActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/28 23:39
 */
public class InteractionListActivity extends BaseActivity {

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_interaction_list;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("互动", R.drawable.interaction_send);
        InteractionFragment interactionFragment = new InteractionFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.interaciton_list_content, interactionFragment).commit();
    }
}
