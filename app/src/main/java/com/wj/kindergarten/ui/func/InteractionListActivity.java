package com.wj.kindergarten.ui.func;

import android.content.Intent;
<<<<<<< HEAD
import android.view.MotionEvent;
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

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
<<<<<<< HEAD
    public static InteractionListActivity instance;
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_interaction_list;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
<<<<<<< HEAD
        instance = this;
        setTitleText("互动", R.drawable.interaction_send);
        interactionFragment = new InteractionFragment();
        interactionFragment.setNewsuuid(getIntent().getStringExtra("newsuuid"));
=======
        setTitleText("互动", R.drawable.interaction_send);
        interactionFragment = new InteractionFragment();
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
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
<<<<<<< HEAD

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
}
