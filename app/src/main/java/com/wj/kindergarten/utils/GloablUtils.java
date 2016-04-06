package com.wj.kindergarten.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;

/**
 * Created by Administrator on 2015/11/9.
 */
public interface GloablUtils {

    public static final String QQ_APP_ID = "1104813270";
    public static final String WEIXIN_APP_ID = "wx6699cf8b21e12618";
    public static final String WEIXIN_SERECT = "639c78a45d012434370f4c1afc57acd1";


    int ALL_BACK = R.drawable.all_arrow;

    //家庭相册根据familyuuid分类的表
    String FAMILY_UUID = "family_uuid";
    //用来存放所有照片对象的表
    String FAMILY_UUID_OBJECT = "family_uuid_object";
    //存放上传照片状态的数据库
    String UP_LOAD_STATUS_PIC = "up_load_status_pic";

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

    String SAVE_ALREADY_UPLOAD_PIC = "save_already_upload_pic";

    String MINE_ADD_CHILD_FINISH = "add_child_finish";


    String SET_PULL_LAOUT_LIST_BACKGROUND_COLOR = "#ffffff";


    //图片查询地址
    Uri QUERY_PIC = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    int INTERRACTION_REFRESH_LIST = 136;
    int GET_PF_ALBUM_LIST_SUCCESS = 3000;
    int CLASSFIY_PF_BY_DATE = 3001;
    int PF_EDIT_TAKE_PHOTO = 3002;
    int PF_EDIT_CHOOSE_IMAGE = 3003;
    int PF_ALBUM_ADD_WATERFALL_PIC = 3004; //处理添加相册瀑布流照片
    int ADD_FAMILY_MEMBER = 3050;
    int REQUEST_CONTACT = 3051;
    int GET_SINGLE_PIC_FROM_BOUTIQUE = 3052;
    int INVITE_MORE_PEOPLE = 3053;
    int UPDATE_SUCCESSED_REFRESH = 3054;
    int DELETE_BOUTIQUE_ALBUM_SUCCESSED = 3055;
    int DELETE_FUSION_INFO_SUCCESSED = 3056;
    int GET_PIC_ADD_FAMALI_ALBUM = 3057;
    int ADD_NEW_ALBUM_SUCCESSED = 3058;
    //接受二维码链接的广播action
    //自定义上拉，下拉刷新
    int PULL_FROM_TOP = 3059;
    int DISALBED = 3060;
    int PULL_FROM_EDN = 3061;
    int PULL_BOTH = 3062;
    int FROM_UP = 3063;
    int FROM_DOWN = 3064;
    int CONFIRM_BOUND_TEL = 3065;
    int BOUND_TEL = 3066;
    int UPDATE_MY_INFO = 3067;
    int MY_INFO_REQUESTCODE_TAKE = 3068;
    int MY_INFO_REQUESTCODE_PICK = 3069;
    int MY_INFO_REQUESTCODE_CUTTING = 3070;
    int OWN = 3071;
    int BA = 3072;
    int MA = 3073;
    int OTHER = 3074;
    int SCHOOL = 3075;
    int REMARK = 3076;

    String RECEVIER_TWO_CODE_URL = "receiver_two_code_url";
    //停止音乐播放的广播
    String STOP_MUSIC_PLAY = "stop_music_play";
    String ALREADY_UPLOADING = "already_uploading";
    String ALREADY_UPLOADING_FINISHED = "already_uploading_finished";

    int MODE_OF_PF = 21;//相册
    int BOUTIQUE_COMMON_TYPE = 22;//精品相册

    String REQUEST_PIC_NEW_DATA = "notify_pic_new_data";


    //存储已上传图片的数据,
    //version --
    // 2  添加family_uuid字段
    // 3同上
    //4 添加uuid字段方便照片删除时，同时删除已上传照片。
    //5 添加拍摄日期字段
    //6 添加上传进度的正在上传进入和总进度
    int ALREADY_DB_VERSION = 6;
    String DELETE_PF_SINGLE_INFO_SUCCESSED = "delete_pf_single_info_successed";
    String UPDATE_BOUTIQUE_ALBUM_SUCCESSED = "update_boutique_album_successed";
    //存放相片的数据库表名
    String FINISH_UPLOAD_PIC = "finish_upload_pic";


    String PF_FAMILY_TABLE_OBJ_NAME = "pf_family_allobj";
    String PF_ALL_TABLE_FAMILY = "pf_all_family";


}
