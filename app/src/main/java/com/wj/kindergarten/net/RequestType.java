package com.wj.kindergarten.net;

/**
 * RequestType
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/5/21 17:00
 */
public class RequestType {

    public final static int LOGIN = 100;
    public final static int REGISTER = 101;
    public final static int FORGET_PWD = 102;
    public final static int CHANGE_CHILD = 103;
    public final static int SMS_CODE = 104;
    public final static int INTERACTION_LIST = 105;
    public final static int ZAN = 106;
    public final static int INTERACTION_SEND = 107;
    public final static int ZAN_LIST = 108;
    public final static int REPLY_LIST = 109;
    public final static int REPLY = 110;
    public final static int NOTICE_LIST = 111;
    public final static int NOTICE = 112;
    public final static int SIGN = 113;
    public final static int COURSE_LIST = 114;
    public final static int FOOD_LIST = 115;
    public final static int ARTICLE_LIST = 116;
    public final static int ARTICLE = 117;
    public final static int APPRAISE_TEACHER_LIST = 118;
    public final static int APPRAISE_TEACHER = 119;
    public final static int TEACHERS = 120;//通讯录获得园长和老师列表
    public final static int GET_LEADER_MESSAGE = 121;//获得园长和家长的消息列表
    public final static int GET_EMOT = 122;//获得表情符号列表
    public final static int SEND_MESSAGE_TO_LEADER = 124;//给园长发消息
    public final static int UPDATE_PASSWORD = 125;//修改密码
    public final static int GET_TEACHER_MESSAGE = 126;//获得老师和家长的消息列表
    public final static int SEND_MESSAGE_TO_TEACHER = 127;//给老师发消息
    public final static int ZAN_CANCEL = 128;
    public final static int DEVICE_SAVE = 129;
    public final static int QUERY_MESSAGE = 130;//消息
    public final static int QUERY_SCHOOL = 131;//首页title
    public final static int QUERY_MORE = 132;//首页more
    public final static int QUERY_STORE = 133;//收藏列表
    public final static int STORE = 134;//收藏
    public final static int CANCEL_STORE = 135;//取消收藏
    public final static int QUERY_TEACHER_INFO = 136;//获取老师的信息
    public final static int LOGIN_OUT = 137;//退出
    public final static int KA_INFO = 138;//学生卡号信息
    public final static int READ_MESSAGE = 139;//消息阅读
    public final static int TRAIN_COURSE_LIST = 140;

    public final static int TRAIN_CHLID_INFO = 141;
    public final static int TRAIN_CLASS= 142;

    //培训机构
    public static final int SPECIAL_COURSE_TYPE = 143;
    public static final int SPECIAL_COURSE_INFO = 144;
    public static final int  ONCE_COURSE_CLICK  = 145;
    public static final int ALL_TRAINC_SCHOOL = 146;
    public static final int MORE_DISCUSS_FROM_UUID = 147;
    public static final int NEXT_CLASS_INFO = 148;
    public static final int TEACHER_COUNT = 149;
    public static final int STUDY_STATE = 150;
    public static final int MINE_ALL_COURSE = 151;
    public static final int TRAIN_SCHOOL_DETAIL = 152;
    public static final int ALL_TEACHER = 153;
    public static final int GET_ASSESS_STATE = 154;
    public static final int TEACHER_DETAIL_INFO = 155;
    public static final int GET_PRIVELEGE_ACTIVE = 156;
}
