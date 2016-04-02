package com.wj.kindergarten.ui.addressbook;

import com.wj.kindergarten.bean.MyMessage;

import java.util.Comparator;

/**
 * Created by zoupengqiang on 14-5-27.
 *
 */
public class CompareMessage implements Comparator {

    @Override
    public int compare(Object o, Object o2) {
        MyMessage m1 = (MyMessage) o;
        MyMessage m2 = (MyMessage) o2;
        return m1.getCreate_time().compareTo(m2.getCreate_time());
    }
}
