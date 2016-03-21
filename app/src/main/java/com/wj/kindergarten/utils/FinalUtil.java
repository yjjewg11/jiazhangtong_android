package com.wj.kindergarten.utils;

import android.content.Context;

import com.wj.kindergarten.ui.mine.photofamilypic.dbupdate.UploadPathDbTwo;
import com.wj.kindergarten.ui.mine.photofamilypic.dbupdate.UploadPathFour;

import net.tsz.afinal.FinalDb;

/**
 * Created by tangt on 2016/3/16.
 */
public abstract class FinalUtil {



    //创建存放已经上传的照片的数据库
    public static FinalDb getAlreadyUploadDb(Context context){
       return FinalDb.create(context,"afinal.db",true,GloablUtils.ALREADY_DB_VERSION,new UploadPathFour());
    }
    //创建单张时光轴照片的对象
    public static FinalDb getFamilyUuidObjectDb(Context context){
        return FinalDb.create(context,GloablUtils.FAMILY_UUID_OBJECT);
    }
    //存放已family_uuid为单位的家庭相册
    public static FinalDb getAllFamilyAlbum(Context context){
        return FinalDb.create(context,GloablUtils.FAMILY_UUID);
    }
}
