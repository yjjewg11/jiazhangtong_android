package com.wj.kindergarten.ui.more;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

/**
 * Created by tangt on 2016/1/28.
 */
public class BanScrollView extends PullToRefreshScrollView {
    private boolean canScroll = false;

    public BanScrollView(Context context) {
        super(context);
    }

    public BanScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!canScroll()) {
            if (ev.getAction() == MotionEvent.ACTION_MOVE
                    || ev.getAction() == MotionEvent.ACTION_UP) {
                return true;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    public boolean canScroll() {
        return canScroll;
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }


}
