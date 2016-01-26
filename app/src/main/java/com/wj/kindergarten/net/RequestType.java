package com.wj.kindergarten.net;

import android.content.Context;

/**
 * RequestType
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/5/21 17:00
 */
public interface RequestType {

     int LOGIN = 100;
     int REGISTER = 101;
     int FORGET_PWD = 102;
     int CHANGE_CHILD = 103;
     int SMS_CODE = 104;
     int INTERACTION_LIST = 105;
     int ZAN = 106;
     int INTERACTION_SEND = 107;
     int ZAN_LIST = 108;
     int REPLY_LIST = 109;
     int REPLY = 110;
     int NOTICE_LIST = 111;
     int NOTICE = 112;
     int SIGN = 113;
     int COURSE_LIST = 114;
     int FOOD_LIST = 115;
     int ARTICLE_LIST = 116;
     int ARTICLE = 117;
     int APPRAISE_TEACHER_LIST = 118;
     int APPRAISE_TEACHER = 119;
     int TEACHERS = 120;//通讯录获得园长和老师列表
     int GET_LEADER_MESSAGE = 121;//获得园长和家长的消息列表
     int GET_EMOT = 122;//获得表情符号列表
     int SEND_MESSAGE_TO_LEADER = 124;//给园长发消息
     int UPDATE_PASSWORD = 125;//修改密码
     int GET_TEACHER_MESSAGE = 126;//获得老师和家长的消息列表
     int SEND_MESSAGE_TO_TEACHER = 127;//给老师发消息
     int ZAN_CANCEL = 128;
     int DEVICE_SAVE = 129;
     int QUERY_MESSAGE = 130;//消息
     int QUERY_SCHOOL = 131;//首页title
     int QUERY_MORE = 132;//首页more
     int QUERY_STORE = 133;//收藏列表
     int STORE = 134;//收藏
     int CANCEL_STORE = 135;//取消收藏
     int QUERY_TEACHER_INFO = 136;//获取老师的信息
     int LOGIN_OUT = 137;//退出
     int KA_INFO = 138;//学生卡号信息
     int READ_MESSAGE = 139;//消息阅读
     int TRAIN_COURSE_LIST = 140;

     int TRAIN_CHLID_INFO = 141;
     int TRAIN_CLASS= 142;

    //培训机构
     int SPECIAL_COURSE_TYPE = 143;
     int SPECIAL_COURSE_INFO = 144;
     int  ONCE_COURSE_CLICK  = 145;
     int ALL_TRAINC_SCHOOL = 146;
     int MORE_DISCUSS_FROM_UUID = 147;
     int NEXT_CLASS_INFO = 148;
     int TEACHER_COUNT = 149;
     int STUDY_STATE = 150;
     int MINE_ALL_COURSE = 151;
     int TRAIN_SCHOOL_DETAIL = 152;
     int ALL_TEACHER = 153;
     int GET_ASSESS_STATE = 154;
     int TEACHER_DETAIL_INFO = 155;
     int GET_PRIVELEGE_ACTIVE = 156;
     int GET_USER_INFO = 157;//获取用户信息
     int GET_TOPIC_CONFIG = 158;
     int GET_MAIN_TOPIC = 159;
     int FOUND_TYPE_COUNT = 160;
     int FOUND_HOT_SELECTION = 161;
     int GET_INTERACTION_LINK = 162;
     int GET_PF_ALBUM_LIST = 163;
     int LOOK_FOR_ALL_PF = 164;
     int PF_PIC_BY_UUID = 165;
     int CHECK_PF_IS_CHANGE = 166;
     int PF_OBJ_BY_UPDATE = 167;
     int GET_SINGLE_PF_INFO = 168;
}
