package com.wj.kindergarten.ui.more;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tangt on 2016/3/14.
 */
public class CommonWrapper<T1 extends ViewGroup,T2 extends ViewGroup> {
    T1 t1;
    T2 t2;


    public CommonWrapper(T1 t1, T2 t2, String property) {
        this.t1 = t1;
        this.t2 = t2;
    }



}
