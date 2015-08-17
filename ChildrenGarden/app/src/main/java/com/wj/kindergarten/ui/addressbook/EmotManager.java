package com.wj.kindergarten.ui.addressbook;

import com.wj.kindergarten.bean.Emot;

import java.util.ArrayList;

/**
 * EmotManager
 *
 * @Description:表情管理
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-12 21:42
 */
public class EmotManager {
    private static ArrayList<Emot> emots = new ArrayList<Emot>();

    public static void addEmots(ArrayList<Emot> list) {
        if (null != list && list.size() > 0) {
            emots.clear();
            emots.addAll(list);
        }
    }

    public static ArrayList<Emot> getEmots() {
        return emots;
    }
}
