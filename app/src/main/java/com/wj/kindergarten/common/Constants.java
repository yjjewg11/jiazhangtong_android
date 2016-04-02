package com.wj.kindergarten.common;

import android.os.Environment;

import java.io.File;

/**
 * Constants
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/5/20 12:42
 */
public abstract class Constants {
    public static final int MAIN_ITEM_MAX = 3;
    public static final int HTTP_TIME_OUT = 10 * 1000;

    public static final String parentPath = Environment.getExternalStorageDirectory() +
            File.separator + "CG" + File.separator;
    public static final String cachePath = parentPath + "cache";
    public static final String cameraPicName = "CGTemp.jpg";
    public static final String cameraPicPath = parentPath + cameraPicName;

    public static final String GARDEN_INTERACTION = "garden_interaction";
    public static final String GARDEN_DES = "garden_des";
    public static final String GARDEN_NOTICE = "garden_notice";
    public static final String GARDEN_SIGN = "garden_sign";
    public static final String GARDEN_COURSE = "garden_course";
    public static final String GARDEN_FOODS = "garden_foods";
    public static final String GARDEN_ARTICLE = "garden_article";
    public static final String GARDEN_SPECIAL = "garden_special";
    public static final String GARDEN_TEACHER = "garden_teacher";
    public static final String GARDEN_MORE = "garden_more";
    public static final String GARDEN_ADDRESS_LIST = "address_list";

    public static final String PRIVIAL_ACTIVE = "you_hui_active";


    public static final String ALREADY_SELECT_KEY = "select_list";
    public static final int HTTP_PIC_TIME_OUT = 120 * 1000;

    public static boolean isStore = true;
}
