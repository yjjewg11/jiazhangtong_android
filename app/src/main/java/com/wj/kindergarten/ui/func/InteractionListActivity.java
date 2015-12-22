package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.view.MotionEvent;


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
    private InteractionFragment interactionFragment = null;
    public static InteractionListActivity instance;


    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_interaction_list;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void titleLeftButtonListener() {
        super.titleLeftButtonListener();
        hideSoftKeyBoard(interactionFragment.getEmot2(),interactionFragment.getBottomLayou());
    }
    
    @Override
    protected void onCreate() {
        instance = this;
        setTitleText("班级互动", R.drawable.interaction_send);
        interactionFragment = new InteractionFragment();
        interactionFragment.setNewsuuid(getIntent().getStringExtra("newsuuid"));
        getSupportFragmentManager().beginTransaction().add(R.id.interaciton_list_content, interactionFragment).commit();
    }

    @Override
    protected void titleRightButtonListener() {
        startActivityForResult(new Intent(mContext, InteractionSentActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                interactionFragment.refreshList();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

}
