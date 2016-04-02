package com.wj.kindergarten.ui.other;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2015/11/13.
 */
public class SlowyScrollView extends ScrollView {
	public SlowyScrollView(Context context) {
		super(context);
	}

	public SlowyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	@Override
	public void fling(int velocityY) {
		super.fling(velocityY/4);
	}
}
