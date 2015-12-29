package com.wj.kindergarten.utils;

import com.wj.kindergarten.CGApplication;

/**
 * Created by Administrator on 2015/11/9.
 */
public interface GloablUtils {
    //芒果id
    String MOGO_ID = "b1be6cfbb82542c4ba00f32ef1166884";
    String FROM_COURSE_TO_MINE_COURSE = "my_course_to";
    String MINE_COURSE_FRAGMENT = "mine_course_fragment";
    String COURSE_DETAIL_INDROTUCE_FRAGMENT = "course_detail_introduce_fragment";
    String MINE_DISCUSS_FRAGMENT = "mine_discuss_fragment";
    String SPLIT_STIRNG_MOBILE_NUMBER = ",|/";
    String[] COMMON_STRING = new String[]{
            "school", "recruit", "course", "assess"
    };
    String CACHE_USER_INFO = "cacheUserInfo.txt";
    String CACHE_DATA = "cacheData";

    String MINE_ADD_CHILD_FINISH = "add_child_finish";


    String SET_PULL_LAOUT_LIST_BACKGROUND_COLOR = "#ffffff";

    //接受二维码链接的广播action
    String RECEVIER_TWO_CODE_URL = "receiver_two_code_url";


}
